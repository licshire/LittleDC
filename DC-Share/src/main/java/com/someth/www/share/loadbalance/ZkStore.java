package com.someth.www.share.loadbalance;

import com.someth.www.share.CONF;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by asus-pc on 2016/11/19.
 * class name ZkStore, offer zk operation
 * not mention muilty thread, Todo
 * not mention old session and new session
 */
public class ZkStore{

	private static final Log LOG = LogFactory.getLog(ZkStore.class);
	private String zkAddr = null;
	private int zkTimeout = 3000;
	private ZooKeeper zk = null;
	private int tryConnectTimes = 5;

	private CountDownLatch connectedSignal = new CountDownLatch(1);

	public ZkStore(Configuration conf){

		zkAddr = conf.getString(CONF.ZKADDRCONF, CONF.ZKADDR_DEFAULT);
		LOG.info("zkADDR:" + zkAddr);
		zkTimeout = conf.getInt(CONF.ZKTIMEOUTCONF, CONF.ZKTIMEOUT_DEFAULT);
		tryConnectTimes = conf.getInt(CONF.ZKTRYSCONF, CONF.ZKTRYS_DEFAULT);
		try {
			connectZk(zkAddr);
		} catch (Exception e) {
			LOG.error("connect to zk error", e);
		}
	}

	private void connectZk(String zkHost) throws IOException, InterruptedException {
		zk = new ZooKeeper(zkAddr, zkTimeout, null);
		zk.register(new ZkWatcher());
		connectedSignal.await();
	}

	public void close(){
		try {
			if(zk != null){
				zk.close();
			}
			else{
				System.out.println("zk not exist");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void setData(String rootPath, String data){

		byte[] buffer = data.getBytes();
		try {
			zk.setData(rootPath, buffer, -1);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String getData(String rootPath){
		try {
			byte[] buffer = zk.getData(rootPath, false, null);
			return new String(buffer, "UTF-8");
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public void create(String rootPath, String data){

		try {
			byte[] buffer = data.getBytes();
			String[] pathToCreate = rootPath.split("/");
			//should check path is illegal or not here
			StringBuilder tmpPath = new StringBuilder();
			for(int i = 1; i < pathToCreate.length - 1; i++){
				tmpPath.append("/").append(pathToCreate[i]);
				createDir(tmpPath.toString());
			}
			createDirWithData(rootPath, buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void createDir(String path) throws InterruptedException {
		try {
			zk.create(path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} catch (KeeperException e) {
			if(e.code() == KeeperException.Code.NODEEXISTS){
				System.out.println("path " + path + " is already exist" );
			}
		}
	}

	public void createDirWithData(String path, byte[] data) throws InterruptedException {

		try {
			//zk slaveId may should be write as EPHEMERAL, for unexpect problems when system running
			//thus, slaveId may not been cleaned
			//zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} catch (KeeperException e) {
			if(e.code() == KeeperException.Code.NODEEXISTS){
				System.out.println("tmp path " + path + " " + data + " already exist" );
			}
		}
	}

	public void deleteDir(String path){
		try {
			zk.delete(path, -1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			if (e.code() == KeeperException.Code.NONODE){
				System.out.println("no node: " + path + " delete error");
			}
			else if(e.code() == KeeperException.Code.NOTEMPTY){
				System.out.println("not empty node: " + path + " delete error");
			}
		}
	}

	public List<String> listGroup(String groupName){
		List<String> children = null;
		try {
			children = zk.getChildren(groupName, false);
			if(children.isEmpty()){
				LOG.info("zk dir is empty");
			}
			return children;
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return children;
	}

	private final class ZkWatcher implements Watcher{

		public void process(WatchedEvent watchedEvent) {
			switch (watchedEvent.getState()){
				case SyncConnected:
					LOG.info("ZK connected");
					connectedSignal.countDown();
					break;
				case Disconnected:
					LOG.info("zk disconnected");
					zk = null;
					break;
				case Expired:
					LOG.info("zk connect expired");
					try {
						LOG.info("retry connect zk");
						connectZk(zkAddr);
					} catch (Exception e) {
						LOG.info("retry connnect zk error ", e);
					}
					break;
				default:
					LOG.info("unexpected envent");
			}

		}
	}

}
