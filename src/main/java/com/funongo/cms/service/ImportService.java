package com.funongo.cms.service;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.funongo.cms.bo.Content;
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

	@Autowired
	JdbcTemplate template;

	@Resource(name = "appProperties")
	Properties properties;

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public UploadBO importData(FileBO fileBO) {

		UploadBO uploadBO = new UploadBO();

		String app = fileBO.isApp() ? "1" : "0";

		String wap = fileBO.isWap() ? "1" : "0";
		
		HashMap<String, ArrayList<String>> errors = new HashMap<String, ArrayList<String>>();

		HashMap<String, HashSet<Integer>> ids = new HashMap<String, HashSet<Integer>>();
		try{
			ids = contentService.getAllCategoryIds();
		}
		catch(Exception e){
			e.printStackTrace();
			ArrayList<String> errorList= new ArrayList<String>();
			errorList.add("Error in connecting to database");
			errors.put("General", errorList);
		}
				

		HashSet<Integer> categories = ids.get("categoryIds");

		HashSet<Integer> subCategories = ids.get("subCategoryIds");

		HashSet<Integer> genres = ids.get("genreIds");

		HashSet<Integer> tps = ids.get("tpIds");

		// HashMap<String, ArrayList<String>> error = new HashMap<String,
		// ArrayList<String>>();

		

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

			ArrayList<String> colNames = null;
			ArrayList<Map> mapArray = null;
			Row row = null;
			Sheet sheet = null;
			int sheetRows = 0;
			int rowCols = 0;
			Map<String, String> rowMap = null;

			sheet = workbook.getSheetAt(0);
			sheetRows = sheet.getPhysicalNumberOfRows();
			mapArray = new ArrayList<Map>(sheetRows - 1);
			colNames = getColNames(workbook);
			colNames.trimToSize();
			rowCols = colNames.size();
			ArrayList<String> rowValues = new ArrayList<String>();
			ArrayList<String> columns = null;

			for (int i = 1; i < sheetRows; i++) {
				row = sheet.getRow(i);
				rowMap = new HashMap<String, String>(rowCols);
				columns = new ArrayList<String>();
				for (int c = 0; c < rowCols; c++) {
					String cellValue = getCellValue(row.getCell(c));
					rowMap.put(colNames.get(c), cellValue);
					columns.add(cellValue);
				}

				mapArray.add(rowMap);
				
				// Column Values
				columns.add("'C'");
				columns.add(app);
				columns.add(wap);
				columns.add("'admin'");
				columns.add("getDate()");
				columns.add("'APPROVED'");
				columns.add("getDate()");
				

				// validate the content
				errors.putAll(excelValidatorService.validateContent(rowMap, categories, subCategories, genres, tps));
				System.out.println("error size.." + errors.size());
				// for(HashMap<String, ArrayList<String>> errorList: errors){
				// System.out.println(errorList.);
				// }
				
				// as SmartUrlProvider is added later, from the map take the value of it and put to columns
				columns.add(rowMap.get("SMARTURLPROVIDER"));

				rowValues.add("(" + StringUtils.collectionToCommaDelimitedString(columns) + ")");
			}
			
			// Column Headers
			colNames.add("PACK_CONTENT");
			colNames.add("IsAPP");
			colNames.add("IsWAP");
			colNames.add("UPLOADED_BY");
			colNames.add("UPLOADED_DATE");
			colNames.add("STATUS");
			colNames.add("APPROVED_DATE");
			
			// also add SMARTURLPROVIDER to column headers
			colNames.add("SMARTURLPROVIDER");

			// Comma separated column names
			String colNameString = StringUtils.collectionToDelimitedString(colNames, ",", "[", "]");
			colNameString = "(" + colNameString + ")";

			String columnValues = "";

			if (errors.size() == 0) {
				Integer maxContentId = contentService.getMaxContentId();
				String tableName = properties.getProperty("contentTable");
				String query = "Insert into " + tableName + " " + colNameString + " values "
						+ StringUtils.collectionToCommaDelimitedString(rowValues);
				// System.out.println(query);

				Connection con = null;
				PreparedStatement ps = null;

				try {
					con = dataSource.getConnection();
					ps = con.prepareStatement(query);
					int rows = ps.executeUpdate();

					if (rows > 0) {
						contentList = contentService.getCurrentlyUpdatedContent(maxContentId, rows);
					}
					LOGGER.info("rows manipulated: " + rows);
				} catch (Exception e) {
					ArrayList<String> exceptionList = new ArrayList<String>();
					exceptionList.add(e.getMessage());
					errors.put("General", exceptionList);
					LOGGER.info(e.getMessage());
				} finally {
					try {

						if (ps != null) {
							ps.close();
						}
						if (con != null) {
							con.close();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						ArrayList<String> exceptionList = new ArrayList<String>();
						exceptionList.add(e.getMessage());
						errors.put("General", exceptionList);
						LOGGER.setLevel(Level.INFO);
						LOGGER.info(e.getMessage());
					}
				}

				if (errors.size() == 0) {
					String contentTable = properties.getProperty("contentTable");
					String approvedTable = properties.getProperty("approvedTable");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String currentDate = sdf.format(new Date());

					String approveQuery = "Insert into " + approvedTable
							+ "([CONTENT_ID], [CONTENT_NAME], [DISPLAY_NAME], [TP_ID], [CATEGORY_ID], [SUBCATEGORY_ID], [WRAPPING_PARTNER], [COST],"
							+ " [GENRE_ID], [SUB_GENRE], [LANGUAGE], [RATING], [SEARCH], [SHORT_DESCRIPTION], [LONG_DESCRIPTION], [CONTENT_TYPE], "
							+ " [STATUS], [ALBUM_ID], [MOVIE_ID], [OS_ID], [FILE_SIZE], [CONTENT_PRODUCTION_DATE], [AGE_GROUP], [UPLOADED_BY], "
							+ " [UPLOADED_DATE], [VALIDFROM], [VALIDTO], [APPROVED_DATE], [IMAGE_PREVIEW], [AUDIO_PREVIEW], [VIDEO_PREVIEW], "
							+ " [PREVIEW_FILE_NAME], [CONTENT_VERSION], [SMARTURL1], [SMARTURL2], [SMARTURL3], [DIRECTORS], [PRODUCERS], [MUSIC_DIRECTORS], "
							+ " [ACTORS], [ACTRESSES], [SINGERS], [CHOREOGRAPHER], [SUPPORTING_STAR_CAST], [LYRICIST], [REVIEW], [RELEASEDATE], "
							+ " [PRODUCTION_COMPANIES], [IMAGE_PREVIEW1], [IMAGE_PREVIEW2], [IMAGE_PREVIEW3], [IMAGE_PREVIEW4], [IMAGE_PREVIEW5], "
							+ " [POSTERURL1], [POSTERURL2], [POSTERURL3], [POSTERURL4], [POSTERURL5], [POSTERURL6], [DURATION], [GRADE], [C_LANG], "
							+ " [PACK_CONTENT], [FILESIZE480], [FILESIZE360], [FILESIZE240], [SMARTURLPROVIDER], [SMARTURL2SIZE480],"
							+ " [SMARTURL2SIZE360], [SMARTURL2SIZE240], [SMARTURL2SIZE720])"							
							+ " select [CONTENT_ID], [CONTENT_NAME], [DISPLAY_NAME], [TP_ID], [CATEGORY_ID], [SUBCATEGORY_ID], [WRAPPING_PARTNER], "
							+ " [COST], [GENRE_ID], [SUB_GENRE], [LANGUAGE], [RATING], [SEARCH], [SHORT_DESCRIPTION], [LONG_DESCRIPTION], "
							+ " [CONTENT_TYPE], [STATUS], [ALBUM_ID], [MOVIE_ID], [OS_ID], [FILE_SIZE], [CONTENT_PRODUCTION_DATE], [AGE_GROUP], "
							+ " [UPLOADED_BY], [UPLOADED_DATE], [VALIDFROM], [VALIDTO], [APPROVED_DATE], [IMAGE_PREVIEW], [AUDIO_PREVIEW], "
							+ " [VIDEO_PREVIEW], [PREVIEW_FILE_NAME], [CONTENT_VERSION], [SMARTURL1], [SMARTURL2], [SMARTURL3], [DIRECTORS], [PRODUCERS], "
							+ " [MUSIC_DIRECTORS], [ACTORS], [ACTRESSES], [SINGERS], [CHOREOGRAPHER], [SUPPORTING_STAR_CAST], [LYRICIST], [REVIEW], "
							+ " [RELEASEDATE], [PRODUCTION_COMPANIES], [IMAGE_PREVIEW1], [IMAGE_PREVIEW2], [IMAGE_PREVIEW3], [IMAGE_PREVIEW4], "
							+ " [IMAGE_PREVIEW5], [POSTERURL1], [POSTERURL2], [POSTERURL3], [POSTERURL4], [POSTERURL5], [POSTERURL6], [DURATION], "
							+ " [GRADE], [C_LANG], [PACK_CONTENT], [FILESIZE480], [FILESIZE360], [FILESIZE240], [SMARTURLPROVIDER], [SMARTURL2SIZE480], "
							+ " [SMARTURL2SIZE360], [SMARTURL2SIZE240], [SMARTURL2SIZE720]"
							+ " from " + contentTable + " where uploaded_date>='" + currentDate + "' and IsApp=1 "
							+ " and content_id not in (select content_id from " + approvedTable + ") and content_id > "+maxContentId;

					// System.out.println(approveQuery);

					try {
						con = dataSource.getConnection();
						ps = con.prepareStatement(approveQuery);
						int rows = ps.executeUpdate();

						LOGGER.info("rows inserted in approved: " + rows);
					} catch (Exception e) {
						ArrayList<String> exceptionList = new ArrayList<String>();
						exceptionList.add(e.getMessage());
						errors.put("General", exceptionList);
						LOGGER.info(e.getMessage());
					} finally {
						try {

							if (ps != null) {
								ps.close();
							}
							if (con != null) {
								con.close();
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							ArrayList<String> exceptionList = new ArrayList<String>();
							exceptionList.add(e.getMessage());
							errors.put("General", exceptionList);
							LOGGER.setLevel(Level.INFO);
							LOGGER.info(e.getMessage());
						}
					}

					String procedureQuery = "Exec [Portaldb].[dbo].["
							+ properties.getProperty("updateSearchInsertionProcedure") + "]";
					Connection connection = null;
					PreparedStatement preparedStatement = null;
					try {
						connection = dataSource.getConnection();
						preparedStatement = connection.prepareStatement(procedureQuery);
						preparedStatement.execute();
					} catch (Exception e) {
						e.printStackTrace();
						LOGGER.info("catch proc");
						LOGGER.info(e.getMessage());
					} finally {
						try {

							if (preparedStatement != null) {
								preparedStatement.close();
							}
							if (connection != null) {
								connection.close();
							}
						} catch (Exception e) {
							LOGGER.info("catch proc1");
							// TODO Auto-generated catch block
							LOGGER.setLevel(Level.INFO);
							LOGGER.info(e.getMessage());
						}
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

	public UploadBO importLangData(FileBO fileBO) {
		UploadBO uploadBO = new UploadBO();
		// HashMap<String, ArrayList<String>> error = new HashMap<String,
		// ArrayList<String>>();
		final List<Content> contents = new ArrayList<Content>();
		HashMap<String, ArrayList<String>> errors = new HashMap<String, ArrayList<String>>();

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

			ArrayList<String> colNames = null;
			ArrayList<Map> mapArray = null;
			Row row = null;
			Sheet sheet = null;
			int sheetRows = 0;
			int rowCols = 0;
			Map<String, String> rowMap = null;

			sheet = workbook.getSheetAt(0);
			sheetRows = sheet.getPhysicalNumberOfRows();
			mapArray = new ArrayList<Map>(sheetRows - 1);
			colNames = getColNames(workbook);
			colNames.trimToSize();
			rowCols = colNames.size();

			for (int i = 1; i < sheetRows; i++) {
				row = sheet.getRow(i);
				rowMap = new HashMap<String, String>(rowCols);

				for (int c = 0; c < rowCols; c++) {
					String cellValue = getCellValue(row.getCell(c));
					rowMap.put(colNames.get(c), cellValue);
				}
				mapArray.add(rowMap);
				Content content = new Content();

				errors.putAll(excelValidatorService.validateLanguageContent(rowMap, content));
				if (errors.isEmpty()) {
					contents.add(content);
				}
			}
			if (errors.size() == 0) {
				String tableName = properties.getProperty("contentLangTable");
				String sql = "INSERT INTO " + tableName
						+ " (CONTENT_ID, CONTENT_NAME, DISPLAY_NAME, COST, METALANG, RATING, SEARCH, SHORT_DESCRIPTION, LONG_DESCRIPTION, STATUS, CONTENT_PRODUCTION_DATE, UPLOADED_BY, UPLOADED_DATE, VALIDFROM, VALIDTO, APPROVED_DATE, IMAGE_PREVIEW, AUDIO_PREVIEW, VIDEO_PREVIEW, PREVIEW_FILE_NAME, CONTENT_VERSION, DIRECTORS, PRODUCERS, MUSIC_DIRECTORS, ACTORS, ACTRESSES, SINGERS, CHOREOGRAPHER, SUPPORTING_STAR_CAST, LYRICIST, RELEASEDATE, PRODUCTION_COMPANIES, REVIEW, IMAGE_PREVIEW1, IMAGE_PREVIEW2, IMAGE_PREVIEW3, IMAGE_PREVIEW4, IMAGE_PREVIEW5, POSTERURL1, POSTERURL2, POSTERURL3, POSTERURL4, POSTERURL5, POSTERURL6, CATEGORY_ID, FILE_SIZE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
				template.batchUpdate(sql, new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						Content content = contents.get(i);
						ps.setInt(1, content.getContentId());
						ps.setString(2, content.getContentName());
						ps.setString(3, content.getDisplayName());
						ps.setDouble(4, content.getCost());
						ps.setString(5, content.getLanguage());
						ps.setInt(6, content.getRating());
						ps.setString(7, content.getSearch());
						ps.setString(8, content.getShortDescription());
						ps.setString(9, content.getLongDescription());
						ps.setString(10, content.getStatus());
						if (content.getContentProductionDate() == null)
							ps.setDate(11, null);
						else
							ps.setDate(11, new java.sql.Date(content.getContentProductionDate().getTime()));
						ps.setString(12, content.getUploadedBy());
						ps.setTimestamp(13, new Timestamp(System.currentTimeMillis()));
						if (content.getValidFrom() == null)
							ps.setDate(14, null);
						else
							ps.setDate(14, new java.sql.Date(content.getValidFrom().getTime()));
						if (content.getValidTo() == null)
							ps.setDate(15, null);
						else
							ps.setDate(15, new java.sql.Date(content.getValidTo().getTime()));
						if (content.getApprovedDate() == null)
							ps.setDate(16, null);
						else
							ps.setDate(16, new java.sql.Date(content.getApprovedDate().getTime()));
						ps.setInt(17, content.getImagePreview());
						ps.setInt(18, content.getAudioPreview());
						ps.setInt(19, content.getVideoPreview());
						ps.setString(20, content.getPreviewFileName());
						ps.setString(21, content.getContentVersion());
						ps.setString(22, content.getDirectors());
						ps.setString(23, content.getProducers());
						ps.setString(24, content.getMusicDirectors());
						ps.setString(25, content.getActors());
						ps.setString(26, content.getActresses());
						ps.setString(27, content.getSingers());
						ps.setString(28, content.getChoreographer());
						ps.setString(29, content.getSupportingStarCast());
						ps.setString(30, content.getLyricist());
						if (content.getReleaseDate() == null)
							ps.setDate(31, null);
						else
							ps.setDate(31, new java.sql.Date(content.getReleaseDate().getTime()));
						ps.setString(32, content.getProductionCompanies());
						ps.setString(33, content.getReview());
						ps.setInt(34, content.getImagePreview1());
						ps.setInt(35, content.getImagePreview2());
						ps.setInt(36, content.getImagePreview3());
						ps.setInt(37, content.getImagePreview4());
						ps.setInt(38, content.getImagePreview5());
						ps.setInt(39, content.getPosterUrl1());
						ps.setInt(40, content.getPosterUrl2());
						ps.setInt(41, content.getPosterUrl3());
						ps.setInt(42, content.getPosterUrl4());
						ps.setInt(43, content.getPosterUrl5());
						ps.setInt(44, content.getPosterUrl6());
						ps.setInt(45, content.getCategoryId());
						ps.setFloat(46, content.getFileSize());
					}

					@Override
					public int getBatchSize() {
						return contents.size();
					}
				});
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		uploadBO.setErrors(errors);
		uploadBO.setSize(contents.size());
		return uploadBO;
	}

	public ArrayList<String> getColNames(Workbook workbook) {
		ArrayList<String> colNames = new ArrayList<String>();
		Sheet sheet = workbook.getSheetAt(0);
		Row row = sheet.getRow(0);
		Cell cell = null;
		int cols = 0;
		if (row != null) {
			cols = row.getPhysicalNumberOfCells();
			for (int i = 0; i < cols; i++) {
				cell = row.getCell(i);
				if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
					String header = cell.getRichStringCellValue().getString();
					System.out.println(header);
					colNames.add(header);
				}
			}
		}
		return colNames;
	}

	public String getCellValue(Cell cell) {

		System.out.println("cell " + cell);
		if (cell == null) {
			return null;
		}
		System.out.println("cell type " + cell.getCellType());

		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC: {
			if (DateUtil.isCellDateFormatted(cell)) {
				System.out.println("date formatted");
				Date date = DateUtil.getJavaDate(cell.getNumericCellValue());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateString = sdf.format(date);
				System.out.println("date string " + dateString);
				return "'" + dateString + "'";
			}
			return cell.getNumericCellValue() + "";
		}
		case Cell.CELL_TYPE_STRING:
			return "'" + cell.getStringCellValue() + "'";
		}

		return null;
	}

}
