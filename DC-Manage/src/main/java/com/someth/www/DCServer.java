package com.someth.www;

import com.someth.www.job.JobInfo;
import com.someth.www.job.JobRespose;
import com.someth.www.share.loadbalance.DCStates;
import com.someth.www.share.loadbalance.RoleStates;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.net.UnknownHostException;

/**
 *
 *
 */
public class DCServer {

	private static Configuration conf = null;
	private static final String manageId = java.util.UUID.randomUUID().toString();
	private DCStates manageStates = null;
	private JobManager jobManager = null;

	public DCServer() throws UnknownHostException {
		initConf();
		manageStates = new RoleStates(conf, "manage", manageId);
		manageStates.setDataToZk("alive");
		jobManager = JobManager.get(manageStates, conf);
	}

	private void initConf() {
		try {
			conf = new PropertiesConfiguration(this.getClass().getClassLoader().getResource("DCSlave.properties"));
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static void main( String[] args ){
		JobInfo jobInfo = new JobInfo();
		jobInfo.setJobType("WordCount");
		jobInfo.setJobContent("./pom.xml");
		try {
			DCServer dcServer = new DCServer();
			dcServer.jobManager.runJob(jobInfo);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
//		JobManager jobManager = JobManager.get();
//		JobRespose jobRespose = jobManager.runJob(jobInfo);

		System.out.println("------------->end");
//		System.out.println("file word number is " + jobRespose.wcTest);
	}
}
