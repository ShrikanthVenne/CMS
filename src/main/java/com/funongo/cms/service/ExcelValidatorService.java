package com.funongo.cms.service;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExcelValidatorService {
	
	
	
	@Resource(name="appProperties")
	Properties properties;
	
	public HashMap<String, ArrayList<String>> validateContent(Map<String, String> rowMap, HashSet<Integer> categories, HashSet<Integer> subCategories, HashSet<Integer> genres, HashSet<Integer> tps){
		int category = 0;		
		ArrayList<String> errors = new ArrayList<String>();
		String dateFormat = "yyyy-MM-dd HH:mm:ss";
		if(rowMap.get("CATEGORY_ID") != null){
			try{
				category = (int) Double.parseDouble(rowMap.get("CATEGORY_ID"));
			}
			catch(NumberFormatException e){
				errors.add("CATEGORY_ID should be a number");
			}
			
			if(!categories.contains(category)){
				errors.add("CATEGORY_ID incorrect");				
				//as category id is wrong we can't perform other checks, so return
				HashMap<String, ArrayList<String>> error = new HashMap<String, ArrayList<String>>();
				error.put(rowMap.get("CONTENT_NAME"), errors);
				return error;
			}
			
			
			int subcategory_id = 0;
			if(rowMap.get("SUBCATEGORY_ID") != null){
				boolean numberError = false;
				try{
					subcategory_id = (int) Double.parseDouble(rowMap.get("SUBCATEGORY_ID"));				
				}
				catch(NumberFormatException e){
					numberError = true;
					
				}
				if(!subCategories.contains(subcategory_id)){
					if(numberError){
						errors.add("SUBCATEGORY_ID should be a number");
					}
					else{
						errors.add("SUBCATEGORY_ID incorrect");
					}														
				}
			}
			
			int genre = 0;
			if(rowMap.get("GENRE_ID") != null){
				boolean numberError = false;
				try{
					genre = (int) Double.parseDouble(rowMap.get("GENRE_ID"));				
				}
				catch(NumberFormatException e){
					numberError = true;
					
				}
				if(!genres.contains(genre)){
					if(numberError){
						errors.add("GENRE_ID should be a number");
					}
					else{
						errors.add("GENRE_ID incorrect");
					}														
				}
			}
			else{
				errors.add("GENRE_ID cannot be blank");
			}
			
			int tp = 0;
			if(rowMap.get("TP_ID") != null){
				boolean numberError = false;
				try{
					tp = (int) Double.parseDouble(rowMap.get("TP_ID"));
				}
				catch(NumberFormatException e){
					numberError = true;
					
				}
				if(!tps.contains(tp)){
					if(numberError){
						errors.add("TP_ID should be a number");
					}
					else{
						errors.add("TP_ID incorrect");
					}														
				}
			}
			else{
				errors.add("TP_ID cannot be blank");
			}
			
						
			
			// validate content name. It's mandatory for all categories
			if(rowMap.get("CONTENT_NAME") != null){
				String contentName = rowMap.get("CONTENT_NAME");
				// validate length of content
				if(contentName.length() > 40){
					errors.add("CONTENT_NAME cannot be greater than 40 characters");
				}
			}
			else{
				errors.add("CONTENT_NAME cannot be blank");
			}
			
			// validate display name, It's mandatory for all categories
			if(rowMap.get("DISPLAY_NAME") != null){
				String displayName = rowMap.get("DISPLAY_NAME");
				// validate length of content
				if(displayName.length() > 50){
					errors.add("DISPLAY_NAME cannot be greater than 50 characters");
				}
			}
			else{
				errors.add("DISPLAY_NAME cannot be blank");
			}
			
			// validate cost, It's mandatory for all categories
			if(rowMap.get("COST") != null){
				try{
					 Double.parseDouble(rowMap.get("COST"));
				}
				catch(NumberFormatException e){
					errors.add("COST should be a number");
				}
			}
			else{
				errors.add("COST cannot be blank");
			}
			
			// validate language, It's mandatory for all categories
			if(rowMap.get("LANGUAGE") != null){				
				String language = rowMap.get("LANGUAGE");
				if(language.length() > 50){
					errors.add("LANGUAGE cannot be greater than 50 characters");
				}
			}
			else{
				errors.add("LANGUAGE cannot be blank");
			}
			
			// validate rating, It's mandatory for all categories
			if(rowMap.get("RATING") != null){
				try{
					double rating = Double.parseDouble(rowMap.get("RATING"));
					if(rating<0 || rating>5){
						errors.add("Rating should be in range 0-5");
					}
				}
				catch(NumberFormatException e){
					errors.add("RATING should be a number");
				}
			}
			else{
				errors.add("RATING cannot be blank");
			}
			
			// validate search, It's mandatory for all categories
			if(rowMap.get("SEARCH") != null){
				String search = rowMap.get("SEARCH");
				// validate length of content
				if(search.length() > 250){
					errors.add("SEARCH cannot be greater than 250 characters");
				}
			}
			else{
				errors.add("SEARCH cannot be blank");
			}
			
			// validate short description, It's mandatory for all categories
			if(rowMap.get("SHORT_DESCRIPTION") != null){
				String search = rowMap.get("SHORT_DESCRIPTION");
				// validate length of content
				if(search.length() > 250){
					errors.add("SHORT_DESCRIPTION cannot be greater than 250 characters");
				}
			}
			else{
				errors.add("SHORT_DESCRIPTION cannot be blank");
			}
			
			// validate long description, It's mandatory for all categories
			if(rowMap.get("LONG_DESCRIPTION") == null){
				errors.add("LONG_DESCRIPTION cannot be blank");
			}
			
			// validate file size, It's mandatory for all categories
			if(rowMap.get("FILE_SIZE") != null){
				try{
					Double.parseDouble(rowMap.get("FILE_SIZE"));
				}
				catch(NumberFormatException e){
					errors.add("FILE_SIZE should be a number");
				}
			}
			else{
				errors.add("FILE_SIZE cannot be blank");
			}	
			
			// validate content production date, It's mandatory for all categories
			if(rowMap.get("CONTENT_PRODUCTION_DATE") != null){
				String dateToValidate = rowMap.get("CONTENT_PRODUCTION_DATE");
				if(!isThisDateValid(dateToValidate, dateFormat)){
					errors.add("CONTENT_PRODUCTION_DATE is not valid");
				}
			}
			
			
			// validate short description, It's mandatory for all categories
			if(rowMap.get("AGE_GROUP") != null){
				String ageGroup = rowMap.get("AGE_GROUP");
				// validate length of age group
				if(ageGroup.length() > 30){
					errors.add("AGE_GROUP cannot be greater than 30 characters");
				}
			}
			else{
				errors.add("AGE_GROUP cannot be blank");
			}
			
			// validate valid from date, It's mandatory for all categories
			if(rowMap.get("VALIDFROM") != null){
				String dateToValidate = rowMap.get("VALIDFROM");
				if(!isThisDateValid(dateToValidate, dateFormat)){
					errors.add("VALIDFROM is not valid");
				}
			}
			else{
				errors.add("VALIDFROM cannot be blank");
			}
			
			// validate valid to date, It's mandatory for all categories
			if(rowMap.get("VALIDTO") != null){
				String dateToValidate = rowMap.get("VALIDTO");
				if(!isThisDateValid(dateToValidate, dateFormat)){
					errors.add("VALIDTO is not valid");
				}
			}
			else{
				errors.add("VALIDTO cannot be blank");
			}
			
			// validate smart url1, It's mandatory for videos
			if(rowMap.get("SMARTURL1") != null){
				String search = rowMap.get("SMARTURL1");
				// validate length of content
				if(search.length() > 200){
					errors.add("SMARTURL1 cannot be greater than 200 characters");
				}
			}
			else if(category == new Integer(properties.getProperty("videosId"))){
				errors.add("SMARTURL1 cannot be blank");
			}
			
			// validate smart url2, It's mandatory for movies
			if(rowMap.get("SMARTURL2") != null){
				String search = rowMap.get("SMARTURL2");
				// validate length of content
				if(search.length() > 200){
					errors.add("SMARTURL2 cannot be greater than 200 characters");
				}
			}
			else if(category == new Integer(properties.getProperty("moviesId"))){
				errors.add("SMARTURL2 cannot be blank");
			}
			
			// validate directors, It's mandatory for movies
			if(rowMap.get("DIRECTORS") != null){
				String directors = rowMap.get("DIRECTORS");
				// validate length of content
				if(directors.length() > 100){
					errors.add("DIRECTORS cannot be greater than 100 characters");
				}
			}
			else if(category == new Integer(properties.getProperty("moviesId"))){
				errors.add("DIRECTORS cannot be blank");
			}
			
			// validate producers, It's mandatory for movies
			if(rowMap.get("PRODUCERS") != null){
				String producers = rowMap.get("PRODUCERS");
				// validate length of content
				if(producers.length() > 100){
					errors.add("PRODUCERS cannot be greater than 100 characters");
				}
			}
			else if(category == new Integer(properties.getProperty("moviesId"))){
				errors.add("PRODUCERS cannot be blank");
			}
			
			// validate music directors, It's mandatory for movies
			if(rowMap.get("MUSIC_DIRECTORS") != null){
				String musicDirectors = rowMap.get("MUSIC_DIRECTORS");
				// validate length of content
				if(musicDirectors.length() > 100){
					errors.add("MUSIC_DIRECTORS cannot be greater than 100 characters");
				}
			}
			else if(category == new Integer(properties.getProperty("moviesId"))){
				errors.add("MUSIC_DIRECTORS cannot be blank");
			}
			
			// validate actors, It's mandatory for movies
			if(rowMap.get("ACTORS") != null){
				String actors = rowMap.get("ACTORS");
				// validate length of content
				if(actors.length() > 100){
					errors.add("ACTORS cannot be greater than 100 characters");
				}
			}
			else if(category == new Integer(properties.getProperty("moviesId"))){
				errors.add("ACTORS cannot be blank");
			}
			
			// validate actresses, It's mandatory for movies
			if(rowMap.get("ACTRESSES") != null){
				String actresses = rowMap.get("ACTRESSES");
				// validate length of content
				if(actresses.length() > 200){
					errors.add("ACTRESSES cannot be greater than 200 characters");
				}
			}
			else if(category == new Integer(properties.getProperty("moviesId"))){
				errors.add("ACTRESSES cannot be blank");
			}
			
			// validate singers, It's mandatory for movies
			if(rowMap.get("SINGERS") != null){
				String singers = rowMap.get("SINGERS");
				// validate length of content
				if(singers.length() > 300){
					errors.add("SINGERS cannot be greater than 300 characters");
				}
			}
			else if(category == new Integer(properties.getProperty("moviesId"))){
				errors.add("SINGERS cannot be blank");
			}
			
			// validate choreographer, It's mandatory for movies
			if(rowMap.get("CHOREOGRAPHER") != null){
				String choreographer = rowMap.get("CHOREOGRAPHER");
				// validate length of content
				if(choreographer.length() > 200){
					errors.add("CHOREOGRAPHER cannot be greater than 200 characters");
				}
			}
			else if(category == new Integer(properties.getProperty("moviesId"))){
				errors.add("CHOREOGRAPHER cannot be blank");
			}
			
			// validate supporting star cast, It's mandatory for movies
			if(rowMap.get("SUPPORTING_STAR_CAST") != null){
				String supportingStarCast = rowMap.get("SUPPORTING_STAR_CAST");
				// validate length of content
				if(supportingStarCast.length() > 500){
					errors.add("SUPPORTING_STAR_CAST cannot be greater than 500 characters");
				}
			}
			else if(category == new Integer(properties.getProperty("moviesId"))){
				errors.add("SUPPORTING_STAR_CAST cannot be blank");
			}
			
			// validate lyricist, It's mandatory for movies
			if(rowMap.get("LYRICIST") != null){
				String lyricist = rowMap.get("LYRICIST");
				// validate length of content
				if(lyricist.length() > 100){
					errors.add("LYRICIST cannot be greater than 100 characters");
				}
			}
			else if(category == new Integer(properties.getProperty("moviesId"))){
				errors.add("LYRICIST cannot be blank");
			}
			
			// validate release date, It's mandatory for all categories
			if(rowMap.get("RELEASEDATE") != null){
				String dateToValidate = rowMap.get("RELEASEDATE");
				if(!isThisDateValid(dateToValidate, dateFormat)){
					errors.add("RELEASEDATE is not valid");
				}
			}
			else if(category == new Integer(properties.getProperty("moviesId"))){
				errors.add("RELEASEDATE cannot be blank");
			}
			
			// validate production companies, It's mandatory for movies
			if(rowMap.get("PRODUCTION_COMPANIES") != null){
				String lyricist = rowMap.get("PRODUCTION_COMPANIES");
				// validate length of production companies
				if(lyricist.length() > 200){
					errors.add("PRODUCTION_COMPANIES cannot be greater than 200 characters");
				}
			}
			else if(category == new Integer(properties.getProperty("moviesId"))){
				errors.add("PRODUCTION_COMPANIES cannot be blank");
			}
			
			// validate image preview1, It's mandatory all categories
			if(rowMap.get("IMAGE_PREVIEW1") != null){
				String imagePreview = rowMap.get("IMAGE_PREVIEW1");
				System.out.println("img prev... "+imagePreview);
				if(!(imagePreview.equals("0.0")|| imagePreview.equals("1.0"))){
					errors.add("IMAGE_PREVIEW1 is not valid");
				}
			}
			else{
				errors.add("IMAGE_PREVIEW1 cannot be blank");
			}
			
			// validate image preview2, It's mandatory all categories
			if(rowMap.get("IMAGE_PREVIEW2") != null){
				String imagePreview = rowMap.get("IMAGE_PREVIEW2");
				
				if(!(imagePreview.equals("0.0")|| imagePreview.equals("1.0"))){
					errors.add("IMAGE_PREVIEW2 is not valid");
				}
			}
			else{
				errors.add("IMAGE_PREVIEW2 cannot be blank");
			}
			
			// validate image preview3, It's mandatory all categories
			if(rowMap.get("IMAGE_PREVIEW3") != null){
				String imagePreview = rowMap.get("IMAGE_PREVIEW3");
				
				if(!(imagePreview.equals("0.0")|| imagePreview.equals("1.0"))){
					errors.add("IMAGE_PREVIEW3 is not valid");
				}
			}
			else{
				errors.add("IMAGE_PREVIEW3 cannot be blank");
			}
			
			// validate image preview4, It's mandatory all categories
			if(rowMap.get("IMAGE_PREVIEW4") != null){
				String imagePreview = rowMap.get("IMAGE_PREVIEW4");
				
				if(!(imagePreview.equals("0.0")|| imagePreview.equals("1.0"))){
					errors.add("IMAGE_PREVIEW4 is not valid");
				}
			}
			else{
				errors.add("IMAGE_PREVIEW4 cannot be blank");
			}
			
			// validate image preview5, It's mandatory all categories
			if(rowMap.get("IMAGE_PREVIEW5") != null){
				String imagePreview = rowMap.get("IMAGE_PREVIEW5");
				
				if(!(imagePreview.equals("0.0")|| imagePreview.equals("1.0"))){
					errors.add("IMAGE_PREVIEW5 is not valid");
				}
			}
			else{
				errors.add("IMAGE_PREVIEW5 cannot be blank");
			}
			
			// validate poster url1, It's mandatory all categories
			if(rowMap.get("POSTERURL1") != null){
				String posterUrl = rowMap.get("POSTERURL1");
				
				if(!(posterUrl.equals("0.0")|| posterUrl.equals("1.0"))){
					errors.add("POSTERURL1 is not valid");
				}
			}
			else{
				errors.add("POSTERURL1 cannot be blank");
			}
			
			
			// validate poster url2, It's mandatory all categories
			if(rowMap.get("POSTERURL2") != null){
				String posterUrl = rowMap.get("POSTERURL2");
				
				if(!(posterUrl.equals("0.0")|| posterUrl.equals("1.0"))){
					errors.add("POSTERURL2 is not valid");
				}
			}
			else{
				errors.add("POSTERURL2 cannot be blank");
			}
						
						
			// validate poster url3, It's mandatory all categories
			if(rowMap.get("POSTERURL3") != null){
				String posterUrl = rowMap.get("POSTERURL3");
				
				if(!(posterUrl.equals("0.0")|| posterUrl.equals("1.0"))){
					errors.add("POSTERURL3 is not valid");
				}
			}
			else{
				errors.add("POSTERURL3 cannot be blank");
			}
						
			// validate poster url4, It's mandatory all categories
			if(rowMap.get("POSTERURL4") != null){
				String posterUrl = rowMap.get("POSTERURL4");
				
				if(!(posterUrl.equals("0.0")|| posterUrl.equals("1.0"))){
					errors.add("POSTERURL4 is not valid");
				}
			}
			else{
				errors.add("POSTERURL4 cannot be blank");
			}
						
						
			// validate poster url5, It's mandatory all categories
			if(rowMap.get("POSTERURL5") != null){
				String posterUrl = rowMap.get("POSTERURL5");
				
				if(!(posterUrl.equals("0.0")|| posterUrl.equals("1.0"))){
					errors.add("POSTERURL5 is not valid");
				}
			}
			else{
				errors.add("POSTERURL5 cannot be blank");
			}
						
						
			// validate poster url6, It's mandatory all categories
			if(rowMap.get("POSTERURL6") != null){
				String posterUrl = rowMap.get("POSTERURL6");
				
				if(!(posterUrl.equals("0.0")|| posterUrl.equals("1.0"))){
					errors.add("POSTERURL6 is not valid");
				}
			}
			else{
				errors.add("POSTERURL6 cannot be blank");
			}
			
			// validate duration, It's mandatory for videos and movies
			if(rowMap.get("DURATION") != null){
				try{
					Double.parseDouble(rowMap.get("DURATION"));
				}
				catch(NumberFormatException e){
					errors.add("DURATION should be a number");
				}
			}
			else if(category == new Integer(properties.getProperty("moviesId")) || 
					category == new Integer(properties.getProperty("videosId"))){
				errors.add("DURATION cannot be blank");
			}
			
			// validate grade, It's mandatory for movies
			if(rowMap.get("GRADE") != null){
				String grade = rowMap.get("GRADE");
				// validate length of production companies
				if(grade.length() > 10){
					errors.add("GRADE cannot be greater than 10 characters");
				}
			}
			else if(category == new Integer(properties.getProperty("moviesId"))){
				errors.add("GRADE cannot be blank");
			}
			
			// validate c lang, It's mandatory for all categories
			if(rowMap.get("C_LANG") != null ){
				String cLang = rowMap.get("C_LANG");
				// validate length of production companies
				if(cLang.length() > 50){
					errors.add("C_LANG cannot be greater than 50 characters");
				}
			}
			else{
				errors.add("C_LANG cannot be blank");
			}	
			
			// validate wrapping partner. It's not mandatoty
			if(rowMap.get("WRAPPING_PARTNER") != null){
				String contentName = rowMap.get("WRAPPING_PARTNER");
				// validate length of content
				if(contentName.length() > 50){
					errors.add("WRAPPING_PARTNER cannot be greater than 50 characters");
				}
			}
			
			// validate sub genre id. It's not mandatory
			if(rowMap.get("SUB_GENRE") != null){
				String subGenre = rowMap.get("SUB_GENRE");
				List<String> subGenreList = new ArrayList<String>();
				if(subGenre.contains("#")){
					String subGenreIds[] = subGenre.split("#");
					subGenreList =  Arrays.asList(subGenreIds);
				}
				else{System.out.println("sub genre else..");
					System.out.println(subGenre);
					subGenreList.add(subGenre);
				}
				for(String currentSubGenre: subGenreList){
					boolean numberError = false;
					int genreId = 0;
					try{
						genreId = (int) Double.parseDouble(currentSubGenre);
						System.out.println(genreId);
					}
					catch(NumberFormatException e){
						e.printStackTrace();
						numberError = true;
						
					}
					System.out.println("genre condition "+genres.contains(genreId));
					System.out.println("gen id"+genreId);
					if(!genres.contains(genreId)){
						if(numberError){
							errors.add("SUB_GENRE should be a number");
						}
						else{
							errors.add("SUB_GENRE incorrect");
						}														
					}
				}								
			}
			
			// validate content type, It's not mandatory
			if(rowMap.get("CONTENT_TYPE") != null){
				String contentType = rowMap.get("CONTENT_TYPE");
				// validate length of content
				if(contentType.length() > 20){
					errors.add("CONTENT_TYPE cannot be greater than 20 characters");
				}
			}		
			
			// validate image preview, It's not mandatory
			if(rowMap.get("IMAGE_PREVIEW") != null){
				String imagePreview = rowMap.get("IMAGE_PREVIEW");
				
				if(!(imagePreview.equals("0.0")|| imagePreview.equals("1.0"))){
					errors.add("IMAGE_PREVIEW is not valid");
				}
			}	
			
			// validate audio preview, It's not mandatory
			if(rowMap.get("AUDIO_PREVIEW") != null){
				String imagePreview = rowMap.get("AUDIO_PREVIEW");
				
				if(!(imagePreview.equals("0.0")|| imagePreview.equals("1.0"))){
					errors.add("AUDIO_PREVIEW is not valid");
				}
			}
			
			// validate video preview, It's not mandatory
			if(rowMap.get("VIDEO_PREVIEW") != null){
				String imagePreview = rowMap.get("VIDEO_PREVIEW");
				
				if(!(imagePreview.equals("0.0")|| imagePreview.equals("1.0"))){
					errors.add("VIDEO_PREVIEW is not valid");
				}
			}
			
			// validate preview file name, It's not mandatory
			if(rowMap.get("PREVIEW_FILE_NAME") != null){
				String search = rowMap.get("PREVIEW_FILE_NAME");
				// validate length of content
				if(search.length() > 50){
					errors.add("PREVIEW_FILE_NAME cannot be greater than 50 characters");
				}
			}
			
			// validate content version, It's not mandatory
			if(rowMap.get("CONTENT_VERSION") != null){
				String contentVersion = rowMap.get("CONTENT_VERSION");
				// validate length of content
				if(contentVersion.length() > 20){
					errors.add("CONTENT_VERSION cannot be greater than 20 characters");
				}
			}	
			
			// validate review, It's not mandatory
			if(rowMap.get("REVIEW") != null){
				String review = rowMap.get("REVIEW");
				// validate length of content
				if(review.length() > 300){
					errors.add("REVIEW cannot be greater than 300 characters");
				}
			}	
			
		}
		else{
			errors.add("CATEGORY_ID cannot be blank");
		}
		HashMap<String, ArrayList<String>> error = new HashMap<String, ArrayList<String>>();
		if(errors.size() > 0)
			error.put(rowMap.get("CONTENT_NAME"), errors);
		return error;
	}
	
	public boolean isThisDateValid(String dateToValidate, String dateFormat){
		
		if(dateToValidate == null){
			return false;
		}
		
		//String dateString = dateToValidate;
		dateToValidate = dateToValidate.replace("'", "");
		
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setLenient(false);
		
		try {
			
			//if not valid, it will throw ParseException
			Date date = sdf.parse(dateToValidate);
			System.out.println("DDDDDDDDD "+date);
		
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

}
