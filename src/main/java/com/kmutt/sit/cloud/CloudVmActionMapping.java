package com.kmutt.sit.cloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CloudVmActionMapping {
	
	private static Map<CloudVmAction.VmActionType, CloudVmAction.VmActionType> vmActionMapping;
	private static List<CloudVmAction.VmActionType> startVmActionList;
	private static List<CloudVmAction.VmActionType> stopVmActionList;
	
	static {
		vmActionMapping = new HashMap<CloudVmAction.VmActionType, CloudVmAction.VmActionType>();
		
		vmActionMapping.put(CloudVmAction.VmActionType.STOP_VM, CloudVmAction.VmActionType.START_VM);
		vmActionMapping.put(CloudVmAction.VmActionType.RELEASE_TASK, CloudVmAction.VmActionType.SUBMIT_TASK);
		vmActionMapping.put(CloudVmAction.VmActionType.STOP_IDLE_VM, CloudVmAction.VmActionType.START_IDLE_VM);
		vmActionMapping.put(CloudVmAction.VmActionType.STOP_WAITING_PROCESS, CloudVmAction.VmActionType.START_WAITING_PROCESS);
		vmActionMapping.put(CloudVmAction.VmActionType.STOP_PROCESS_TASK, CloudVmAction.VmActionType.START_PROCESS_TASK);
		vmActionMapping.put(CloudVmAction.VmActionType.RECEIVE_FILE, CloudVmAction.VmActionType.REQUEST_FILE);
		vmActionMapping.put(CloudVmAction.VmActionType.RELEASE_CLUSTER, CloudVmAction.VmActionType.SUBMIT_CLUSTER);
		
		startVmActionList = new ArrayList<CloudVmAction.VmActionType>();
		startVmActionList.add(CloudVmAction.VmActionType.START_VM);
		startVmActionList.add(CloudVmAction.VmActionType.SUBMIT_TASK);
		startVmActionList.add(CloudVmAction.VmActionType.START_IDLE_VM);
		startVmActionList.add(CloudVmAction.VmActionType.START_WAITING_PROCESS);
		startVmActionList.add(CloudVmAction.VmActionType.START_PROCESS_TASK);
		startVmActionList.add(CloudVmAction.VmActionType.REQUEST_FILE);
		startVmActionList.add(CloudVmAction.VmActionType.SUBMIT_CLUSTER);

		stopVmActionList = new ArrayList<CloudVmAction.VmActionType>();
		stopVmActionList.add(CloudVmAction.VmActionType.STOP_VM);
		stopVmActionList.add(CloudVmAction.VmActionType.RELEASE_TASK);
		stopVmActionList.add(CloudVmAction.VmActionType.STOP_IDLE_VM);
		stopVmActionList.add(CloudVmAction.VmActionType.STOP_WAITING_PROCESS);
		stopVmActionList.add(CloudVmAction.VmActionType.STOP_PROCESS_TASK);
		stopVmActionList.add(CloudVmAction.VmActionType.RECEIVE_FILE);
		stopVmActionList.add(CloudVmAction.VmActionType.RELEASE_CLUSTER);
	}
	
	public static boolean isValidActionMapping(CloudVmAction.VmActionType key, CloudVmAction.VmActionType value) {
		return value == vmActionMapping.get(key);
	}
	
	public static boolean isStartVmAction(CloudVmAction.VmActionType startAction) {
		return startVmActionList.contains(startAction);
	}
	
	public static boolean isStopVmAction(CloudVmAction.VmActionType stopAction) {
		return stopVmActionList.contains(stopAction);
	}
}
