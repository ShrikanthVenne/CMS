package com.funongo.cms.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCountCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.funongo.cms.bo.Content;
import com.sun.istack.internal.logging.Logger;

@Service
public class ExcelValidatorService {

	@Resource(name = "appProperties")
	Properties properties;

	@Autowired
	@Qualifier("portalDs")
	DataSource dataSource;

	@Autowired
	@Qualifier("portalDB")
	JdbcTemplate template;
	
	@Autowired
	ContentService contentService;

	public HashMap<String, ArrayList<String>> validateContent(Map<String, String> rowMap, HashSet<Integer> categories,
			HashSet<Integer> subCategories, HashSet<Integer> genres, HashSet<Integer> tps) {
		int category = 0;
		ArrayList<String> errors = new ArrayList<String>();
		String dateFormat = "yyyy-MM-dd HH:mm:ss";
		if (rowMap.get("CATEGORY_ID") != null) {
			try {
				category = (int) Double.parseDouble(rowMap.get("CATEGORY_ID"));
			} catch (NumberFormatException e) {
				errors.add("CATEGORY_ID should be a number");
			}

			if (!categories.contains(category)) {
				errors.add("CATEGORY_ID incorrect");
				// as category id is wrong we can't perform other checks, so
				// return
				HashMap<String, ArrayList<String>> error = new HashMap<String, ArrayList<String>>();
				error.put(rowMap.get("CONTENT_NAME"), errors);
				return error;
			}

			int subcategory_id = 0;
			if (rowMap.get("SUBCATEGORY_ID") != null) {
				boolean numberError = false;
				try {
					subcategory_id = (int) Double.parseDouble(rowMap.get("SUBCATEGORY_ID"));
				} catch (NumberFormatException e) {
					numberError = true;

				}
				if (!subCategories.contains(subcategory_id)) {
					if (numberError) {
						errors.add("SUBCATEGORY_ID should be a number");
					} else {
						errors.add("SUBCATEGORY_ID incorrect");
					}
				}
			}

			int genre = 0;
			if (rowMap.get("GENRE_ID") != null) {
				boolean numberError = false;
				try {
					genre = (int) Double.parseDouble(rowMap.get("GENRE_ID"));
				} catch (NumberFormatException e) {
					numberError = true;

				}
				if (!genres.contains(genre)) {
					if (numberError) {
						errors.add("GENRE_ID should be a number");
					} else {
						errors.add("GENRE_ID incorrect");
					}
				}
			} else {
				errors.add("GENRE_ID cannot be blank");
			}

			int tp = 0;
			if (rowMap.get("TP_ID") != null) {
				boolean numberError = false;
				try {
					tp = (int) Double.parseDouble(rowMap.get("TP_ID"));
				} catch (NumberFormatException e) {
					numberError = true;

				}
				if (!tps.contains(tp)) {
					if (numberError) {
						errors.add("TP_ID should be a number");
					} else {
						errors.add("TP_ID incorrect");
					}
				}
			} else {
				errors.add("TP_ID cannot be blank");
			}

			// validate content name. It's mandatory for all categories
			if (rowMap.get("CONTENT_NAME") != null) {
				String contentName = rowMap.get("CONTENT_NAME");
				// validate length of content
				if (contentName.length() > 42) {
					errors.add("CONTENT_NAME cannot be greater than 40 characters");
				}
			} else {
				errors.add("CONTENT_NAME cannot be blank");
			}

			// validate display name, It's mandatory for all categories
			if (rowMap.get("DISPLAY_NAME") != null) {
				String displayName = rowMap.get("DISPLAY_NAME");
				// validate length of content
				if (displayName.length() > 52) {
					errors.add("DISPLAY_NAME cannot be greater than 50 characters");
				}
			} else {
				errors.add("DISPLAY_NAME cannot be blank");
			}

			// validate cost, It's mandatory for all categories
			if (rowMap.get("COST") != null) {
				try {
					Double.parseDouble(rowMap.get("COST"));
				} catch (NumberFormatException e) {
					errors.add("COST should be a number");
				}
			} else {
				errors.add("COST cannot be blank");
			}

			// validate language, It's mandatory for all categories
			if (rowMap.get("LANGUAGE") != null) {
				String language = rowMap.get("LANGUAGE");
				if (language.length() > 52) {
					errors.add("LANGUAGE cannot be greater than 50 characters");
				}
			} else {
				errors.add("LANGUAGE cannot be blank");
			}

			// validate rating, It's mandatory for all categories
			if (rowMap.get("RATING") != null) {
				try {
					double rating = Double.parseDouble(rowMap.get("RATING"));
					if (rating < 0 || rating > 5) {
						errors.add("Rating should be in range 0-5");
					}
				} catch (NumberFormatException e) {
					errors.add("RATING should be a number");
				}
			} else {
				errors.add("RATING cannot be blank");
			}

			// validate search, It's mandatory for all categories
			if (rowMap.get("SEARCH") != null) {
				String search = rowMap.get("SEARCH");
				// validate length of content
				if (search.length() > 252) {
					errors.add("SEARCH cannot be greater than 250 characters");
				}
			} else {
				errors.add("SEARCH cannot be blank");
			}

			// validate short description, It's mandatory for all categories
			if (rowMap.get("SHORT_DESCRIPTION") != null) {
				String search = rowMap.get("SHORT_DESCRIPTION");
				// validate length of content
				if (search.length() > 252) {
					errors.add("SHORT_DESCRIPTION cannot be greater than 250 characters");
				}
			} else {
				errors.add("SHORT_DESCRIPTION cannot be blank");
			}

			// validate long description, It's mandatory for all categories
			if (rowMap.get("LONG_DESCRIPTION") == null) {
				errors.add("LONG_DESCRIPTION cannot be blank");
			}

			// validate file size, It's mandatory for all categories
			if (rowMap.get("FILE_SIZE") != null) {
				try {
					Double.parseDouble(rowMap.get("FILE_SIZE"));
				} catch (NumberFormatException e) {
					errors.add("FILE_SIZE should be a number");
				}
			} 

			// validate content production date, It's mandatory for all
			// categories
			if (rowMap.get("CONTENT_PRODUCTION_DATE") != null) {
				String dateToValidate = rowMap.get("CONTENT_PRODUCTION_DATE");
				if (!isThisDateValid(dateToValidate, dateFormat)) {
					errors.add("CONTENT_PRODUCTION_DATE is not valid");
				}
			}

			// validate short description, It's mandatory for all categories
			if (rowMap.get("AGE_GROUP") != null) {
				String ageGroup = rowMap.get("AGE_GROUP");
				// validate length of age group
				if (ageGroup.length() > 32) {
					errors.add("AGE_GROUP cannot be greater than 30 characters");
				}
			} else {
				errors.add("AGE_GROUP cannot be blank");
			}

			// validate valid from date, It's mandatory for all categories
			if (rowMap.get("VALIDFROM") != null) {
				String dateToValidate = rowMap.get("VALIDFROM");
				if (!isThisDateValid(dateToValidate, dateFormat)) {
					errors.add("VALIDFROM is not valid");
				}
			} else {
				errors.add("VALIDFROM cannot be blank");
			}

			// validate valid to date, It's mandatory for all categories
			if (rowMap.get("VALIDTO") != null) {
				String dateToValidate = rowMap.get("VALIDTO");
				if (!isThisDateValid(dateToValidate, dateFormat)) {
					errors.add("VALIDTO is not valid");
				}
			} else {
				errors.add("VALIDTO cannot be blank");
			}						
			
			
			// validate smart urls, It's mandatory for videos, movies and short films
			String smartUrl1 = rowMap.get("SMARTURL1");
			String smartUrl2 = rowMap.get("SMARTURL2");
			String smartUrl3 = rowMap.get("SMARTURL3");
			
			if((category == new Integer(properties.getProperty("videosId")) || 
				category == new Integer(properties.getProperty("moviesId")) || 
				category == new Integer(properties.getProperty("shortFilmId")))
					&& 
				smartUrl1 == null && smartUrl2 == null && smartUrl3 == null){
				errors.add("SmartUrl cannot be blank");
			}
			
			
			/*// Make a binary String for smartUrlProvider
			String smartUrlProviderString = "";
			
			smartUrlProviderString = smartUrl3 == null ? (smartUrlProviderString + 0) : (smartUrlProviderString + 1);
			
			smartUrlProviderString = smartUrl2 == null ? (smartUrlProviderString + 0) : (smartUrlProviderString + 1);
			
			smartUrlProviderString = smartUrl1 == null ? (smartUrlProviderString + 0) : (smartUrlProviderString + 1);
			
			System.out.println("smartUrlProviderString "+smartUrlProviderString);
			*/
			// Convert String to Integer			
			int smartUrlProvider = contentService.getSmartUrlProvider(smartUrl1, smartUrl2, smartUrl3);
			System.out.println("provider "+smartUrlProvider);
			// Since SmartUrlProvide is not part of excel, add to the map
			rowMap.put("SMARTURLPROVIDER", smartUrlProvider+"");
			

			// validate directors, It's mandatory for movies
			if (rowMap.get("DIRECTORS") != null) {
				String directors = rowMap.get("DIRECTORS");
				// validate length of content
				if (directors.length() > 102) {
					errors.add("DIRECTORS cannot be greater than 100 characters");
				}
			} else if (category == new Integer(properties.getProperty("moviesId"))) {
				errors.add("DIRECTORS cannot be blank");
			}

			// validate producers, It's mandatory for movies
			if (rowMap.get("PRODUCERS") != null) {
				String producers = rowMap.get("PRODUCERS");
				// validate length of content
				if (producers.length() > 102) {
					errors.add("PRODUCERS cannot be greater than 100 characters");
				}
			} else if (category == new Integer(properties.getProperty("moviesId"))) {
				errors.add("PRODUCERS cannot be blank");
			}

			// validate music directors, It's mandatory for movies
			if (rowMap.get("MUSIC_DIRECTORS") != null) {
				String musicDirectors = rowMap.get("MUSIC_DIRECTORS");
				// validate length of content
				if (musicDirectors.length() > 102) {
					errors.add("MUSIC_DIRECTORS cannot be greater than 100 characters");
				}
			} else if (category == new Integer(properties.getProperty("moviesId"))) {
				errors.add("MUSIC_DIRECTORS cannot be blank");
			}

			// validate actors, It's mandatory for movies
			if (rowMap.get("ACTORS") != null) {
				String actors = rowMap.get("ACTORS");
				// validate length of content
				if (actors.length() > 102) {
					errors.add("ACTORS cannot be greater than 100 characters");
				}
			} else if (category == new Integer(properties.getProperty("moviesId"))) {
				errors.add("ACTORS cannot be blank");
			}

			// validate actresses, It's mandatory for movies
			if (rowMap.get("ACTRESSES") != null) {
				String actresses = rowMap.get("ACTRESSES");
				// validate length of content
				if (actresses.length() > 202) {
					errors.add("ACTRESSES cannot be greater than 200 characters");
				}
			} else if (category == new Integer(properties.getProperty("moviesId"))) {
				errors.add("ACTRESSES cannot be blank");
			}

			// validate singers, It's mandatory for movies
			if (rowMap.get("SINGERS") != null) {
				String singers = rowMap.get("SINGERS");
				// validate length of content
				if (singers.length() > 302) {
					errors.add("SINGERS cannot be greater than 300 characters");
				}
			} else if (category == new Integer(properties.getProperty("moviesId"))) {
				errors.add("SINGERS cannot be blank");
			}

			// validate choreographer, It's mandatory for movies
			if (rowMap.get("CHOREOGRAPHER") != null) {
				String choreographer = rowMap.get("CHOREOGRAPHER");
				// validate length of content
				if (choreographer.length() > 202) {
					errors.add("CHOREOGRAPHER cannot be greater than 200 characters");
				}
			} else if (category == new Integer(properties.getProperty("moviesId"))) {
				errors.add("CHOREOGRAPHER cannot be blank");
			}

			// validate supporting star cast, It's mandatory for movies
			if (rowMap.get("SUPPORTING_STAR_CAST") != null) {
				String supportingStarCast = rowMap.get("SUPPORTING_STAR_CAST");
				// validate length of content
				if (supportingStarCast.length() > 502) {
					errors.add("SUPPORTING_STAR_CAST cannot be greater than 500 characters");
				}
			} else if (category == new Integer(properties.getProperty("moviesId"))) {
				errors.add("SUPPORTING_STAR_CAST cannot be blank");
			}

			// validate lyricist, It's mandatory for movies
			if (rowMap.get("LYRICIST") != null) {
				String lyricist = rowMap.get("LYRICIST");
				// validate length of content
				if (lyricist.length() > 102) {
					errors.add("LYRICIST cannot be greater than 100 characters");
				}
			} else if (category == new Integer(properties.getProperty("moviesId"))) {
				errors.add("LYRICIST cannot be blank");
			}

			// validate release date, It's mandatory for all categories
			if (rowMap.get("RELEASEDATE") != null) {
				String dateToValidate = rowMap.get("RELEASEDATE");
				if (!isThisDateValid(dateToValidate, dateFormat)) {
					errors.add("RELEASEDATE is not valid");
				}
			} else if (category == new Integer(properties.getProperty("moviesId"))) {
				errors.add("RELEASEDATE cannot be blank");
			}

			// validate production companies, It's mandatory for movies
			if (rowMap.get("PRODUCTION_COMPANIES") != null) {
				String lyricist = rowMap.get("PRODUCTION_COMPANIES");
				// validate length of production companies
				if (lyricist.length() > 202) {
					errors.add("PRODUCTION_COMPANIES cannot be greater than 200 characters");
				}
			} else if (category == new Integer(properties.getProperty("moviesId"))) {
				errors.add("PRODUCTION_COMPANIES cannot be blank");
			}

			// validate image preview1, It's mandatory all categories
			if (rowMap.get("IMAGE_PREVIEW1") != null) {
				String imagePreview = rowMap.get("IMAGE_PREVIEW1");
				System.out.println("img prev... " + imagePreview);
				if (!(imagePreview.equals("0.0") || imagePreview.equals("1.0"))) {
					errors.add("IMAGE_PREVIEW1 is not valid");
				}
			} else {
				errors.add("IMAGE_PREVIEW1 cannot be blank");
			}

			// validate image preview2, It's mandatory all categories
			if (rowMap.get("IMAGE_PREVIEW2") != null) {
				String imagePreview = rowMap.get("IMAGE_PREVIEW2");

				if (!(imagePreview.equals("0.0") || imagePreview.equals("1.0"))) {
					errors.add("IMAGE_PREVIEW2 is not valid");
				}
			} else {
				errors.add("IMAGE_PREVIEW2 cannot be blank");
			}

			// validate image preview3, It's mandatory all categories
			if (rowMap.get("IMAGE_PREVIEW3") != null) {
				String imagePreview = rowMap.get("IMAGE_PREVIEW3");

				if (!(imagePreview.equals("0.0") || imagePreview.equals("1.0"))) {
					errors.add("IMAGE_PREVIEW3 is not valid");
				}
			} else {
				errors.add("IMAGE_PREVIEW3 cannot be blank");
			}

			// validate image preview4, It's mandatory all categories
			if (rowMap.get("IMAGE_PREVIEW4") != null) {
				String imagePreview = rowMap.get("IMAGE_PREVIEW4");

				if (!(imagePreview.equals("0.0") || imagePreview.equals("1.0"))) {
					errors.add("IMAGE_PREVIEW4 is not valid");
				}
			} else {
				errors.add("IMAGE_PREVIEW4 cannot be blank");
			}

			// validate image preview5, It's mandatory all categories
			if (rowMap.get("IMAGE_PREVIEW5") != null) {
				String imagePreview = rowMap.get("IMAGE_PREVIEW5");

				if (!(imagePreview.equals("0.0") || imagePreview.equals("1.0"))) {
					errors.add("IMAGE_PREVIEW5 is not valid");
				}
			} else {
				errors.add("IMAGE_PREVIEW5 cannot be blank");
			}

			// validate poster url1, It's mandatory all categories
			if (rowMap.get("POSTERURL1") != null) {
				String posterUrl = rowMap.get("POSTERURL1");

				if (!(posterUrl.equals("0.0") || posterUrl.equals("1.0"))) {
					errors.add("POSTERURL1 is not valid");
				}
			} else {
				errors.add("POSTERURL1 cannot be blank");
			}

			// validate poster url2, It's mandatory all categories
			if (rowMap.get("POSTERURL2") != null) {
				String posterUrl = rowMap.get("POSTERURL2");

				if (!(posterUrl.equals("0.0") || posterUrl.equals("1.0"))) {
					errors.add("POSTERURL2 is not valid");
				}
			} else {
				errors.add("POSTERURL2 cannot be blank");
			}

			// validate poster url3, It's mandatory all categories
			if (rowMap.get("POSTERURL3") != null) {
				String posterUrl = rowMap.get("POSTERURL3");

				if (!(posterUrl.equals("0.0") || posterUrl.equals("1.0"))) {
					errors.add("POSTERURL3 is not valid");
				}
			} else {
				errors.add("POSTERURL3 cannot be blank");
			}

			// validate poster url4, It's mandatory all categories
			if (rowMap.get("POSTERURL4") != null) {
				String posterUrl = rowMap.get("POSTERURL4");

				if (!(posterUrl.equals("0.0") || posterUrl.equals("1.0"))) {
					errors.add("POSTERURL4 is not valid");
				}
			} else {
				errors.add("POSTERURL4 cannot be blank");
			}

			// validate poster url5, It's mandatory all categories
			if (rowMap.get("POSTERURL5") != null) {
				String posterUrl = rowMap.get("POSTERURL5");

				if (!(posterUrl.equals("0.0") || posterUrl.equals("1.0"))) {
					errors.add("POSTERURL5 is not valid");
				}
			} else {
				errors.add("POSTERURL5 cannot be blank");
			}

			// validate poster url6, It's mandatory all categories
			if (rowMap.get("POSTERURL6") != null) {
				String posterUrl = rowMap.get("POSTERURL6");

				if (!(posterUrl.equals("0.0") || posterUrl.equals("1.0"))) {
					errors.add("POSTERURL6 is not valid");
				}
			} else {
				errors.add("POSTERURL6 cannot be blank");
			}

			// validate duration, It's mandatory for videos and movies
			if (rowMap.get("DURATION") != null) {
				try {
					Double.parseDouble(rowMap.get("DURATION"));
				} catch (NumberFormatException e) {
					errors.add("DURATION should be a number");
				}
			} else if (category == new Integer(properties.getProperty("moviesId"))
					|| category == new Integer(properties.getProperty("videosId"))) {
				errors.add("DURATION cannot be blank");
			}

			// validate grade, It's mandatory for movies
			if (rowMap.get("GRADE") != null) {
				String grade = rowMap.get("GRADE");
				// validate length of production companies
				if (grade.length() > 12) {
					errors.add("GRADE cannot be greater than 10 characters");
				}
			} else if (category == new Integer(properties.getProperty("moviesId"))) {
				errors.add("GRADE cannot be blank");
			}

			// validate c lang, It's mandatory for all categories
			if (rowMap.get("C_LANG") != null) {
				String cLang = rowMap.get("C_LANG");
				// validate length of production companies
				if (cLang.length() > 52) {
					errors.add("C_LANG cannot be greater than 50 characters");
				}
			} else {
				errors.add("C_LANG cannot be blank");
			}

			// validate wrapping partner. It's not mandatoty
			if (rowMap.get("WRAPPING_PARTNER") != null) {
				String contentName = rowMap.get("WRAPPING_PARTNER");
				// validate length of content
				if (contentName.length() > 52) {
					errors.add("WRAPPING_PARTNER cannot be greater than 50 characters");
				}
			}

			// validate sub genre id. It's not mandatory
			if (rowMap.get("SUB_GENRE") != null) {
				String subGenre = rowMap.get("SUB_GENRE");
				List<String> subGenreList = new ArrayList<String>();
				if (subGenre.contains("#")) {
					String subGenreIds[] = subGenre.split("#");
					subGenreList = Arrays.asList(subGenreIds);
				} else {
					System.out.println("sub genre else..");
					System.out.println(subGenre);
					subGenreList.add(subGenre);
				}
				for (String currentSubGenre : subGenreList) {
					boolean numberError = false;
					int genreId = 0;
					try {
						genreId = (int) Double.parseDouble(currentSubGenre);
						System.out.println(genreId);
					} catch (NumberFormatException e) {
						e.printStackTrace();
						numberError = true;

					}
					System.out.println("genre condition " + genres.contains(genreId));
					System.out.println("gen id" + genreId);
					if (!genres.contains(genreId)) {
						if (numberError) {
							errors.add("SUB_GENRE should be a number");
						} else {
							errors.add("SUB_GENRE incorrect");
						}
					}
				}
			}

			// validate content type, It's not mandatory
			if (rowMap.get("CONTENT_TYPE") != null) {
				String contentType = rowMap.get("CONTENT_TYPE");
				// validate length of content
				if (contentType.length() > 22) {
					errors.add("CONTENT_TYPE cannot be greater than 20 characters");
				}
			}

			// validate image preview, It's not mandatory
			if (rowMap.get("IMAGE_PREVIEW") != null) {
				String imagePreview = rowMap.get("IMAGE_PREVIEW");

				if (!(imagePreview.equals("0.0") || imagePreview.equals("1.0"))) {
					errors.add("IMAGE_PREVIEW is not valid");
				}
			}

			// validate audio preview, It's not mandatory
			if (rowMap.get("AUDIO_PREVIEW") != null) {
				String imagePreview = rowMap.get("AUDIO_PREVIEW");

				if (!(imagePreview.equals("0.0") || imagePreview.equals("1.0"))) {
					errors.add("AUDIO_PREVIEW is not valid");
				}
			}

			// validate video preview, It's not mandatory
			if (rowMap.get("VIDEO_PREVIEW") != null) {
				String imagePreview = rowMap.get("VIDEO_PREVIEW");

				if (!(imagePreview.equals("0.0") || imagePreview.equals("1.0"))) {
					errors.add("VIDEO_PREVIEW is not valid");
				}
			}

			// validate preview file name, It's not mandatory
			if (rowMap.get("PREVIEW_FILE_NAME") != null) {
				String search = rowMap.get("PREVIEW_FILE_NAME");
				// validate length of content
				if (search.length() > 52) {
					errors.add("PREVIEW_FILE_NAME cannot be greater than 50 characters");
				}
			}

			// validate content version, It's not mandatory
			if (rowMap.get("CONTENT_VERSION") != null) {
				String contentVersion = rowMap.get("CONTENT_VERSION");
				// validate length of content
				if (contentVersion.length() > 22) {
					errors.add("CONTENT_VERSION cannot be greater than 20 characters");
				}
			}

			// validate review, It's not mandatory
			if (rowMap.get("REVIEW") != null) {
				String review = rowMap.get("REVIEW");
				// validate length of content
				if (review.length() > 302) {
					errors.add("REVIEW cannot be greater than 300 characters");
				}
			}
			
			// validate SMARTURL2SIZE480, It's mandatory for Touchfone/smarturl2
			if (rowMap.get("SMARTURL2SIZE480") != null) {
				String smartUrlSize = rowMap.get("SMARTURL2SIZE480");
				// validate length of smartUrlSize
				if (smartUrlSize.length() > 202) {
					errors.add("SMARTURL2SIZE480 cannot be greater than 300 characters");
				}
			}
			else if(smartUrl2 != null){
				errors.add("SMARTURL2SIZE480 cannot be blank");
			}
			
			// validate SMARTURL2SIZE360, It's mandatory for Touchfone/smarturl2
			if (rowMap.get("SMARTURL2SIZE360") != null) {
				String smartUrlSize = rowMap.get("SMARTURL2SIZE360");
				// validate length of smartUrlSize
				if (smartUrlSize.length() > 202) {
					errors.add("SMARTURL2SIZE360 cannot be greater than 300 characters");
				}
			}
			else if(smartUrl2 != null){
				errors.add("SMARTURL2SIZE360 cannot be blank");
			}
			
			
			// validate SMARTURL2SIZE240, It's mandatory for Touchfone/smarturl2
			if (rowMap.get("SMARTURL2SIZE240") != null) {
				String smartUrlSize = rowMap.get("SMARTURL2SIZE240");
				// validate length of smartUrlSize
				if (smartUrlSize.length() > 202) {
					errors.add("SMARTURL2SIZE240 cannot be greater than 300 characters");
				}
			}
			else if(smartUrl2 != null){
				errors.add("SMARTURL2SIZE240 cannot be blank");
			}
			
			
			// validate SMARTURL2SIZE720, It's mandatory for Touchfone/smarturl2
			if (rowMap.get("SMARTURL2SIZE720") != null) {
				String smartUrlSize = rowMap.get("SMARTURL2SIZE720");
				// validate length of smartUrlSize
				if (smartUrlSize.length() > 202) {
					errors.add("SMARTURL2SIZE720 cannot be greater than 300 characters");
				}
			}
			else if(smartUrl2 != null){
				errors.add("SMARTURL2SIZE720 cannot be blank");
			}
			
			// validate FILESIZE480, It's mandatory for all categories
			if (rowMap.get("FILESIZE480") != null) {
				try {
					Double.parseDouble(rowMap.get("FILESIZE480"));
				} catch (NumberFormatException e) {
					errors.add("FILESIZE480 should be a number");
				}
			} 
			else if(smartUrl2 != null) {
				errors.add("FILESIZE480 cannot be blank");
			}
			
			// validate FILESIZE360, It's mandatory for all categories
			if (rowMap.get("FILESIZE360") != null) {
				try {
					Double.parseDouble(rowMap.get("FILESIZE360"));
				} catch (NumberFormatException e) {
					errors.add("FILESIZE360 should be a number");
				}
			} 
			else if(smartUrl2 != null) {
				errors.add("FILESIZE360 cannot be blank");
			}
			
			// validate FILESIZE240, It's mandatory for all categories
			if (rowMap.get("FILESIZE240") != null) {
				try {
					Double.parseDouble(rowMap.get("FILESIZE240"));
				} catch (NumberFormatException e) {
					errors.add("FILESIZE240 should be a number");
				}
			} 
			else if(smartUrl2 != null) {
				errors.add("FILESIZE240 cannot be blank");
			}
			

		} else {
			errors.add("CATEGORY_ID cannot be blank");
		}
		HashMap<String, ArrayList<String>> error = new HashMap<String, ArrayList<String>>();
		if (errors.size() > 0) {
			String contentName = rowMap.get("CONTENT_NAME");
			if (contentName != null) {
				error.put(contentName.replace("'", ""), errors);
			} else {
				error.put(contentName, errors);
			}
		}
		return error;
	}

