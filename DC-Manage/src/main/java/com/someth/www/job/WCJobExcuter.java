package com.someth.www.job;

import com.someth.www.JobExecuter;
import com.someth.www.jobmanage.ManegeSystem;

import java.io.*;

/**
 * Created by asus-pc on 2016/11/12.
 */
public class WCJobExcuter extends JobExecuter {

	public WCJobExcuter(JobInfo jobInfo, ManegeSystem manegeSystem){
		super(jobInfo, manegeSystem);
	}

	public JobRespose dispatchJob(JobInfo jobInfo) {
		System.out.println("dispatching job");

		Integer totalNumber = 0;
		File filename = new File(jobInfo.getJobContent());
		System.out.println(filename.getAbsolutePath());
		InputStreamReader reader = null;

		String line = "";
		try {
			reader = new InputStreamReader(
				new FileInputStream(filename));
			BufferedReader br = new BufferedReader(reader);

			line = br.readLine();
			while (line != null){
				try {
					Integer num = (Integer)this.manegeSystem.wcOneLine(line);
					line = br.readLine();
					totalNumber += num;
				}catch (NullPointerException e){
					System.out.println(e);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		JobRespose jobRespose = new JobRespose();
		jobRespose.errNo = 200;
		jobRespose.wcTest = totalNumber;
		jobRespose.resultDesc = "wordcount test success";

		return jobRespose;

	}

}
