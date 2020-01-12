package com.snap.common.util;

import java.awt.AWTException;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.snap.service.SnapLaunchService;

public class LaunchJob implements Job{
	private static final Logger logger = Logger.getLogger(LaunchJob.class.getName());

	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		logger.debug("inside execute");
		JobDataMap dtMap = context.getJobDetail().getJobDataMap();			
		String snapPath = (String) dtMap.get("1");
		
		SnapLaunchService launchService = new SnapLaunchService();
		try {
			launchService.snapLaunch(snapPath);
		} catch (IOException | AWTException e) {
			logger.error("Exception occured..", e);
		}
		
		logger.debug("launched....");
	}

	
}
