package com.funongo.cms.bo;

import org.springframework.web.multipart.MultipartFile;


public class FileBO {
	
	private MultipartFile file;
	
	private boolean app;
	
	private boolean wap;

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public boolean isApp() {
		return app;
	}

	public void setApp(boolean app) {
		this.app = app;
	}

	public boolean isWap() {
		return wap;
	}

	public void setWap(boolean wap) {
		this.wap = wap;
	}	

}
