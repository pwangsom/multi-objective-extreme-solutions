package com.kmutt.sit.cloud.vm.model;

import java.util.HashMap;

public class AwsProviderModel extends GeneralProviderModel {
	
	public AwsProviderModel() {		
		// TODO Auto-generated constructor stub
		this(0.0, 0.0);
	}

	public AwsProviderModel(Double vmBootTime, Double vmDegrade) {		
		super(6, 0, 5);
		// TODO Auto-generated constructor stub		

		super.setProviderModelType(ProviderModelType.AWS);
		
		vmMapping = new HashMap<Integer, VmModel>();
		
		vmMapping.put(0, new VmModel(VmModel.VmType.M1_SMALL, 0.06, 26.0, vmBootTime, vmDegrade));
		vmMapping.put(1, new VmModel(VmModel.VmType.M1_MEDIUM, 0.12, 13.0, vmBootTime, vmDegrade));
		vmMapping.put(2, new VmModel(VmModel.VmType.M1_LARGE, 0.24, 6.50, vmBootTime, vmDegrade));
		vmMapping.put(3, new VmModel(VmModel.VmType.M1_XLARGE, 0.48, 3.25, vmBootTime, vmDegrade));
		vmMapping.put(4, new VmModel(VmModel.VmType.M3_XLAGE, 0.50, 2.00, vmBootTime, vmDegrade));
		vmMapping.put(5, new VmModel(VmModel.VmType.M3_DOUBLEXLARGE, 1.00, 1.00, vmBootTime, vmDegrade));
		
		super.setVmMapping(vmMapping);
		super.setVmBootTime(vmBootTime);
		super.setVmDegradation(vmDegrade);
	}

}
