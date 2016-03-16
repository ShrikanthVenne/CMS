package com.funongo.cms.service;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.funongo.cms.bo.EmailBO;

@Service
public class ReportService {
	
	@Autowired
	DataSource dataSource;
	
	public void sendDailyReport(){
		Properties properties = new Properties();
		FileInputStream file;
		try {
			file = new FileInputStream("C:\\cms_config\\emailProperties.properties");			
			properties.load(file);
			file.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String from = properties.getProperty("emailFrom");
		String username = properties.getProperty("username");
		String password = properties.getProperty("password");
		String subject = properties.getProperty("contentSummarySubject");
		String toList = properties.getProperty("dailyReportTo");
		String to[] = toList.split(",");
		String message = "<html><body>"
						+"Dear All, <br/><br/>"
				        +"<b>Below is the Summary of Content Uploaded Today on Chillx :</b><br/><br/>"
				        + "</body></html>";	
		
		
		
		String query1 = "select b.category_name Category, c.genre_name Genre, count(distinct(a.content_id)) No_Of_Content from atom_content_approved a"
				+" left outer join atom_category b on a.category_id=b.category_id "
				+" left outer join atom_genre c on a.genre_id=c.genre_id "
				+" where uploaded_date >=CONVERT(VARCHAR(10),GETDATE(),110) "
				+" group by b.CATEGORY_NAME, c.genre_name "
				+" UNION ALL " 
				+" Select 'Total', '', count(distinct(content_id)) from atom_content_approved where uploaded_date >=CONVERT(VARCHAR(10),GETDATE(),110)";
		
		String query2 = "Exec [Spx_CMSUploadStats]";

		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");			
			con = DriverManager.getConnection("jdbc:sqlserver://43.242.215.66;databaseName=CMS_Trial;user=sa;password=sql@123");
			
			
			ps = con.prepareStatement(query1);
			rs = ps.executeQuery();
			
			message += "<table border='1' cellspacing='0', cellpadding='3'><thead><tr>"
					+"<th>Category</th>"
					+"<th>Genre</th>"
					+"<th>No_Of_Content</th>"					
					+"</tr></thead>";
			
			while(rs.next()){
				if(rs.getString("Category").equalsIgnoreCase("Total")){
					message += "<tr>"
							+ "<td><b>"+rs.getString("Category")+"</b></td>"
							+ "<td><b>"+rs.getString("Genre")+"</b></td>"
							+ "<td align='right'><b>"+rs.getString("No_Of_Content")+"</b></td>"						
							+ "</tr>";
				}
				else{
					message += "<tr>"
							+ "<td>"+rs.getString("Category")+"</td>"
							+ "<td>"+rs.getString("Genre")+"</td>"
							+ "<td align='right'>"+rs.getString("No_Of_Content")+"</td>"						
							+ "</tr>";
				}
				
				//System.out.println(rs.getString(1));
			}
			message += "</table>";
			
			message+="<br/><br/>";
			
			message+="<b>Summary of Total Content Uploaded Till Date on Chillx : <b><br/><br/>";
			
			rs.close();
			ps.close();
			
			
			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			message += "<table border='1' cellspacing='0', cellpadding='3'><thead><tr>"
					+"<th>Category</th>"
					+"<th>Injected</th>"
					+"<th>Metadata_English</th>"
					+"<th>Dubbing_Started</th>"
					+"<th>Subtitle_Started</th>"
					+"<th>Posterurl</th>"
					+"<th>Posters_ML</th>"
					+"<th>PreviewImage</th>"
					+"<th>Video_Preview</th>"
					+"<th>Saranyu_URL</th>"
					+"<th>Touchfone_URL</th>"
					+"<th>Youtube_URL</th>"
					+"</tr></thead>";
			while(rs.next()){
				if(rs.getString("Category").equalsIgnoreCase("Total")){
					message += "<tr>"
							+ "<td><b>"+rs.getString("Category")+"</td>"
							+ "<td align='right'><b>"+rs.getString("Injected")+"</b></td>"
							+ "<td align='right'><b>"+rs.getString("Metadata_English")+"</b></td>"
							+ "<td align='right'><b>"+rs.getString("Dubbing_Started")+"</b></td>"
							+ "<td align='right'><b>"+rs.getString("Subtitle_Started")+"</b></td>"
							+ "<td align='right'><b>"+rs.getString("Posterurl")+"</b></td>"
							+ "<td align='right'><b>"+rs.getString("Posters_ML")+"</b></td>"
							+ "<td align='right'><b>"+rs.getString("PreviewImage")+"</b></td>"
							+ "<td align='right'><b>"+rs.getString("Video_Preview")+"</b></td>"
							+ "<td align='right'><b>"+rs.getString("Saranyu_URL")+"</b></td>"
							+ "<td align='right'><b>"+rs.getString("Touchfone_URL")+"</b></td>"
							+ "<td align='right'><b>"+rs.getString("Youtube_URL")+"</b></td>"
							+ "</tr>";
				}
				else{
					message += "<tr>"
							+ "<td>"+rs.getString("Category")+"</td>"
							+ "<td align='right'>"+rs.getString("Injected")+"</td>"
							+ "<td align='right'>"+rs.getString("Metadata_English")+"</td>"
							+ "<td align='right'>"+rs.getString("Dubbing_Started")+"</td>"
							+ "<td align='right'>"+rs.getString("Subtitle_Started")+"</td>"
							+ "<td align='right'>"+rs.getString("Posterurl")+"</td>"
							+ "<td align='right'>"+rs.getString("Posters_ML")+"</td>"
							+ "<td align='right'>"+rs.getString("PreviewImage")+"</td>"
							+ "<td align='right'>"+rs.getString("Video_Preview")+"</td>"
							+ "<td align='right'>"+rs.getString("Saranyu_URL")+"</td>"
							+ "<td align='right'>"+rs.getString("Touchfone_URL")+"</td>"
							+ "<td align='right'>"+rs.getString("Youtube_URL")+"</td>"
							+ "</tr>";
				}
				
				//System.out.println(rs.getString(1));
			}
			message += "</table>";
			
			message+="<br/><br/>";
			message+="**This is a system generated email, please do not reply to this email";
			
			System.out.println(message);
			
			rs.close();
			ps.close();
			
						

			
			
			/*String query3 = "";
			ps = con.prepareStatement(query3);
			rs = ps.executeQuery();
			
			message += "<table border=1 cellspacing=0, cellpadding=0><thead><tr>"
					+"<th>Category</th>"
					+"<th>Injected</th>"
					+"<th>Metadata_English</th>"
					+"<th>Dubbing_Started</th>"
					+"<th>Subtitle_Started</th>"
					+"<th>Posterurl</th>"
					+"<th>Posters_ML</th>"
					+"<th>PreviewImage</th>"
					+"<th>Video_Preview</th>"
					+"<th>Saranyu_URL</th>"
					+"<th>Touchfone_URL</th>"
					+"<th>Youtube_URL</th>"
					+"</tr></thead>";
			
			while(rs.next()){
				message += "<tr>"
						+ "<td>"+rs.getString("Category")+"</td>"
						+ "<td>"+rs.getString("Injected")+"</td>"
						+ "<td>"+rs.getString("Metadata_English")+"</td>"
						+ "<td>"+rs.getString("Dubbing_Started")+"</td>"
						+ "<td>"+rs.getString("Subtitle_Started")+"</td>"
						+ "<td>"+rs.getString("Posterurl")+"</td>"
						+ "<td>"+rs.getString("Posters_ML")+"</td>"
						+ "<td>"+rs.getString("PreviewImage")+"</td>"
						+ "<td>"+rs.getString("Video_Preview")+"</td>"
						+ "<td>"+rs.getString("Saranyu_URL")+"</td>"
						+ "<td>"+rs.getString("Touchfone_URL")+"</td>"
						+ "<td>"+rs.getString("Youtube_URL")+"</td>"
						+ "</tr>";
				//System.out.println(rs.getString(1));
			}
			message += "</table>";
			
			message+="<br/><br/>";
			
			rs.close();
			ps.close();*/
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try {
				if(rs != null){				
					rs.close();													
				}
				if(ps != null){
					ps.close();					
				}
				if(con != null){
					con.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();				    
			}
		}
		
		EmailBO emailBO = new EmailBO(from, to, subject, message);
		SendEmailService.sendEmail(emailBO,username, password);
		}
}
