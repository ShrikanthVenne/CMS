package com.funongo.cms.cron.listener;

import java.text.ParseException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import com.funongo.cms.cron.SendDailyUploadsJob;

public class QuartzListener implements ServletContextListener {

	Scheduler scheduler = null;
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("Context Initialized");		
		
		JobDetail job = new JobDetail();
		job.setName("Daily Uploads");
		job.setJobClass(SendDailyUploadsJob.class);
		
		
		// set the trigger
		CronTrigger cronTrigger = new CronTrigger();
		cronTrigger.setName("TriggerName");
		try {
			cronTrigger.setCronExpression("0 0 21 * * ?");
			//cronTrigger.setCronExpression("0 0/1 * * * ?");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//schedule it
		
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, cronTrigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("Context Destroyed");		
	}

	

}
