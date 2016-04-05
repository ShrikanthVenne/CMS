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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.funongo.cms.bo.EmailBO;

@Service
public class ReportService {

	@Autowired
	@Qualifier("portalDs")
	DataSource dataSource;

	public void sendDailyReport() {
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
		String message = "<html><body>" + "Dear All, <br/><br/>"
				+ "<b>Below is the Summary of Content Uploaded Today on Chillx :</b><br/><br/>" + "</body></html>";

		String query1 = "Exec [Spx_CMSUploadStats] 1";

		String query2 = "Exec [Spx_CMSUploadStats] 2";

		String query3 = "Exec [Spx_CMSUploadStats] 3";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager
					.getConnection("jdbc:sqlserver://43.242.215.66;databaseName=CMS_Trial;user=sa;password=sql@123");

			ps = con.prepareStatement(query1);
			rs = ps.executeQuery();

			message += "<table border='1' cellspacing='0', cellpadding='3'><thead><tr>" + "<th>Category</th>"
					+ "<th>Genre</th>" + "<th>No_Of_Content</th>" + "</tr></thead>";

			while (rs.next()) {
				if (rs.getString("Category").equalsIgnoreCase("Total")) {
					message += "<tr>" + "<td><b>" + rs.getString("Category") + "</b></td>" + "<td><b>"
							+ rs.getString("Genre") + "</b></td>" + "<td align='right'><b>"
							+ rs.getString("No_Of_Content") + "</b></td>" + "</tr>";
				} else {
					message += "<tr>" + "<td>" + rs.getString("Category") + "</td>" + "<td>" + rs.getString("Genre")
							+ "</td>" + "<td align='right'>" + rs.getString("No_Of_Content") + "</td>" + "</tr>";
				}

				// System.out.println(rs.getString(1));
			}
			message += "</table>";

			message += "<br/><br/>";

			message += "<b>Summary of Total Content Uploaded Till Date on Chillx : <b><br/><br/>";

			rs.close();
			ps.close();

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			message += "<table border='1' cellspacing='0', cellpadding='3'><thead><tr>" + "<th>Category</th>"
					+ "<th>Injected</th>" + "<th>Metadata_English</th>" + "<th>Dubbing_Started</th>"
					+ "<th>Subtitle_Started</th>" + "<th>Posterurl</th>" + "<th>Posters_ML</th>"
					+ "<th>PreviewImage</th>" + "<th>Video_Preview</th>" + "<th>Saranyu_URL</th>"
					+ "<th>Touchfone_URL</th>" + "<th>Youtube_URL</th>" + "</tr></thead>";
			while (rs.next()) {
				if (rs.getString("Category").equalsIgnoreCase("Total")) {
					message += "<tr>" + "<td><b>" + rs.getString("Category") + "</td>" + "<td align='right'><b>"
							+ rs.getString("Injected") + "</b></td>" + "<td align='right'><b>"
							+ rs.getString("Metadata_English") + "</b></td>" + "<td align='right'><b>"
							+ rs.getString("Dubbing_Started") + "</b></td>" + "<td align='right'><b>"
							+ rs.getString("Subtitle_Started") + "</b></td>" + "<td align='right'><b>"
							+ rs.getString("Posterurl") + "</b></td>" + "<td align='right'><b>"
							+ rs.getString("Posters_ML") + "</b></td>" + "<td align='right'><b>"
							+ rs.getString("PreviewImage") + "</b></td>" + "<td align='right'><b>"
							+ rs.getString("Video_Preview") + "</b></td>" + "<td align='right'><b>"
							+ rs.getString("Saranyu_URL") + "</b></td>" + "<td align='right'><b>"
							+ rs.getString("Touchfone_URL") + "</b></td>" + "<td align='right'><b>"
							+ rs.getString("Youtube_URL") + "</b></td>" + "</tr>";
				} else {
					message += "<tr>" + "<td>" + rs.getString("Category") + "</td>" + "<td align='right'>"
							+ rs.getString("Injected") + "</td>" + "<td align='right'>"
							+ rs.getString("Metadata_English") + "</td>" + "<td align='right'>"
							+ rs.getString("Dubbing_Started") + "</td>" + "<td align='right'>"
							+ rs.getString("Subtitle_Started") + "</td>" + "<td align='right'>"
							+ rs.getString("Posterurl") + "</td>" + "<td align='right'>" + rs.getString("Posters_ML")
							+ "</td>" + "<td align='right'>" + rs.getString("PreviewImage") + "</td>"
							+ "<td align='right'>" + rs.getString("Video_Preview") + "</td>" + "<td align='right'>"
							+ rs.getString("Saranyu_URL") + "</td>" + "<td align='right'>"
							+ rs.getString("Touchfone_URL") + "</td>" + "<td align='right'>"
							+ rs.getString("Youtube_URL") + "</td>" + "</tr>";
				}

				// System.out.println(rs.getString(1));
			}
			message += "</table>";

			message += "<br/><br/>";
			message += "<b>Summary of Metadata in Different Languages on Chillx :<b><br/><br/>";

			System.out.println(message);

			rs.close();
			ps.close();

			ps = con.prepareStatement(query3);
			rs = ps.executeQuery();

			message += "<table  border='1' cellspacing='0', cellpadding='3'><thead><tr>" + "<th>Category</th>"
					+ "<th>Metadata_English</th>" + "<th>Hindi</th>" + "<th>Marathi</th>" + "<th>Gujarati</th>"
					+ "<th>Punjabi</th>" + "<th>Bengali</th>" + "<th>Tamil</th>" + "<th>Telugu</th>"
					+ "<th>Malayalam</th>" + "<th>Kannada</th>" + "</tr></thead>";

			while (rs.next()) {
				if (rs.getString("Category").equalsIgnoreCase("Total")) {
					message += "<tr>" + "<td><b>" + rs.getString("Category") + "</b></td>" + "<td align='right'><b>"
							+ rs.getString("Metadata_English") + "</td>" + "<td align='right'><b>"
							+ rs.getString("Metadata_Hindi") + "</td>" + "<td align='right'><b>"
							+ rs.getString("Metadata_Marathi") + "</td>" + "<td align='right'><b>"
							+ rs.getString("Metadata_Gujarati") + "</td>" + "<td align='right'><b>"
							+ rs.getString("Metadata_Punjabi") + "</td>" + "<td align='right'><b>"
							+ rs.getString("Metadata_Bengali") + "</td>" + "<td align='right'><b>"
							+ rs.getString("Metadata_Tamil") + "</td>" + "<td align='right'><b>"
							+ rs.getString("Metadata_Telugu") + "</td>" + "<td align='right'><b>"
							+ rs.getString("Metadata_Malayalam") + "</td>" + "<td align='right'><b>"
							+ rs.getString("Metadata_Kannada") + "</td>" + "</tr>";
				} else {
					message += "<tr>" + "<td>" + rs.getString("Category") + "</td>" + "<td align='right'>"
							+ rs.getString("Metadata_English") + "</td>" + "<td align='right'>"
							+ rs.getString("Metadata_Hindi") + "</td>" + "<td align='right'>"
							+ rs.getString("Metadata_Marathi") + "</td>" + "<td align='right'>"
							+ rs.getString("Metadata_Gujarati") + "</td>" + "<td align='right'>"
							+ rs.getString("Metadata_Punjabi") + "</td>" + "<td align='right'>"
							+ rs.getString("Metadata_Bengali") + "</td>" + "<td align='right'>"
							+ rs.getString("Metadata_Tamil") + "</td>" + "<td align='right'>"
							+ rs.getString("Metadata_Telugu") + "</td>" + "<td align='right'>"
							+ rs.getString("Metadata_Malayalam") + "</td>" + "<td align='right'>"
							+ rs.getString("Metadata_Kannada") + "</td>" + "</tr>";
				}
			}
			message += "</table>";

			message += "<br/><br/>";

			message += "**This is a system generated email, please do not reply to this email";

			rs.close();
			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		EmailBO emailBO = new EmailBO(from, to, subject, message);
		SendEmailService.sendEmail(emailBO, username, password);
	}
}
