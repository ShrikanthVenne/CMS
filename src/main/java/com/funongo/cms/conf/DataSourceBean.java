package com.funongo.cms.conf;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataSourceBean {

	@Bean(destroyMethod = "close", name = "portalDs")
	public DataSource portalDataSource() {
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

	@Bean(name = "portalDB")
	public JdbcTemplate getPortalDBJdbcTemplate() {
		return new JdbcTemplate(portalDataSource());
	}

	@Bean(name = "CMSDataSource")
	public DriverManagerDataSource getCmsTrialDataSource() {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		driverManagerDataSource.setUrl("jdbc:sqlserver://43.242.215.66:1433;databaseName=CMS_TRIAL;");
		driverManagerDataSource.setUsername("sa");
		driverManagerDataSource.setPassword("sql@123");
		return driverManagerDataSource;
	}

	@Bean(name = "CMSTrial")
	public JdbcTemplate getCMSTrialJdbcTemplate() {
		return new JdbcTemplate(getCmsTrialDataSource());
	}

}
