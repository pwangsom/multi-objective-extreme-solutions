package com.kmutt.sit.cloud.vm.model;

import java.util.HashMap;

public class GceProviderModel extends GeneralProviderModel {
	
	public GceProviderModel() {		
		// TODO Auto-generated constructor stub
		this(0.0, 0.0);
	}
	

	public GceProviderModel(Double vmBootTime, Double vmDegrade) {		
		super(6, 0, 5);
		// TODO Auto-generated constructor stub
		
		super.setProviderModelType(ProviderModelType.GCE);
		
		vmMapping = new HashMap<Integer, VmModel>();
		
		vmMapping.put(0, new VmModel(VmModel.VmType.F1_MICRO, 0.009, 40.0, vmBootTime, vmDegrade));
		vmMapping.put(1, new VmModel(VmModel.VmType.G1_SMALL, 0.032, 16.0, vmBootTime, vmDegrade));
		vmMapping.put(2, new VmModel(VmModel.VmType.N1_STANDARD_1, 0.061, 8.0, vmBootTime, vmDegrade));
		vmMapping.put(3, new VmModel(VmModel.VmType.N1_STANDARD_2, 0.122, 4.0, vmBootTime, vmDegrade));
		vmMapping.put(4, new VmModel(VmModel.VmType.N1_STANDARD_4, 0.244, 2.0, vmBootTime, vmDegrade));
		vmMapping.put(5, new VmModel(VmModel.VmType.N1_STANDARD_8, 0.488, 1.0, vmBootTime, vmDegrade));
		
		super.setVmMapping(vmMapping);
		super.setVmBootTime(vmBootTime);
		super.setVmDegradation(vmDegrade);
	}
}
