package com.kmutt.sit.workflow.component;

import java.util.ArrayList;
import java.util.List;

import com.kmutt.sit.pegasus.dax.DaxNode;

public class TimeSlot {

	private int timeIndex;
	private boolean isEmptyTimeSlot;
	private DaxNode computeTask;
	private Double startTime;
	private Double finishTime;
	private Double computeTime = 0.00;
	private Double actualRunTime;
	
	private int localCommuTask = 0;
	private int remoteCommuTask = 0;
	private Double commuTime = 0.00;
	private List<CommunicationTask> commuTaskList;	
	
	public TimeSlot() {
		this.isEmptyTimeSlot = true;
		this.computeTask = null;
		this.commuTaskList = new ArrayList<CommunicationTask>();
	}
	
	private void computeActualRuntime() {
		this.actualRunTime = this.computeTime + this.commuTime;
		this.finishTime = this.startTime + this.actualRunTime;
	}
	
	public int getTimeIndex() {
		return timeIndex;
	}

	public void setTimeIndex(int timeIndex) {
		this.timeIndex = timeIndex;
	}

	public boolean isEmptyTimeSlot() {
		return isEmptyTimeSlot;
	}
	
	public void setEmptyTimeSlot(boolean isEmptyTimeSlot) {
		this.isEmptyTimeSlot = isEmptyTimeSlot;
	}
	
	public DaxNode getComputeTask() {
		return computeTask;
	}
	
	public void setComputeTask(DaxNode computeTask) {
		this.computeTask = computeTask;
		this.isEmptyTimeSlot = false;
	}

	public Double getStartTime() {
		return startTime;
	}

	public void setStartTime(Double startTime) {
		this.startTime = startTime;
	}

	public Double getActualRunTime() {
		return actualRunTime;
	}

	public Double getFinishTime() {
		return finishTime;
	}
	
	public Double getComputeTime() {
		return computeTime;
	}

	public void setComputeTime(Double computeTime) {
		this.computeTime = computeTime;
		computeActualRuntime();
	}

	public void setCommunicationTasks(List<CommunicationTask> commuTaskList) {
		this.commuTaskList.addAll(commuTaskList);
		assessCommunicationTasks();
	}
	
	private void assessCommunicationTasks() {		
		if(this.commuTaskList.isEmpty()) {
			this.localCommuTask = 0;
			this.remoteCommuTask = 0;
			this.commuTime = 0.000;
		} else {			
			this.localCommuTask = (int) this.commuTaskList.stream().filter(t -> t.isLocalFile() == true).count();
			this.remoteCommuTask = (int) this.commuTaskList.stream().filter(t -> t.isLocalFile() == false).count();
			this.commuTime = this.commuTaskList.stream().map(t -> t.getCommunicatonTime())
												.mapToDouble(Double::doubleValue).sum();
		}
		computeActualRuntime();
	}
	
	public List<CommunicationTask> getCommunicationTaskList(){
		return this.commuTaskList;
	}
	
	public int getLocalCommunicationTask() {
		return localCommuTask;
	}

	public int getRemoteCommunicationTask() {
		return remoteCommuTask;
	}

	public Double getCommunicationTime() {
		return commuTime;
	}
	
	public String getComputeId() {
		return this.computeTask.getId();
	}

	@Override
	public String toString() {
		return String.format("%10s", isEmptyTimeSlot ? "-" : computeTask.getId());
		// return isEmptyTimeSlot ? String.format("%7s", "-") : computeTask.getId();
	}
	
}
