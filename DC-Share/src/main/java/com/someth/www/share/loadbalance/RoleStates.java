package com.someth.www.share.loadbalance;

import com.someth.www.share.CONF;
import org.apache.commons.configuration.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by asus-pc on 2016/11/20.
 */
public class RoleStates extends DCStates {
	private static ZkStore zk = null;
	private static String localHost = null;
	private static String rolePath  = null;
	private static String serviceRole = null;
	public RoleStates(Configuration conf, String role, String roleId) throws UnknownHostException {
		zk = new ZkStore(conf);
		serviceRole = role;
		//zk.listGroup("/");
		//get slave servece port here
		localHost = getLocalHost(conf);
		rolePath  = getZkPath(conf, serviceRole, roleId);
		zk.create(rolePath, localHost);
	}

	public List<String> listGroup(String rolePath){
		return zk.listGroup(rolePath);
	}

	public void setDataToZk(String data){
		String dataToWrite = localHost + "#" + data;
		zk.setData(rolePath, dataToWrite);
	}

	public String getDataFromZk(String absolutPath) {
		String data = zk.getData(absolutPath);
		if(data == null){
			System.out.println("data getted is null");
			return  null;
		}
		return data;
	}

	public String getDataFromZk(){
		String data = zk.getData(rolePath);
		if(data == null){
			System.out.println("data getted is null");
			return  null;
		}
		return data;
	}

	public void cleanZkData(){
		zk.deleteDir(rolePath);
		zk.close();
	}

	public String getZkPath(Configuration conf, String role, String slaveId){
		String rootPath = "";
		if(role == "manage"){
			rootPath = conf.getString(CONF.MANAGE_ROOTPATH, CONF.MANAGE_ROOTPATH_DEFAULT);
		}
		else if(role == "slave"){
			rootPath = conf.getString(CONF.SLAVE_ROOTPATH, CONF.SLAVE_ROOTPATH_DEFAULT);
		}
		else{
			System.out.println("unexpected role");
		}
		return rootPath + "/" + slaveId;
	}

	public String getLocalHost(Configuration conf) throws UnknownHostException {
		int actorPort = conf.getInt(CONF.SYSTEM_ACTOR_PORT, CONF.ACTOR_PORT_DEFULT);
		return "127.0.0.1" + ":" + actorPort;

//		return InetAddress.getLocalHost().getHostName() + ":" + actorPort;
	}
}
