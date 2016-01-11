package com.funongo.cms.bo;

import java.util.ArrayList;
import java.util.HashMap;

public class UploadBO {
	
	private ArrayList<ContentBO> contentList;
	
	private HashMap<String, ArrayList<String>> errors;

	public ArrayList<ContentBO> getContentList() {
		return contentList;
	}

	public void setContentList(ArrayList<ContentBO> contentList) {
		this.contentList = contentList;
	}

	public HashMap<String, ArrayList<String>> getErrors() {
		return errors;
	}

	public void setErrors(HashMap<String, ArrayList<String>> errors) {
		this.errors = errors;
	}
	
	

}
