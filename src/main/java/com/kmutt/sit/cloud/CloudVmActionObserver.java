package com.kmutt.sit.cloud;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CloudVmActionObserver {
	
	private static CloudVmActionObserver instance = new CloudVmActionObserver();
	
	private List<CloudVmCompletedAction> actionList = new ArrayList<CloudVmCompletedAction>();
	
	private Integer vmactionId = 0;
	
	private CloudVmActionObserver() {}
	
	public static CloudVmActionObserver getCloudVmActionObserver() {
		return instance;
	}

	public void addCloudVmCompletedAction(CloudVmCompletedAction action) {
		// TODO Auto-generated method stub
		this.actionList.add(action);
	}

	public List<CloudVmCompletedAction> getCloudVmCompletedActionList() {
		// TODO Auto-generated method stub
		return this.actionList;
	}
	
	public Integer getVmActionId() {
		return this.vmactionId++;
	}
	
	public List<CloudVmAction> getCloudVmStartActionList(){
		return actionList.stream().map(m -> m.getStartAction())
				.collect(Collectors.toList());
	}

	public List<CloudVmAction> getCloudVmStopActionList(){
		return actionList.stream().map(m -> m.getStopAction())
				.collect(Collectors.toList());
	}
}
