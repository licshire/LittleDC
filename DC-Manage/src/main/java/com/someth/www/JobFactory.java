package com.someth.www;

import com.someth.www.job.JobInfo;
import com.someth.www.jobmanage.ManegeSystem;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by asus-pc on 2016/11/12.
 */
public class JobFactory {
	private static Map<String, Class<? extends JobExecuter>> jobs = new HashMap<String, Class<? extends JobExecuter>>();

	private JobFactory(){


	}

	public static void register(String jobType, Class<? extends JobExecuter> jobContext){
		jobs.put(jobType, jobContext);
	}

	static JobExecuter getJobContext(JobInfo jobInfo, ManegeSystem manegeSystem){
		try {
			if( !jobs.containsKey(jobInfo.getJobType())){
				throw new Exception("no this job");
			}
			Constructor<? extends JobExecuter> job = jobs.get(jobInfo.getJobType()).getConstructor(JobInfo.class, ManegeSystem.class);
			return job.newInstance(jobInfo, manegeSystem);
		}
		catch (Exception ex){
			return null;
		}
	}


}