	public boolean isThisDateValid(String dateToValidate, String dateFormat) {

		if (dateToValidate == null) {
			return false;
		}

		// String dateString = dateToValidate;
		dateToValidate = dateToValidate.replace("'", "");

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setLenient(false);

		try {

			// if not valid, it will throw ParseException
			Date date = sdf.parse(dateToValidate);
			System.out.println("DDDDDDDDD " + date);

		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public HashMap<String, ArrayList<String>> validateLanguageContent(Map<String, String> rowMap, Content content) {
		ArrayList<String> errors = new ArrayList<String>();
		Content existingContent = new Content();
		int categoryId = 0;
		String contentName = null;
		String language = null;
		if (rowMap.get("CONTENT_NAME") != null) {
			contentName = rowMap.get("CONTENT_NAME").replace("'", "");
			existingContent = getContent(contentName);

			// validate length of content
			if (existingContent != null) {
				content.setContentName(existingContent.getContentName());
				content.setContentId(existingContent.getContentId());
				content.setCategoryId(existingContent.getCategoryId());
			} else {
				errors.add("CONTENT_NAME does not exist");
			}
		} else {
			errors.add("CONTENT_NAME cannot be blank");
		}

		if (rowMap.get("DISPLAY_NAME") != null) {
			String displayName = rowMap.get("DISPLAY_NAME").replace("'", "");
			content.setDisplayName(displayName);
		} else {
			errors.add("DISPLAY_NAME cannot be blank");
		}

		if (rowMap.get("LANGUAGE") != null) {
			language = rowMap.get("LANGUAGE").replace("'", "");
			content.setLanguage(language);
		} else {
			errors.add("LANGUAGE cannot be blank");
		}
		if (language != null) {
			boolean exist = isContentExist(contentName, language);
			if (exist) {
				errors.add("Content Already Exists");
			}
		} else {
			errors.add("LANGUAGE cannot be blank");
		}
		if (rowMap.get("SEARCH") != null) {
			String search = rowMap.get("SEARCH").replace("'", "");
			content.setSearch(search);
		} else {
			errors.add("SEARCH cannot be blank");
		}

		if (rowMap.get("SHORT_DESCRIPTION") != null) {
			String shortDescription = rowMap.get("SHORT_DESCRIPTION").replace("'", "");
			content.setShortDescription(shortDescription);
		} else {
			errors.add("SHORT_DESCRIPTION cannot be blank");
		}
		if (rowMap.get("LONG_DESCRIPTION") != null) {
			String longDescription = rowMap.get("LONG_DESCRIPTION").replace("'", "");
			content.setLongDescription(longDescription);
		} else {
			errors.add("LONG_DESCRIPTION cannot be blank");
		}

		if (rowMap.get("DIRECTORS") != null) {
			String directors = rowMap.get("DIRECTORS").replace("'", "");
			content.setDirectors(directors);
		} else if (existingContent != null && existingContent.getDirectors() != null) {
			errors.add("DIRECTORS cannot be blank");
		}

		// validate producers, It's mandatory for movies
		if (rowMap.get("PRODUCERS") != null) {
			String producers = rowMap.get("PRODUCERS").replace("'", "");
			content.setProducers(producers);
		} else if (existingContent != null && existingContent.getProducers() != null) {
			errors.add("PRODUCERS cannot be blank");
		}

		// validate music directors, It's mandatory for movies
		if (rowMap.get("MUSIC_DIRECTORS") != null) {
			String musicDirectors = rowMap.get("MUSIC_DIRECTORS").replace("'", "");
			content.setMusicDirectors(musicDirectors);
		} else if (existingContent != null && existingContent.getMusicDirectors() != null) {
			errors.add("MUSIC_DIRECTORS cannot be blank");
		}

		// validate actors, It's mandatory for movies
		if (rowMap.get("ACTORS") != null) {
			String actors = rowMap.get("ACTORS").replace("'", "");
			content.setActors(actors);
		} else if (existingContent != null && existingContent.getActors() != null) {
			errors.add("ACTORS cannot be blank");
		}

		// validate actresses, It's mandatory for movies
		if (rowMap.get("ACTRESSES") != null) {
			String actresses = rowMap.get("ACTRESSES").replace("'", "");
			content.setActresses(actresses);
		} else if (existingContent != null && existingContent.getActresses() != null) {
			errors.add("ACTRESSES cannot be blank");
		}

		// validate singers, It's mandatory for movies
		if (rowMap.get("SINGERS") != null) {
			String singers = rowMap.get("SINGERS").replace("'", "");
			content.setSingers(singers);
		} else if (existingContent != null && existingContent.getSingers() != null) {
			errors.add("SINGERS cannot be blank");
		}

		// validate choreographer, It's mandatory for movies
		if (rowMap.get("CHOREOGRAPHER") != null) {
			String choreographer = rowMap.get("CHOREOGRAPHER").replace("'", "");
			content.setChoreographer(choreographer);
		} else if (existingContent != null && existingContent.getChoreographer() != null) {
			errors.add("CHOREOGRAPHER cannot be blank");
		}

		// validate supporting star cast, It's mandatory for movies
		if (rowMap.get("SUPPORTING_STAR_CAST") != null) {
			String supportingStarCast = rowMap.get("SUPPORTING_STAR_CAST").replace("'", "");
			content.setSupportingStarCast(supportingStarCast);
		} else if (existingContent != null && existingContent.getSupportingStarCast() != null) {
			errors.add("SUPPORTING_STAR_CAST cannot be blank");
		}

		// validate lyricist, It's mandatory for movies
		if (rowMap.get("LYRICIST") != null) {
			String lyricist = rowMap.get("LYRICIST").replace("'", "");
			content.setLyricist(lyricist);
		} else if (existingContent != null && existingContent.getLyricist() != null) {
			errors.add("LYRICIST cannot be blank");
		}
		// validate production companies, It's mandatory for movies
		if (rowMap.get("PRODUCTION_COMPANIES") != null) {
			String productionCompanies = rowMap.get("PRODUCTION_COMPANIES").replace("'", "");
			content.setProductionCompanies(productionCompanies);
		} else if (existingContent != null && existingContent.getProductionCompanies() != null) {
			errors.add("PRODUCTION_COMPANIES cannot be blank");
		}
		// validate review, It's not mandatory
		if (rowMap.get("REVIEW") != null) {
			String review = rowMap.get("REVIEW").replace("'", "");
			content.setReview(review);
		} else if (existingContent != null && existingContent.getReview() != null) {
			errors.add("REVIEW cannot be blank");
		}

		content.setCost(existingContent.getCost());
		content.setRating(existingContent.getRating());
		content.setStatus(existingContent.getStatus());
		content.setFileSize(existingContent.getFileSize());
		content.setContentProductionDate(existingContent.getContentProductionDate());
		content.setUploadedBy("admin");
		content.setValidFrom(existingContent.getValidFrom());
		content.setValidTo(existingContent.getValidTo());
		content.setApprovedDate(existingContent.getApprovedDate());
		content.setImagePreview(existingContent.getImagePreview());
		content.setAudioPreview(existingContent.getAudioPreview());
		content.setVideoPreview(existingContent.getVideoPreview());
		content.setPreviewFileName(existingContent.getPreviewFileName());
		content.setContentVersion(existingContent.getContentVersion());
		content.setReleaseDate(existingContent.getReleaseDate());
		content.setImagePreview1(existingContent.getImagePreview1());
		content.setImagePreview2(existingContent.getImagePreview2());
		content.setImagePreview3(existingContent.getImagePreview3());
		content.setImagePreview4(existingContent.getImagePreview4());
		content.setImagePreview5(existingContent.getImagePreview5());
		content.setPosterUrl1(existingContent.getPosterUrl1());
		content.setPosterUrl2(existingContent.getPosterUrl2());
		content.setPosterUrl3(existingContent.getPosterUrl3());
		content.setPosterUrl4(existingContent.getPosterUrl4());
		content.setPosterUrl5(existingContent.getPosterUrl5());
		content.setPosterUrl6(existingContent.getPosterUrl6());

		HashMap<String, ArrayList<String>> error = new HashMap<String, ArrayList<String>>();
		if (errors.size() > 0) {
			if (contentName != null) {
				error.put(contentName.replace("'", ""), errors);
			} else {
				error.put(contentName, errors);
			}
		}
		return error;
	}

	private boolean isContentExist(String contentName, String language) {
		boolean isExist = false;
		int val = 0;
		String tableName = properties.getProperty("contentLangTable");
		String sql = "SELECT * FROM " + tableName + " WHERE CONTENT_NAME = ?  AND METALANG = ?";
		System.out.println(sql + " " + contentName + " " + language);
		Object[] params = { contentName, language };
		RowCountCallbackHandler rowCountHandler = new RowCountCallbackHandler();
		template.query(sql, params, rowCountHandler);
		val = rowCountHandler.getRowCount();
		if (val > 0)
			isExist = true;
		return isExist;
	}

	private Content getContent(String contentName) {
		Content content = new Content();
		String query = "SELECT CONTENT_ID, CONTENT_NAME, DISPLAY_NAME, COST, LANGUAGE, RATING, SEARCH, SHORT_DESCRIPTION, LONG_DESCRIPTION, STATUS, FILE_SIZE, CONTENT_PRODUCTION_DATE, UPLOADED_BY,VALIDFROM, VALIDTO, APPROVED_DATE, IMAGE_PREVIEW, AUDIO_PREVIEW, VIDEO_PREVIEW, PREVIEW_FILE_NAME, CONTENT_VERSION, DIRECTORS, PRODUCERS, MUSIC_DIRECTORS, ACTORS, ACTRESSES, SINGERS, CHOREOGRAPHER, SUPPORTING_STAR_CAST, LYRICIST, RELEASEDATE, PRODUCTION_COMPANIES, REVIEW, IMAGE_PREVIEW1, IMAGE_PREVIEW2, IMAGE_PREVIEW3, IMAGE_PREVIEW4, IMAGE_PREVIEW5, POSTERURL1, POSTERURL2, POSTERURL3, POSTERURL4, POSTERURL5, POSTERURL6, CATEGORY_ID FROM ATOM_CONTENT WHERE CONTENT_NAME = ?";
		Object[] params = { contentName };
		RowMapper<Content> contentMapper = new RowMapper<Content>() {

			@Override
			public Content mapRow(ResultSet rs, int rowNum) throws SQLException {
				Content content = new Content();
				content.setContentId(rs.getInt("CONTENT_ID"));
				content.setContentName(rs.getString("CONTENT_NAME"));
				content.setDisplayName(rs.getString("DISPLAY_NAME"));
				content.setCost(rs.getDouble("COST"));
				content.setLanguage(rs.getString("LANGUAGE"));
				content.setRating(rs.getInt("RATING"));
				content.setSearch(rs.getString("SEARCH"));
				content.setShortDescription(rs.getString("SHORT_DESCRIPTION"));
				content.setLongDescription(rs.getString("LONG_DESCRIPTION"));
				content.setStatus(rs.getString("STATUS"));
				content.setFileSize(rs.getFloat("FILE_SIZE"));
				content.setContentProductionDate(rs.getDate("CONTENT_PRODUCTION_DATE"));
				content.setUploadedBy(rs.getString("UPLOADED_BY"));
				content.setValidFrom(rs.getDate("VALIDFROM"));
				content.setValidTo(rs.getDate("VALIDTO"));
				content.setApprovedDate(rs.getDate("APPROVED_DATE"));
				content.setImagePreview(rs.getInt("IMAGE_PREVIEW"));
				content.setAudioPreview(rs.getInt("AUDIO_PREVIEW"));
				content.setVideoPreview(rs.getInt("VIDEO_PREVIEW"));
				content.setPreviewFileName(rs.getString("PREVIEW_FILE_NAME"));
				content.setContentVersion(rs.getString("CONTENT_VERSION"));
				content.setDirectors(rs.getString("DIRECTORS"));
				content.setProducers(rs.getString("PRODUCERS"));
				content.setMusicDirectors(rs.getString("MUSIC_DIRECTORS"));
				content.setActors(rs.getString("ACTORS"));
				content.setActresses(rs.getString("ACTRESSES"));
				content.setSingers(rs.getString("SINGERS"));
				content.setChoreographer(rs.getString("CHOREOGRAPHER"));
				content.setSupportingStarCast(rs.getString("SUPPORTING_STAR_CAST"));
				content.setLyricist(rs.getString("LYRICIST"));
				content.setReleaseDate(rs.getDate("RELEASEDATE"));
				content.setProductionCompanies(rs.getString("PRODUCTION_COMPANIES"));
				content.setReview(rs.getString("REVIEW"));
				content.setImagePreview1(rs.getInt("IMAGE_PREVIEW1"));
				content.setImagePreview2(rs.getInt("IMAGE_PREVIEW2"));
				content.setImagePreview3(rs.getInt("IMAGE_PREVIEW3"));
				content.setImagePreview4(rs.getInt("IMAGE_PREVIEW4"));
				content.setImagePreview5(rs.getInt("IMAGE_PREVIEW5"));
				content.setPosterUrl1(rs.getInt("POSTERURL1"));
				content.setPosterUrl2(rs.getInt("POSTERURL2"));
				content.setPosterUrl3(rs.getInt("POSTERURL3"));
				content.setPosterUrl4(rs.getInt("POSTERURL4"));
				content.setPosterUrl5(rs.getInt("POSTERURL5"));
				content.setPosterUrl6(rs.getInt("POSTERURL6"));
				content.setCategoryId(rs.getInt("CATEGORY_ID"));
				return content;
			}
		};
		content = template.queryForObject(query, params, contentMapper);
		return content;
	}

}
