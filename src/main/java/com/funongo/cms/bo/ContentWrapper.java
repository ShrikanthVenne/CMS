package com.funongo.cms.bo;

import java.util.ArrayList;
import java.util.List;

public class ContentWrapper {

	private List<Content> contents;

	public List<Content> getContents() {
		return contents;
	}

	public void setContents(List<Content> contents) {
		if (contents == null) {
			contents = new ArrayList<Content>();
		}
		this.contents = contents;
	}
}
