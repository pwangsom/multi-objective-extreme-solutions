package com.kmutt.sit.cloud;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CloudVmCompletedAction {

	private CloudVmAction startAction;
	private CloudVmAction stopAction;	
	
	public void setStartAction(CloudVmAction startAction) {
		this.startAction = startAction;		
	}
	
	public void setStopAction(CloudVmAction stopAction) {
		this.stopAction = stopAction;
	}

	public CloudVmAction getStartAction() {
		return startAction;
	}

	public CloudVmAction getStopAction() {
		return stopAction;
	}
	
	public List<CloudVmAction> getAction() {
		return Arrays.asList(startAction, stopAction).stream().collect(Collectors.toList());
	}
}
