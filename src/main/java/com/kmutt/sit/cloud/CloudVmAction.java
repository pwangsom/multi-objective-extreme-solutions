package com.kmutt.sit.cloud;

public class CloudVmAction {
	
	public enum VmActionType{
		START_VM, STOP_VM,
		SUBMIT_TASK, RELEASE_TASK,
		START_IDLE_VM, STOP_IDLE_VM,
		START_WAITING_PROCESS, STOP_WAITING_PROCESS,
		START_PROCESS_TASK, STOP_PROCESS_TASK,
		REQUEST_FILE, RECEIVE_FILE,
		SUBMIT_CLUSTER, RELEASE_CLUSTER;
	}
	
	private Integer actionId;
	private Double actionTime;
	private Integer vmId;
	private Integer vmActionOrder;
	private String actionLog;
	private CloudVmAction.VmActionType actionType;
	
	public CloudVmAction() {
		
	}

	public Double getActionTime() {
		return actionTime;
	}

	public void setActionTime(Double actionTime) {
		this.actionTime = actionTime;
	}

	public String getActionLog() {
		return String.format("%05d:%s", actionId, this.actionLog);
	}

	public void setActionLog(String actionLog) {
		this.actionLog = actionLog;
	}

	public CloudVmAction.VmActionType getActionType() {
		return actionType;
	}

	public void setActionType(CloudVmAction.VmActionType actionType) {
		this.actionType = actionType;
	}

	public Integer getVmId() {
		return vmId;
	}

	public void setVmId(Integer actionVmId) {
		this.vmId = actionVmId;
	}

	public Integer getActionId() {
		return actionId;
	}

	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}
		
	public Integer getVmActionOrder() {
		return vmActionOrder;
	}

	public void setVmActionOrder(Integer vmActionOrder) {
		this.vmActionOrder = vmActionOrder;
	}

	@Override
	public String toString() {
		return String.format("%.2f:%s", this.actionTime, this.actionLog);
	}
}
