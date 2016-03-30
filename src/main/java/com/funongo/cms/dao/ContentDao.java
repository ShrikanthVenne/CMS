package com.funongo.cms.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.funongo.cms.bo.CategoryBO;
import com.funongo.cms.bo.Content;
import com.funongo.cms.bo.Genre;
import com.funongo.cms.bo.TP;

@Repository
public class ContentDao {
	@Autowired
	@Qualifier("portalDB")
	JdbcTemplate template;

	@Autowired
	@Qualifier("CMSTrial")
	JdbcTemplate jdbcTemplate;

	@Resource(name = "appProperties")
	Properties properties;

	public List<CategoryBO> getCategoryList() {
		List<CategoryBO> categoryList = new ArrayList<CategoryBO>();
		String sql = "select CATEGORY_ID, CATEGORY_NAME FROM ATOM_CATEGORY ORDER BY CATEGORY_ID ASC";
		RowMapper<CategoryBO> mapper = new RowMapper<CategoryBO>() {

			@Override
			public CategoryBO mapRow(ResultSet rs, int rowNum) throws SQLException {
				CategoryBO categoryBo = new CategoryBO();
				categoryBo.setCategory_id(rs.getInt("CATEGORY_ID"));
				categoryBo.setCategory_name(rs.getString("CATEGORY_NAME"));
				return categoryBo;
			}
		};
		categoryList.addAll(jdbcTemplate.query(sql, mapper));
		return categoryList;
	}

