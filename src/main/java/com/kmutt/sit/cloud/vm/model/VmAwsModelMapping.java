package com.kmutt.sit.cloud.vm.model;

import java.util.HashMap;
import java.util.Map;

public class VmAwsModelMapping {
	
	final private static int NO_OF_TYPE = 6;
	
	private static Map<Integer, VmModel> mapping;
	
	static {
		mapping = new HashMap<Integer, VmModel>();
		
		mapping.put(0, new VmModel(VmModel.VmType.M1_SMALL, 0.06, 26.0));
		mapping.put(1, new VmModel(VmModel.VmType.M1_MEDIUM, 0.12, 13.0));
		mapping.put(2, new VmModel(VmModel.VmType.M1_LARGE, 0.24, 6.50));
		mapping.put(3, new VmModel(VmModel.VmType.M1_XLARGE, 0.48, 3.25));
		mapping.put(4, new VmModel(VmModel.VmType.M3_XLAGE, 0.50, 2.00));
		mapping.put(5, new VmModel(VmModel.VmType.M3_DOUBLEXLARGE, 1.00, 1.00));
	}
	
	public static VmModel getVmModel(Integer key) {
		return mapping.get(key);
	}
	
	public static int getNoOfTypes() {
		return NO_OF_TYPE;
	}
	
	public static Integer getCostExtremeType() {
		return 0;
	}
	
	public static Integer getMakespanExtremeType() {
		return 5;
	}

}
