package com.snap.processor;

import java.util.Calendar;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

import com.snap.common.util.LaunchJob;

public class LaunchScheduler {
	private static final Logger logger = Logger.getLogger(LaunchScheduler.class.getName());

	public void setUpJob(String snapPath, String p_processTime) throws SchedulerException {
		logger.debug("inside setUpJob()");
		if (null != snapPath && null != p_processTime) {
			logger.debug("snapPath and P_ProcessTime not null");
			JobDetail job = new JobDetail();
			String uuid = UUID.randomUUID().toString();
			job.setName("dummyJobName"+uuid);
			job.setJobClass(LaunchJob.class);

			JobDataMap l_jobDataMap = new JobDataMap();
			l_jobDataMap.put("1", snapPath);
			job.setJobDataMap(l_jobDataMap);
			
			// configure the scheduler time
			SimpleTrigger trigger = new SimpleTrigger();
			Calendar futureDate = Calendar.getInstance(); 
		    futureDate.set(Calendar.YEAR, Integer.parseInt(p_processTime.substring(0, p_processTime.indexOf("-", 0))));
		    futureDate.set(Calendar.MONTH, Integer.parseInt(p_processTime.substring(p_processTime.indexOf("-", 0)+1, p_processTime.lastIndexOf("-")))-1);
		    futureDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(p_processTime.substring( p_processTime.lastIndexOf("-")+1, p_processTime.indexOf("::"))));
		    futureDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(p_processTime.substring(p_processTime.indexOf("::")+2,p_processTime.lastIndexOf(":"))));
		    futureDate.set(Calendar.MINUTE, Integer.parseInt(p_processTime.substring(p_processTime.lastIndexOf(":")+1)));
		    futureDate.set(Calendar.SECOND, 0);
		    futureDate.set(Calendar.MILLISECOND, 0);
		    trigger.setName("trigger"+snapPath+uuid);
			trigger.setStartTime(futureDate.getTime());
			trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
			//trigger.setRepeatInterval(30000);
			trigger.setRepeatInterval(24L * 60L * 60L * 1000L);
			//trigger.sets
			
			//schedule it
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
			logger.debug("launch schedule on "+futureDate.getTime());
		}
	}
	public static void main(String[] args) throws SchedulerException {
	}
}
