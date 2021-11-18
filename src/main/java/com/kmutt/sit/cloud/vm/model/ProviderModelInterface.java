package com.kmutt.sit.cloud.vm.model;

public interface ProviderModelInterface {
	public ProviderModelType getProviderModelType();
	public Double getVmBootTime();
	public void setVmBootTime(Double vmBootTime);
	public Double getVmDegradation();
	public void setVmDegradation(Double vmDegradation);
	public VmModel getVmModel(Integer key);	
	public int getNoOfTypes();	
	public Integer getCostExtremeType();	
	public Integer getMakespanExtremeType();
}
