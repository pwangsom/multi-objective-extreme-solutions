package com.kmutt.sit.cloud;

import com.kmutt.sit.utils.JavaHelper;

public class CloudEvent {
	
	public static enum EventType{
		VM_IDLE, PROCESS_TASK, TASK_WAITING, ACCESS_FILE, UPLOAD_FILE
	}

	private int eventId;
	private Integer vmId;
	private String taskId;
	private String fileName;
	private Double startEvent;
	private Double finishEvent;
	
	// this is amount of time
	private Double eventTime;
	
	private CloudEvent.EventType type;

	public CloudEvent(CloudEvent.EventType type, Integer vmId, String taskId, Double startEvent, Double finishEvent, Double eventTime) {
		this.type = type;
		this.vmId = vmId;
		this.taskId = taskId;
		this.startEvent = startEvent;
		this.finishEvent = finishEvent;
		this.eventTime = eventTime;
	}

	public CloudEvent(int eventId, CloudEvent.EventType type, Integer vmId, String taskId, Double startEvent, Double finishEvent, Double eventTime) {
		this(type, vmId, taskId, startEvent, finishEvent, eventTime);
		this.eventId = eventId;
	}
	
	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public Integer getVmId() {
		return vmId;
	}

	public void setVmId(Integer vmId) {
		this.vmId = vmId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Double getStartEvent() {
		return startEvent;
	}

	public void setStartEvent(Double startEvent) {
		this.startEvent = startEvent;
	}

	public Double getFinishEvent() {
		return finishEvent;
	}

	public void setFinishEvent(Double finishEvent) {
		this.finishEvent = finishEvent;
	}

	public Double getEventTime() {
		return eventTime;
	}

	public void setEventTime(Double eventTime) {
		this.eventTime = eventTime;
	}
	
	public CloudEvent.EventType getType() {
		return type;
	}

	public void setType(CloudEvent.EventType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "["
				+ "Event: " + String.format("%4d", this.getEventId())
				+ ", Type: " + String.format("%16s", this.getType())
				+ ", VM ID: " + this.getVmId()
				+ ", Task ID: " + this.getTaskId()
				// + ", Filename: " + this.getFileName()
				+ ", Start: " + JavaHelper.getTimeDouble(this.getStartEvent())
				+ ", Finish: " + JavaHelper.getTimeDouble(this.getFinishEvent())
				+ ", Total: " + JavaHelper.getTimeDouble(this.getEventTime())
				+ "]";
	}
}
