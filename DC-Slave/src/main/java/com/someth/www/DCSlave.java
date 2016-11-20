package com.someth.www;

import com.someth.www.jobrunner.SlaveSystem;
import com.someth.www.share.loadbalance.DCStates;
import com.someth.www.share.loadbalance.RoleStates;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.net.UnknownHostException;

/**
 * Created by asus-pc on 2016/11/19.
 */
public class DCSlave {

	private static Configuration conf = null;
	private static final String slaveId = java.util.UUID.randomUUID().toString();
	DCStates roleStates = null;
	SlaveSystem slaveSystem = null;

	public DCSlave() throws UnknownHostException {
		initConf();

		//init roleStates in zk
		roleStates = new RoleStates(conf, "slave", slaveId);
		roleStates.listGroup("/");
		//init ActorSystem
		slaveSystem = new SlaveSystem(conf, roleStates);
	}

	private void initConf() {
		try {
			conf = new PropertiesConfiguration(this.getClass().getClassLoader().getResource("DCSlave.properties"));
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}



	/**
	 * clean  data here
	 */
	public void clean(){
		if(roleStates != null){
			roleStates.cleanZkData();
		}
	}


	public static void main(String args[]) throws ConfigurationException, UnknownHostException {
		DCSlave dcSlave = new DCSlave();
		//System.out.println("now clean....");
		//dcSlave.clean();
	}
}
