package com.someth.www.job;

/**
 * Created by asus-pc on 2016/11/12.
 */
public class JobInfo {

	private String jobId = "";
	private String jobType = "";
	private String jobContent = "";

	public String getJobType() {
		return jobType;
	}

	public String getJobId() {
		return jobId;
	}

	public String getJobContent() {
		return jobContent;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public void setJobContent(String jobContent) {
		this.jobContent = jobContent;
	}

	//other to do
}
