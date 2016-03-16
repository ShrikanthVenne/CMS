package com.funongo.cms.conf;

import java.io.FileInputStream;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertiesBean {
	
	@Bean(name="appProperties")
	public Properties portalProperties(){
		Properties properties = new Properties();
		FileInputStream file;
		try {
			file = new FileInputStream("C:\\cms_config\\contentProperties.properties");			
			properties.load(file);
			file.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return properties;
	}
	
	@Bean(name="emailProperties")
	public Properties emailProps(){
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
		return properties;
	}
}
