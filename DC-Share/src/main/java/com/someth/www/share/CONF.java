package com.someth.www.share;

/**
 * Created by asus-pc on 2016/11/19.
 */
public class CONF {

	public static String ZKADDRCONF = "zk_addr";
	public static String ZKTIMEOUTCONF = "zk_timeout";
	public static String ZKTRYSCONF = "zk_trytime";

	public static String ZKADDR_DEFAULT = "10.205.20.226";
	public static Integer ZKTIMEOUT_DEFAULT = 3000;
	public static Integer ZKTRYS_DEFAULT = 5;

	public static String SLAVE_ROOTPATH = "slave_zk_path";
	public static String SLAVE_ROOTPATH_DEFAULT = "/DC/Slave";

	public static String MANAGE_ROOTPATH = "manage_zk_path";
	public static String MANAGE_ROOTPATH_DEFAULT = "/DC/Manage";

	public static String SYSTEM_ACTOR_PORT = "system-actor-port";
	public static Integer ACTOR_PORT_DEFULT = 2556;

}
