package com.kmutt.sit.workflow.component;

public class CommunicationTask {

	private String fileName;
	private Double fileSize;
	private int sourceVmId;
	private int targetVmId;
	private Double commuTime;
	
	private boolean isLocalFile;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Double getFileSize() {
		return fileSize;
	}
	public void setFileSize(Double fileSize) {
		this.fileSize = fileSize;
	}
	public int getSourceVmId() {
		return sourceVmId;
	}
	public void setSourceVmId(int sourceVmId) {
		this.sourceVmId = sourceVmId;
	}
	public int getTargetVmId() {
		return targetVmId;
	}
	public void setTargetVmId(int targetVmId) {
		this.targetVmId = targetVmId;
	}
	public Double getCommunicatonTime() {
		return commuTime;
	}
	public void setCommunicationTime(Double commuTime) {
		this.commuTime = commuTime;
	}
	public boolean isLocalFile() {
		return isLocalFile;
	}
	public void setLocalFile(boolean isLocalFile) {
		this.isLocalFile = isLocalFile;
	}
	
}
