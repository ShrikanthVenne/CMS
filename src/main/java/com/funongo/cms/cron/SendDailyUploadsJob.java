package com.funongo.cms.cron;

import java.util.Properties;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.funongo.cms.bo.EmailBO;
import com.funongo.cms.service.ReportService;
import com.funongo.cms.service.SendEmailService;

public class SendDailyUploadsJob implements Job {
	
	
	

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("Job Executed");	
		ReportService reportService = new ReportService();
		reportService.sendDailyReport();
	}

}
