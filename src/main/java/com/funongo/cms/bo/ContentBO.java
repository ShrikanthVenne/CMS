package com.funongo.cms.bo;

public class ContentBO {
	
	private Integer content_id;
	
	private String content_name;
	
	private CategoryBO category;

	public Integer getContent_id() {
		return content_id;
	}

	public void setContent_id(Integer content_id) {
		this.content_id = content_id;
	}

	public String getContent_name() {
		return content_name;
	}

	public void setContent_name(String content_name) {
		this.content_name = content_name;
	}

	public CategoryBO getCategory() {
		return category;
	}

	public void setCategory(CategoryBO category) {
		this.category = category;
	}
}