	public List<Content> getContentList(CategoryBO category, String search, String fromDate, String toDate) {
		List<Content> contents = new ArrayList<Content>();
		String tableName = properties.getProperty("contentTable");
		StringBuilder builder = new StringBuilder();
		if (search != "") {
			builder.append(" AND A.DISPLAY_NAME  LIKE '%" + search + "%'");
		}
		Date uploadedFromDate = null;
		Date uploadedToDate = null;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		if (fromDate.equals("")) {
			uploadedFromDate = new Date(DateTime.parse("2015-01-01").toDate().getTime());
		} else {
			java.util.Date date = null;
			try {
				date = format.parse(fromDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			uploadedFromDate = new Date(date.getTime());
		}
		if (toDate.equals("")) {
			uploadedToDate = new java.sql.Date(DateTime.now().plusDays(1).toDate().getTime());
		} else {
			java.util.Date date = null;
			try {
				date = format.parse(toDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			uploadedToDate = new Date(date.getTime() + 24 * 60 * 60 * 1000);
		}
		builder.append(
				" AND A.UPLOADED_DATE >= '" + uploadedFromDate + "' AND  A.UPLOADED_DATE <='" + uploadedToDate + "'");
		String sql = "SELECT A.CONTENT_ID,  A.SHORT_DESCRIPTION, A.LONG_DESCRIPTION, A.UPLOADED_DATE, A.DISPLAY_NAME, A.CATEGORY_ID, B.CATEGORY_NAME, A.GENRE_ID, C.GENRE_NAME, A.RATING, A.LANGUAGE, A.SEARCH, A.UPLOADED_BY FROM "
				+ tableName
				+ " A LEFT OUTER JOIN [CMS_Trial].[dbo].[ATOM_CATEGORY] B ON A.CATEGORY_ID=B.CATEGORY_ID LEFT OUTER JOIN [CMS_Trial].[dbo].[ATOM_GENRE] C ON A.GENRE_ID = C.GENRE_ID  WHERE A.CATEGORY_ID =  "
				+ category.getCategory_id() + builder.toString();
		System.out.println(sql);
		RowMapper<Content> contentMapper = new RowMapper<Content>() {

			@Override
			public Content mapRow(ResultSet rs, int arg1) throws SQLException {
				Content content = new Content();
				content.setContentId(rs.getInt("CONTENT_ID"));
				content.setShortDescription(rs.getString("SHORT_DESCRIPTION"));
				content.setLongDescription(rs.getString("LONG_DESCRIPTION"));
				content.setUploadedDate(rs.getDate("UPLOADED_DATE"));
				content.setDisplayName(rs.getString("DISPLAY_NAME"));
				CategoryBO catbo = new CategoryBO();
				catbo.setCategory_id(rs.getInt("CATEGORY_ID"));
				catbo.setCategory_name(rs.getString("CATEGORY_NAME"));
				content.setCategory(catbo);
				Genre genre = new Genre();
				genre.setGenreId(rs.getInt("GENRE_ID"));
				genre.setGenreName(rs.getString("GENRE_NAME"));
				content.setGenre(genre);
				content.setRating(rs.getInt("RATING"));
				content.setLanguage(rs.getString("LANGUAGE"));
				content.setSearch(rs.getString("SEARCH"));
				content.setUploadedBy(rs.getString("UPLOADED_BY"));
				return content;
			}
		};
		contents.addAll(jdbcTemplate.query(sql, contentMapper));
		return contents;
	}

	public Content getContentFormId(final int categoryId) {
		String tableName = properties.getProperty("contentTable");
		String sql = "SELECT A.CONTENT_ID, A.DISPLAY_NAME, A.TP_ID, D.TP_NAME,  C.GENRE_NAME, A.GENRE_ID, A.SUB_GENRE, A.LANGUAGE, A.RATING, A.SEARCH, A.SHORT_DESCRIPTION, A.LONG_DESCRIPTION, A.FILE_SIZE, A.SMARTURL1, A.SMARTURL2, A.DIRECTORS, A.PRODUCERS, A.MUSIC_DIRECTORS, A.ACTORS, A.ACTRESSES, A.SINGERS, A.CHOREOGRAPHER, A.SUPPORTING_STAR_CAST, A.LYRICIST, A.REVIEW, A.RELEASEDATE, A.PRODUCTION_COMPANIES, A.FILESIZE240, A.FILESIZE360, A.FILESIZE480, A.SMARTURL2SIZE240, A.SMARTURL2SIZE480, A.SMARTURL2SIZE360, A.SMARTURL2SIZE720, A.SMARTURL3, B.CATEGORY_NAME, A.CATEGORY_ID FROM "
				+ tableName
				+ " A LEFT OUTER JOIN [CMS_Trial].[dbo].[ATOM_CATEGORY] B ON A.CATEGORY_ID=B.CATEGORY_ID LEFT OUTER JOIN [CMS_Trial].[dbo].[ATOM_GENRE] C ON A.GENRE_ID = C.GENRE_ID LEFT OUTER JOIN [CMS_Trial].[dbo].[ATOM_TP] D ON A.TP_ID = D.TP_ID WHERE CONTENT_ID = ?";
		Object[] param = { categoryId };
		RowMapper<Content> contentMapper = new RowMapper<Content>() {

			@Override
			public Content mapRow(ResultSet rs, int arg1) throws SQLException {
				Content content = new Content();
				content.setDisplayName(rs.getString("DISPLAY_NAME"));
				content.setContentId(rs.getInt("CONTENT_ID"));
				TP tp = new TP();
				tp.setTpId(rs.getInt("TP_ID"));
				tp.setTpName("TP_NAME");
				content.setTp(tp);
				Genre genre = new Genre();
				genre.setGenreId(rs.getInt("GENRE_ID"));
				genre.setGenreName(rs.getString("GENRE_NAME"));
				content.setGenre(genre);
				content.setSubGenre(rs.getString("SUB_GENRE"));
				content.setLanguage(rs.getString("LANGUAGE"));
				content.setRating(rs.getInt("RATING"));
				content.setSearch(rs.getString("SEARCH"));
				content.setShortDescription(rs.getString("SHORT_DESCRIPTION"));
				content.setLongDescription(rs.getString("LONG_DESCRIPTION"));
				content.setFileSize(rs.getInt("FILE_SIZE"));
				content.setSmartUrl1(rs.getString("SMARTURL1"));
				content.setSmartUrl2(rs.getString("SMARTURL2"));
				content.setSmartUrl3(rs.getString("SMARTURL3"));
				content.setDirectors(rs.getString("DIRECTORS"));
				content.setProducers(rs.getString("PRODUCERS"));
				content.setMusicDirectors(rs.getString("MUSIC_DIRECTORS"));
				content.setActors(rs.getString("ACTORS"));
				content.setActresses(rs.getString("ACTRESSES"));
				content.setSingers(rs.getString("SINGERS"));
				content.setChoreographer(rs.getString("CHOREOGRAPHER"));
				content.setSupportingStarCast(rs.getString("SUPPORTING_STAR_CAST"));
				content.setLyricist(rs.getString("LYRICIST"));
				content.setReview(rs.getString("REVIEW"));
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Date rDate = rs.getDate("RELEASEDATE");
				if (rDate == null) {
					content.setrDate("");
				} else {
					content.setrDate(dateFormat.format(rDate));
				}

				content.setProductionCompanies(rs.getString("PRODUCTION_COMPANIES"));
				content.setFileSize240(rs.getFloat("FILESIZE240"));
				content.setFileSize360(rs.getFloat("FILESIZE360"));
				content.setFileSize480(rs.getFloat("FILESIZE480"));
				content.setSmartUrl2Size240(rs.getString("SMARTURL2SIZE240"));
				content.setSmartUrl2Size480(rs.getString("SMARTURL2SIZE480"));
				content.setSmartUrl2Size360(rs.getString("SMARTURL2SIZE360"));
				content.setSmartUrl2Size720(rs.getString("SMARTURL2SIZE720"));
				CategoryBO category = new CategoryBO();
				category.setCategory_id(rs.getInt("CATEGORY_ID"));
				category.setCategory_name(rs.getString("CATEGORY_NAME"));
				content.setCategory(category);
				return content;
			}
		};
		List<Content> contentList = new ArrayList<Content>();
		contentList.addAll(template.query(sql, param, contentMapper));
		return contentList.get(0);
	}

	public List<Genre> getGenreList(Integer categoryId) {
		List<Genre> genres = new ArrayList<Genre>();
		String sql = "SELECT GENRE_ID, GENRE_NAME, GENRE_ORDER_DISPLAY FROM ATOM_GENRE WHERE CATEGORY_ID = ? ORDER BY GENRE_NAME ASC";

		Object[] params = { categoryId };
		RowMapper<Genre> genreMapper = new RowMapper<Genre>() {

			@Override
			public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
				Genre genre = new Genre();
				genre.setGenreId(rs.getInt("GENRE_ID"));
				genre.setGenreName(rs.getString("GENRE_NAME"));
				genre.setGenreOrder(rs.getInt("GENRE_ORDER_DISPLAY"));
				return genre;
			}
		};
		genres.addAll(jdbcTemplate.query(sql, params, genreMapper));
		return genres;
	}

	public List<TP> getAllTpList() {
		List<TP> tpList = new ArrayList<TP>();
		String sql = "SELECT TP_ID, TP_NAME FROM ATOM_TP";
		RowMapper<TP> tpMapper = new RowMapper<TP>() {

			@Override
			public TP mapRow(ResultSet rs, int arg1) throws SQLException {
				TP tp = new TP();
				tp.setTpId(rs.getInt("TP_ID"));
				tp.setTpName(rs.getString("TP_NAME"));
				return tp;
			}
		};
		tpList.addAll(jdbcTemplate.query(sql, tpMapper));
		return tpList;
	}

	public List<String> getLanguages() {
		List<String> languages = new ArrayList<String>();
		String sql = "SELECT LANGUAGE FROM ATOM_LANGUAGE_MST";
		RowMapper<String> languageMapper = new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int arg1) throws SQLException {

				return rs.getString("LANGUAGE");
			}
		};
		languages.addAll(jdbcTemplate.query(sql, languageMapper));
		return languages;
	}

	public void updateContent(Content content) {
		String tableName = properties.getProperty("contentTable");
		String sql = "";

		if (content.getCategory().getCategory_id() == 1) {
			sql = "UPDATE " + tableName
					+ " SET DISPLAY_NAME =?, TP_ID = ?, GENRE_ID = ?, SUB_GENRE=?, LANGUAGE = ?, RATING = ?, SEARCH = ?, SHORT_DESCRIPTION = ?, LONG_DESCRIPTION = ?, FILE_SIZE = ?, SMARTURL1=?, SMARTURL2 = ?, DIRECTORS=?, PRODUCERS = ?, MUSIC_DIRECTORS = ?, ACTORS=?, ACTRESSES = ?, SINGERS=?, CHOREOGRAPHER = ?, SUPPORTING_STAR_CAST=?, LYRICIST = ?, REVIEW = ?, RELEASEDATE=?, PRODUCTION_COMPANIES=?, FILESIZE240 = ?, FILESIZE360 = ?, FILESIZE480=?, SMARTURL2SIZE240=?, SMARTURL2SIZE480=? ,SMARTURL2SIZE360=?, SMARTURL2SIZE720=?, SMARTURL3=? WHERE CONTENT_ID = ?";
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			java.util.Date date = null;
			try {
				date = dateFormat.parse(content.getrDate());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Object[] param = { content.getDisplayName(), content.getTp().getTpId(), content.getGenre().getGenreId(),
					content.getSubGenre(), content.getLanguage(), content.getRating(), content.getSearch(),
					content.getShortDescription(), content.getLongDescription(), content.getFileSize(),
					content.getSmartUrl1(), content.getSmartUrl2(), content.getDirectors(), content.getProducers(),
					content.getMusicDirectors(), content.getActors(), content.getActresses(), content.getSingers(),
					content.getChoreographer(), content.getSupportingStarCast(), content.getLyricist(),
					content.getReview(), new Date(date.getTime()), content.getProductionCompanies(),
					content.getFileSize240(), content.getFileSize360(), content.getFileSize480(),
					content.getSmartUrl2Size240(), content.getSmartUrl2Size360(), content.getSmartUrl2Size480(),
					content.getSmartUrl2Size720(), content.getSmartUrl3(), content.getContentId() };
			template.update(sql, param);
		} else {
			sql = "UPDATE " + tableName
					+ " SET DISPLAY_NAME =?, TP_ID = ?, GENRE_ID = ?, SUB_GENRE=?, LANGUAGE = ?, RATING = ?, SEARCH = ?, SHORT_DESCRIPTION = ?, LONG_DESCRIPTION = ?, FILE_SIZE = ?, SMARTURL1=?, SMARTURL2 = ?, FILESIZE240 = ?, FILESIZE360 = ?, FILESIZE480=?, SMARTURL2SIZE240=?, SMARTURL2SIZE480=? ,SMARTURL2SIZE360=?, SMARTURL2SIZE720=?, SMARTURL3=? WHERE CONTENT_ID = ?";
			Object[] param = { content.getDisplayName(), content.getTp().getTpId(), content.getGenre().getGenreId(),
					content.getSubGenre(), content.getLanguage(), content.getRating(), content.getSearch(),
					content.getShortDescription(), content.getLongDescription(), content.getFileSize(),
					content.getSmartUrl1(), content.getSmartUrl2(), content.getFileSize240(), content.getFileSize360(),
					content.getFileSize480(), content.getSmartUrl2Size240(), content.getSmartUrl2Size360(),
					content.getSmartUrl2Size480(), content.getSmartUrl2Size720(), content.getSmartUrl3(),
					content.getContentId() };
			template.update(sql, param);

		}
	}

}
