package com.funongo.cms.service;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFCellUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.funongo.cms.bo.ContentBO;
import com.funongo.cms.bo.FileBO;
import com.funongo.cms.bo.UploadBO;

@Service
public class ImportService {
	
	@Autowired
	ExcelValidatorService excelValidatorService;
	
	@Autowired
	ContentService contentService;
	
	@Autowired
	DataSource dataSource;
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public UploadBO importData(FileBO fileBO){
		
		UploadBO uploadBO = new UploadBO();
		
		HashMap<String, HashSet<Integer>> ids = contentService.getAllCategoryIds();
		
		HashSet<Integer> categories = ids.get("categoryIds");
		
		HashSet<Integer> subCategories = ids.get("subCategoryIds");
		
		HashSet<Integer> genres = ids.get("genreIds");
		
		HashSet<Integer> tps = ids.get("tpIds");
		
		//HashMap<String, ArrayList<String>> error = new HashMap<String, ArrayList<String>>();
		
		HashMap<String, ArrayList<String>> errors = new HashMap<String, ArrayList<String>>();
		
		ArrayList<ContentBO> contentList = new ArrayList<ContentBO>();
		
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(fileBO.getFile().getBytes());
			Workbook workbook;
			if (fileBO.getFile().getOriginalFilename().endsWith("xls")) {
                workbook = new HSSFWorkbook(bis);
            } else if (fileBO.getFile().getOriginalFilename().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(bis);
            } else {
                throw new IllegalArgumentException("Received file does not have a standard excel extension.");
            }
			
			ArrayList<String>    colNames    = null;
			   ArrayList<Map>       mapArray    = null;
			   Row              row         = null;
			   Sheet          sheet       = null;
			   int                  sheetRows   = 0;
			   int                  rowCols     = 0;
			   Map<String, String>  rowMap      = null;

			   sheet     = workbook.getSheetAt(0);
			   sheetRows = sheet.getPhysicalNumberOfRows();
			   mapArray  = new ArrayList<Map>(sheetRows - 1);
			   colNames  = getColNames(workbook);
			   colNames.trimToSize();
			   rowCols = colNames.size();
			   ArrayList<String> rowValues = new ArrayList<String>();
			   ArrayList<String> columns = null;
			   
			   
			   for (int i = 1; i < sheetRows; i++)
			   {
			      row    = sheet.getRow(i);
			      rowMap = new HashMap<String, String>(rowCols);
			      columns = new ArrayList<String>();
			      for (int c = 0; c < rowCols; c++)
			      {
			    	 String cellValue = getCellValue(row.getCell(c));
			         rowMap.put(colNames.get(c), cellValue);
			         columns.add(cellValue);
			      }
			      
			      
			      
			      mapArray.add(rowMap);
			      columns.add("'C'");
			      columns.add("1");
			      columns.add("'admin'");
			      columns.add("getDate()");
			      columns.add("'UPLOADED'");
			      
			      // validate the content
			      errors.putAll(excelValidatorService.validateContent(rowMap, categories, subCategories, genres, tps));
			      System.out.println("error size.."+errors.size());
//			      for(HashMap<String, ArrayList<String>> errorList: errors){
//			    	  System.out.println(errorList.);
//			      }
			      
			      rowValues.add("("+StringUtils.collectionToCommaDelimitedString(columns)+")");
			   }
			   
			   colNames.add("PACK_CONTENT");
			   colNames.add("IsAPP");
			   colNames.add("UPLOADED_BY");
			   colNames.add("UPLOADED_DATE");
			   colNames.add("STATUS");
			   
			   // Comma separated column names
			   String colNameString = StringUtils.collectionToDelimitedString(colNames,",","[","]");
			   colNameString = "("+colNameString+")";
			   
			   String columnValues = "";
			   
			   if(errors.size() == 0){
				   Integer maxContentId = contentService.getMaxContentId();				   				   
				   
				   String query = "Insert into ATOM_CONTENT_TEST "+colNameString
						   +" values "+StringUtils.collectionToCommaDelimitedString(rowValues);
				   System.out.println(query);
				   
				   Connection con = null;
				   PreparedStatement ps = null;
					
				try{
					con = dataSource.getConnection();
					ps = con.prepareStatement(query);
					int rows = ps.executeUpdate();
					
					if(rows > 0){
						contentList = contentService.getCurrentlyUpdatedContent(maxContentId, rows);
					}
					LOGGER.info("rows manipulated: "+rows);
				}
				catch(Exception e){
					LOGGER.info(e.getMessage());
				}
				finally{
					try {
						
						if(ps != null){
							ps.close();					
						}
						if(con != null){
							con.close();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						LOGGER.setLevel(Level.INFO);
					    LOGGER.info(e.getMessage());				
					}
				}
				   
			   }  

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		uploadBO.setContentList(contentList);
		uploadBO.setErrors(errors);
		
		return uploadBO;
        
	}
	
	public ArrayList<String> getColNames(Workbook workbook){
	   ArrayList<String> colNames = new ArrayList<String>();
	   Sheet sheet = workbook.getSheetAt(0);
	   Row   row   = sheet.getRow(0);
	   Cell  cell  = null;
	   int       cols  = 0;
	   if (row != null)
	   {
	      cols = row.getPhysicalNumberOfCells();
	      for (int i = 0; i < cols; i++)
	      {
	         cell = row.getCell(i);
	         if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING)
	         {
	        	String header = cell.getRichStringCellValue().getString();
	        	System.out.println(header);
	            colNames.add(header);
	         }
	      }
	   }
	   return colNames;
	}
	
	public String  getCellValue(Cell cell){
		
		 System.out.println("cell "+cell);
		 if(cell == null){
			 return null;
		 }
		 System.out.println("cell type "+cell.getCellType());
		
		 switch (cell.getCellType())
         {
             case Cell.CELL_TYPE_NUMERIC:{
            	 if(DateUtil.isCellDateFormatted(cell)){System.out.println("date formatted");
	     			Date date = DateUtil.getJavaDate(cell.getNumericCellValue());
	     			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	     			String dateString = sdf.format(date);
	     			System.out.println("date string "+dateString);
	     			return "'"+dateString+"'";
	     	     }
                 return cell.getNumericCellValue()+"";   
             }
             case Cell.CELL_TYPE_STRING:
                 return "'"+cell.getStringCellValue()+"'";                
         }
		 
		return null;		
	}

}
