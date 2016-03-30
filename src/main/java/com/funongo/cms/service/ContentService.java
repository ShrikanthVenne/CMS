package com.funongo.cms.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.funongo.cms.bo.CategoryBO;
import com.funongo.cms.bo.Content;
import com.funongo.cms.bo.ContentBO;
import com.funongo.cms.bo.Genre;
import com.funongo.cms.bo.TP;
import com.funongo.cms.dao.ContentDao;

@Service
public class ContentService {

	@Autowired
	@Qualifier("portalDs")
	DataSource dataSource;

	@Autowired
	ContentDao contentDao;
	@Resource(name = "appProperties")
	Properties properties;

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public HashMap<String, HashSet<Integer>> getAllCategoryIds() throws RuntimeException {
		HashMap<String, HashSet<Integer>> ids = new HashMap<String, HashSet<Integer>>();
		HashSet<Integer> categoryIds = new HashSet<Integer>();
		HashSet<Integer> subCategoryIds = new HashSet<Integer>();
		HashSet<Integer> genreIds = new HashSet<Integer>();
		HashSet<Integer> tpIds = new HashSet<Integer>();
		LOGGER.setLevel(Level.INFO);
		String query = "Select a.CATEGORY_ID, b.SUBCATEGORY_ID, c.GENRE_ID, d.TP_ID from ATOM_CATEGORY a, ATOM_SUBCATEGORY b, ATOM_GENRE c, ATOM_TP d";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				categoryIds.add(rs.getInt("CATEGORY_ID"));
				subCategoryIds.add(rs.getInt("SUBCATEGORY_ID"));
				genreIds.add(rs.getInt("GENRE_ID"));
				tpIds.add(rs.getInt("TP_ID"));
			}

			ids.put("categoryIds", categoryIds);
			ids.put("subCategoryIds", subCategoryIds);
			ids.put("genreIds", genreIds);
			ids.put("tpIds", tpIds);
			System.out.println(categoryIds.size());
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			throw new RuntimeException();
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
				LOGGER.setLevel(Level.INFO);
				LOGGER.info(e.getMessage());
			}
		}
		return ids;
	}

	public Integer getMaxContentId() {
		String contentTable = properties.getProperty("contentTable");
		Integer id = 0;
		LOGGER.setLevel(Level.INFO);
		String query = "Select max(content_id) from " + contentTable;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			if (rs.next()) {
				id = rs.getInt(1);
			}
			System.out.println(id);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
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
				LOGGER.setLevel(Level.INFO);
				LOGGER.info(e.getMessage());
			}
		}
		return id;
	}

	public ArrayList<ContentBO> getCurrentlyUpdatedContent(Integer maxId, Integer count) {
		String contentTable = properties.getProperty("contentTable");
		String query = "select top " + count + " content_id, content_name, b.category_name " + " from " + contentTable
				+ " a " + " inner join ATOM_CATEGORY b" + " on a. CATEGORY_ID = B.CATEGORY_ID" + " where content_id > "
				+ maxId;
		System.out.println(query);
		ArrayList<ContentBO> contentList = new ArrayList<ContentBO>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				ContentBO contentBO = new ContentBO();
				CategoryBO category = new CategoryBO();
				category.setCategory_name(rs.getString("category_name"));
				contentBO.setContent_id(rs.getInt("content_id"));
				contentBO.setContent_name(rs.getString("content_name"));
				contentBO.setCategory(category);
				contentList.add(contentBO);
			}
			System.out.println("content size " + contentList.size());
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
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
				LOGGER.setLevel(Level.INFO);
				LOGGER.info(e.getMessage());
			}
		}

		return contentList;

	}

	public List<Integer> getAllTps() {
		List<Integer> tpIds = new ArrayList<Integer>();
		LOGGER.setLevel(Level.INFO);
		String query = "Select TP_ID from ATOM_TP";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				tpIds.add(rs.getInt(1));
			}
			System.out.println(tpIds.size());
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
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
				LOGGER.setLevel(Level.INFO);
				LOGGER.info(e.getMessage());
			}
		}
		return tpIds;
	}

	public List<CategoryBO> getCategoryList() {
		return contentDao.getCategoryList();
	}

	public List<Content> getContentList(CategoryBO category, String search, String fromDate, String toDate) {

		return contentDao.getContentList(category, search, fromDate, toDate);
	}

	public Content getContentFromId(int contentId) {
		return contentDao.getContentFormId(contentId);
	}

	public List<Genre> getGenreList(Integer categoryId) {
		return contentDao.getGenreList(categoryId);
	}

	public List<TP> getAllTpList() {

		return contentDao.getAllTpList();
	}

	public List<String> getLanguages() {
		return contentDao.getLanguages();
	}

	public void updateContent(Content content) {
		contentDao.updateContent(content);

	}

}
