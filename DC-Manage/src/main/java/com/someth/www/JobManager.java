package com.someth.www;

import com.someth.www.job.JobInfo;
import com.someth.www.job.JobRespose;
import com.someth.www.jobmanage.ManegeSystem;
import com.someth.www.share.loadbalance.DCStates;
import org.apache.commons.configuration.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asus-pc on 2016/11/12.
 * 1、create jobmange actorsystem once
 * 2、run jobs
 */
public class JobManager {

	private static JobManager jobMC;
	private static final  Object mcLock = new Object();
	private static ManegeSystem manegeSystem;
	private final Map<String, JobExecuter> jobContexts = new HashMap<String, JobExecuter>();

	static {
		try {
			JobFactory.register("WordCount",
                Class.forName("com.someth.www.job.WCJobExcuter").asSubclass(JobExecuter.class));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private JobManager(DCStates dcStates, Configuration conf){
		init(dcStates, conf);
		System.out.println("job manager init");
	}

	private void init(DCStates dcStates, Configuration conf){
		manegeSystem = new ManegeSystem(dcStates, conf);
		//manegeSystem.start();
	}

	public static JobManager get(DCStates dcStates, Configuration conf){

		if(jobMC != null){
			return jobMC;
		}
		synchronized (mcLock){
			if (jobMC != null){
				return jobMC;
			}
			jobMC = new JobManager(dcStates, conf);
		}
		return  jobMC;
	}

	public JobRespose runJob(JobInfo jobInfo){
		System.out.println("job type is------->" + jobInfo.getJobType());
		return JobFactory.getJobContext(jobInfo, manegeSystem).dispatchJob(jobInfo);
	}
}
