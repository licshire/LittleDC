package com.someth.www;

import com.someth.www.job.JobInfo;
import com.someth.www.job.JobRespose;
import com.someth.www.jobmanage.ManegeSystem;

/**
 * Created by asus-pc on 2016/11/12.
 */
public abstract class JobExecuter {

	public JobInfo jobInfo = null;

	public ManegeSystem getManegeSystem() {
		return manegeSystem;
	}

	public ManegeSystem manegeSystem = null;

	public JobExecuter(JobInfo jobInfo, ManegeSystem manegeSystem){
		this.jobInfo = jobInfo;
		this.manegeSystem = manegeSystem;
	}

	public JobInfo getJobId(){
		return this.jobInfo;
	}

	public abstract JobRespose dispatchJob(JobInfo jobInfo);

}
