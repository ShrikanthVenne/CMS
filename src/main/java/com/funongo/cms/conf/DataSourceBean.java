package com.funongo.cms.conf;


import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceBean {
	
	@Bean(destroyMethod = "close", name="portalDs")
	public DataSource portalDataSource(){
		org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();
		ds.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        ds.setUrl("jdbc:sqlserver://43.242.215.66;databaseName=CMS_Trial;user=sa;password=sql@123");           
        ds.setInitialSize(25);
        ds.setMaxActive(25);
        ds.setMaxIdle(25);       
        ds.setRemoveAbandonedTimeout(300);
        ds.setTimeBetweenEvictionRunsMillis(30000);
        ds.setMaxWait(10000);
        ds.setValidationQuery("SELECT 1");
        ds.setDefaultAutoCommit(true);
        ds.setLogAbandoned(true);
        ds.setRemoveAbandoned(true);
        return ds;
	}
}
