package com.kmutt.sit.cloud;

import java.util.ArrayList;
import java.util.List;

import com.kmutt.sit.pegasus.dax.DaxNode;
import com.kmutt.sit.utils.JavaHelper;

public class CloudVm {

	public static enum Status{
		AVAILABLE, BUSY;
	}
	
	private List<DaxNode> taskList = new ArrayList<DaxNode>();
	private Integer vmId;
	private Double currentTime;
	private Double nextAvailableTime;
	
	private Integer vmActionOrder = 0;
	
	private CloudVmActionManager actionManger = new CloudVmActionManager();
	
	public CloudVm() {
		this.actionManger.setCloudVm(this);
	}
	
	public Integer getVmActionOrder() {
		return this.vmActionOrder++;
	}
	
	public void recieveTask(DaxNode task) {
		taskList.add(task);
		actionManger.submitTask(task.getSubmittedTime(), task.getId());
		
		
		if(task.getLastParentFinishTime() > this.nextAvailableTime) {
			createVmIdleEvent(task);
		}
		
		if(!JavaHelper.isNull(task.getWaitingTime()) && task.getWaitingTime() > 0) {			
			createTaskWaitingEvent(task);
		}

		createProcessTaskEvent(task);

		actionManger.releaseTask(task.getFinishedTime(), task.getId());
		this.nextAvailableTime = task.getFinishedTime();
	}
	
	private void createProcessTaskEvent(DaxNode task) {
		CloudEvent event = new CloudEvent(CloudEvent.EventType.PROCESS_TASK, this.vmId,
				task.getId(), task.getCanStartTime(), task.getFinishedTime(), task.getRuntimeDouble());
		
		CloudEventObserver.getCloudEventObserver().addCloudEvent(event);
		
		actionManger.startProcessTask(task.getCanStartTime(), task.getId());
		actionManger.stoptProcessTask(task.getFinishedTime(), task.getId());
	}
	
	private void createTaskWaitingEvent(DaxNode task) {
		CloudEvent event = new CloudEvent(CloudEvent.EventType.TASK_WAITING, this.vmId,
				task.getId(), task.getStartWaitingTime(), task.getCanStartTime(), task.getWaitingTime());

		CloudEventObserver.getCloudEventObserver().addCloudEvent(event);
		
		actionManger.startWaitProcessTask(task.getStartWaitingTime(), task.getId());
		actionManger.stoptWaitProcessTask(task.getCanStartTime(), task.getId());
	}
	
	private void createVmIdleEvent(DaxNode task) {
		CloudEvent event = new CloudEvent(CloudEvent.EventType.VM_IDLE, this.vmId,
				task.getId(), this.nextAvailableTime, task.getCanStartTime(), (task.getCanStartTime() - this.nextAvailableTime));

		CloudEventObserver.getCloudEventObserver().addCloudEvent(event);
		
		actionManger.startIdleVm(this.nextAvailableTime, task.getId());
		actionManger.stopIdleVm(task.getCanStartTime(), task.getId());
	}
	
	public void start(Double requestTime) {
		actionManger.startVm(requestTime);
	}
	
	public void stop(Double requestTime) {
		actionManger.stopVm(requestTime);
	}
	
	public void submitCluster(Double requestTime, String clusterId) {
		actionManger.submitCluster(requestTime, clusterId);
	}
	
	public void releaseCluster(Double requestTime, String clusterId) {
		actionManger.releaseCluster(requestTime, clusterId);
	}
		
	public boolean isAvailable(double requestTime) {		
		return this.nextAvailableTime <= requestTime;
	}

	public List<DaxNode> getTaskList() {
		return taskList;
	}

	public Integer getVmId() {
		return vmId;
	}

	public void setVmId(Integer vmId) {
		this.vmId = vmId;
	}

	public Double getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(Double currentTime) {
		this.currentTime = currentTime;
	}

	public double getNextAvailableTime() {
		return nextAvailableTime;
	}

	public void setNextAvailableTime(double nextAvailableTime) {
		this.nextAvailableTime = nextAvailableTime;
	}
}
