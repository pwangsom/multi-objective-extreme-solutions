package com.kmutt.sit.cloud;

import java.util.LinkedList;

import org.apache.log4j.Logger;

public class CloudVmActionManager {

	private Logger logger = Logger.getLogger(CloudVmActionManager.class);

	private LinkedList<CloudVmAction> actionList;
	
	private CloudVm vm;

	public CloudVmActionManager() {
		actionList = new LinkedList<CloudVmAction>();
	}
	
	public void setCloudVm(CloudVm vm) {
		this.vm = vm;
	}
	
	public void startVm(Double requestTime) {
		addStartVmAction(CloudVmAction.VmActionType.START_VM, vm.getVmId(), 
				requestTime, "+VM", vm.getVmActionOrder());
	}
	
	public void stopVm(Double requestTime) {
		mappingStartStopVmAction(CloudVmAction.VmActionType.STOP_VM, vm.getVmId(), 
				requestTime, "-VM", vm.getVmActionOrder());
	}
	
	public void submitCluster(Double requestTime, String clusterId) {
		addStartVmAction(CloudVmAction.VmActionType.SUBMIT_CLUSTER, vm.getVmId(), 
				requestTime, "+CLUS:" + clusterId, vm.getVmActionOrder());
	}
	
	public void releaseCluster(Double requestTime, String clusterId) {
		mappingStartStopVmAction(CloudVmAction.VmActionType.RELEASE_CLUSTER, vm.getVmId(), 
				requestTime, "-CLUS:" + clusterId, vm.getVmActionOrder());
	}
	
	public void submitTask(Double requestTime, String taskId) {
		addStartVmAction(CloudVmAction.VmActionType.SUBMIT_TASK, vm.getVmId(), 
				requestTime, "+TASK:" + taskId, vm.getVmActionOrder());
	}
	
	public void releaseTask(Double requestTime, String taskId) {
		mappingStartStopVmAction(CloudVmAction.VmActionType.RELEASE_TASK, vm.getVmId(), 
				requestTime, "-TASK:" + taskId, vm.getVmActionOrder());
	}
	
	public void startIdleVm(Double requestTime, String taskId) {
		addStartVmAction(CloudVmAction.VmActionType.START_IDLE_VM, vm.getVmId(), 
				requestTime, "+IDLE:" + taskId, vm.getVmActionOrder());
	}
	
	public void stopIdleVm(Double requestTime, String taskId) {
		mappingStartStopVmAction(CloudVmAction.VmActionType.STOP_IDLE_VM, vm.getVmId(), 
				requestTime, "-IDLE:" + taskId, vm.getVmActionOrder());
	}
	
	public void startWaitProcessTask(Double requestTime, String taskId) {
		addStartVmAction(CloudVmAction.VmActionType.START_WAITING_PROCESS, vm.getVmId(), 
				requestTime, "+WAIT:" + taskId, vm.getVmActionOrder());
	}
	
	public void stoptWaitProcessTask(Double requestTime, String taskId) {
		mappingStartStopVmAction(CloudVmAction.VmActionType.STOP_WAITING_PROCESS, vm.getVmId(), 
				requestTime, "-WAIT:" + taskId, vm.getVmActionOrder());
	}
	
	public void startProcessTask(Double requestTime, String taskId) {
		addStartVmAction(CloudVmAction.VmActionType.START_PROCESS_TASK, vm.getVmId(), 
				requestTime, "+PROC:" + taskId, vm.getVmActionOrder());
	}
	
	public void stoptProcessTask(Double requestTime, String taskId) {
		mappingStartStopVmAction(CloudVmAction.VmActionType.STOP_PROCESS_TASK, vm.getVmId(), 
				requestTime, "-PROC:" + taskId, vm.getVmActionOrder());
	}

	private boolean addStartVmAction(CloudVmAction.VmActionType actionType, Integer actionVmId, Double actionTime,
			String actionLog, Integer vmActionOrder) {

		boolean result = false;

		if (CloudVmActionMapping.isStartVmAction(actionType)) {

			CloudVmAction newStartAction = new CloudVmAction();

			newStartAction.setActionId(CloudVmActionObserver.getCloudVmActionObserver().getVmActionId());
			newStartAction.setActionType(actionType);
			newStartAction.setActionType(actionType);
			newStartAction.setVmId(actionVmId);
			newStartAction.setActionTime(actionTime);
			newStartAction.setActionLog(actionLog);
			newStartAction.setVmActionOrder(vmActionOrder);

			actionList.addFirst(newStartAction);
			
			logger.debug("VM {" + actionVmId + "} -> " + actionList.toString());

			result = true;
		} else {
			logger.error("Action Type is not Start Action: " + actionType);
		}

		return result;
	}

	private boolean mappingStartStopVmAction(CloudVmAction.VmActionType actionType, Integer actionVmId,
			Double actionTime, String actionLog, Integer vmActionOrder) {

		boolean result = false;

		if (CloudVmActionMapping.isStopVmAction(actionType)
				&& CloudVmActionMapping.isValidActionMapping(actionType, actionList.getFirst().getActionType())) {

			CloudVmAction startAction = actionList.getFirst();

			CloudVmAction stopAction = new CloudVmAction();

			stopAction.setActionId(startAction.getActionId());
			stopAction.setActionType(actionType);
			stopAction.setActionType(actionType);
			stopAction.setVmId(actionVmId);
			stopAction.setActionTime(actionTime);
			stopAction.setActionLog(actionLog);
			stopAction.setVmActionOrder(vmActionOrder);

			CloudVmCompletedAction doneAction = new CloudVmCompletedAction();
			doneAction.setStartAction(startAction);
			doneAction.setStopAction(stopAction);

			CloudVmActionObserver.getCloudVmActionObserver().addCloudVmCompletedAction(doneAction);

			logger.debug("VM {" + actionVmId + "} -> {"  + stopAction.toString() + "} -> " + actionList.toString());

			actionList.removeFirst();

			result = true;
		} else {
			logger.error("Mapping Stop Action failed: {" + actionType + ", " + actionList.toString() + "}");
		}

		return result;
	}

}
