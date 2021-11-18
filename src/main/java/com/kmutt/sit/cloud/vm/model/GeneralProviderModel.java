package com.kmutt.sit.cloud.vm.model;

import java.util.Map;

public class GeneralProviderModel implements ProviderModelInterface {
	
	final private int NO_OF_TYPE;
	final private int COST_EXTREME;
	final private int MAKESPAN_EXTREME;
	
	protected Map<Integer, VmModel> vmMapping;
	protected Double vmBootTime = 0.0;
	protected Double vmDegradation = 0.0;
	
	protected ProviderModelType providerType;
	
	public GeneralProviderModel(int noOfTypes, int costExtreme, int makespanExtreme) {
		this.NO_OF_TYPE = noOfTypes;
		this.COST_EXTREME = costExtreme;
		this.MAKESPAN_EXTREME = makespanExtreme;
	}
	
	protected void setVmMapping(Map<Integer, VmModel> vmMapping) {
		this.vmMapping = vmMapping;
	}

	@Override
	public VmModel getVmModel(Integer key) {
		// TODO Auto-generated method stub
		return this.vmMapping.get(key);
	}

	@Override
	public int getNoOfTypes() {
		// TODO Auto-generated method stub
		return this.NO_OF_TYPE;
	}

	@Override
	public Integer getCostExtremeType() {
		// TODO Auto-generated method stub
		return this.COST_EXTREME;
	}

	@Override
	public Integer getMakespanExtremeType() {
		// TODO Auto-generated method stub
		return this.MAKESPAN_EXTREME;
	}

	@Override
	public Double getVmDegradation() {
		// TODO Auto-generated method stub
		return this.vmDegradation;
	}

	@Override
	public void setVmDegradation(Double vmDegradation) {
		// TODO Auto-generated method stub
		this.vmDegradation = vmDegradation;
	}

	@Override
	public Double getVmBootTime() {
		// TODO Auto-generated method stub
		return this.vmBootTime;
	}

	@Override
	public void setVmBootTime(Double vmBootTime) {
		// TODO Auto-generated method stub
		this.vmBootTime = vmBootTime;
	}

	@Override
	public ProviderModelType getProviderModelType() {
		// TODO Auto-generated method stub
		return this.providerType;
	}
	
	protected void setProviderModelType(ProviderModelType providerType) {
		this.providerType = providerType;
	}

}
