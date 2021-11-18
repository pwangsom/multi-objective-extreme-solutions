package com.kmutt.sit.cloud.vm.model;

public class VmModel {
	
	public enum VmType {		
		M1_SMALL, M1_MEDIUM, M1_LARGE,
		M1_XLARGE, M3_XLAGE, M3_DOUBLEXLARGE,
		F1_MICRO, G1_SMALL,
		N1_STANDARD_1, N1_STANDARD_2,
		N1_STANDARD_4, N1_STANDARD_8;
	}
	
	private VmModel.VmType type;
	private Double cost;
	private Double slowdownRatio;
	
	private Double vmBootTime = 0.0;
	private Double vmDegradation = 0.0;
	
	public VmModel(VmModel.VmType type, Double cost, Double slowdownRatio, Double vmBootTime, Double vmDegrade) {
		this(type, cost, slowdownRatio);
		this.vmBootTime = vmBootTime;
		this.vmDegradation = vmDegrade;
	}
	
	public VmModel(VmModel.VmType type, Double cost, Double slowdownRatio) {
		this.type = type;
		this.cost = cost;
		this.slowdownRatio = slowdownRatio;
	}
	
	public VmModel.VmType getType() {
		return type;
	}
	public void setType(VmModel.VmType type) {
		this.type = type;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public Double getSlowdownRatio() {
		return slowdownRatio;
	}
	public void setSlowdownRatio(Double slowdownRatio) {
		this.slowdownRatio = slowdownRatio;
	}

	public Double getVmBootTime() {
		return vmBootTime;
	}

	public void setVmBootTime(Double vmBootTime) {
		this.vmBootTime = vmBootTime;
	}

	public Double getVmDegradation() {
		return vmDegradation;
	}

	public void setVmDegradation(Double vmDegradation) {
		this.vmDegradation = vmDegradation;
	}

}
