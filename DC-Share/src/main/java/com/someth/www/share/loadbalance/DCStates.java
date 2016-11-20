package com.someth.www.share.loadbalance;

import java.util.List;

/**
 * Created by asus-pc on 2016/11/20.
 */
public abstract class DCStates {

	public abstract List<String> listGroup(String rolePath);

	public abstract void setDataToZk(String data);

	public abstract String getDataFromZk(String rootPath);
	public abstract String getDataFromZk();

	public abstract void cleanZkData();

}
