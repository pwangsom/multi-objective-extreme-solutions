package com.kmutt.sit.cloud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.kmutt.sit.pegasus.dax.DaxGraph;
import com.kmutt.sit.pegasus.dax.DaxNode;
import com.kmutt.sit.utils.JavaHelper;

public class CloudScheduling {	

	private Logger logger = Logger.getLogger(CloudScheduling.class);

	
	public enum SchedulingType {
		FIFO, MIN, MAX;
	}
	
	
	final private SchedulingType TYPE;

	private DaxGraph workflow;
	private CloudEnvironment cloud;
	private int eventId = 0;
		
	private double globalTime = 0.00;
	
	public CloudScheduling(SchedulingType type) {
		this.TYPE = type;
	}
	
	public void setWorkflow(DaxGraph workflow) {
		this.workflow = workflow;
	}
	
	public void intialCloudEnvironment(int noOfVm) {
		this.cloud = new CloudEnvironment(noOfVm);
		this.cloud.startVms();
	}
	
	public void printSchedulingReport() {
		logger.info("======Scheduling Report=======");
		logger.debug("");
		logger.info("Workflow          : " + this.workflow.getDaxNameSpace());
		logger.info("No of Task        : " + this.workflow.getDaxNodes().size());
		logger.info("Workflow Cluster  : " + this.workflow.getDaxClusterType());
		logger.info("No of VM          : " + cloud.getNoOfVm());
		logger.info("Scheduling Type   : " + this.TYPE);
		logger.info("Completion Time   : " + String.format("%-10.2f", cloud.getCompletionTime()));
		logger.info("VM Running Time   : " + String.format("%-10.2f", cloud.getVmRunningTime()));
		logger.info("VM Idle Time      : " + String.format("%-10.2f", cloud.getVmIdleTime()));
		logger.info("Task Waiting Time : " + String.format("%-10.2f", cloud.getTaskWaitingTime()));
		logger.info("No Of Comm. Task  : ");
		logger.info("Communication Time: ");
		
	}
	
	public void scheduleWorkflow() {		
		List<List<DaxNode>> workflowLevel = this.workflow.getNodeLevel();
		
		for(List<DaxNode> taskList : workflowLevel) {
			scheduleEachTask(getSortedTaskList(taskList));
		}	
		
		this.globalTime = cloud.getLastNextAvailableTime();
		this.cloud.stopVms(this.globalTime);
	}
	
	private void scheduleEachTask(List<DaxNode> taskList) {
		
		for(DaxNode task : taskList) {
			
			if(task.isNotFinish()) {
				
				boolean isNotSubmitted = true;
				
				while(isNotSubmitted) {										
					
					List<CloudVm> avVmList = cloud.getAvailableVmList(this.globalTime);
					
					if(avVmList.isEmpty()) {
						
						this.globalTime = cloud.getNextAvailableTime();
						
					} else {
						
						CloudVm assignVm = getCloudVm(avVmList, false);
						
						evaluateCloudEvent(assignVm, task, this.globalTime);

						this.globalTime = cloud.getNextAvailableTime();
						
						isNotSubmitted = false;
					}					
				}	
				
			}
			
		}
	}
	
	private void evaluateCloudEvent(CloudVm vm, DaxNode task, Double submittedTime) {				
		if(task.getClusterId().equalsIgnoreCase(DaxNode.NULL_CLUSTER)) {
			evaluateCloudSingleEvent(vm, task, submittedTime);
		} else {
			evaluateCloudClusterEvent(vm, task.getClusterId(), submittedTime);
		}
	}
	
	private void evaluateCloudSingleEvent(CloudVm vm, DaxNode task, Double submittedTime) {
		
		task.setVmId(vm.getVmId());		
		task.setSubmittedTime(submittedTime);
		
		Double lastParentFinishTime = 0.00;
				
		if(!task.getParents().isEmpty()) {			
			
			List<DaxNode> parents = task.getParents().entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
			
			Comparator<DaxNode> comparator = Comparator.comparing(DaxNode::getFinishedTime);
			DaxNode maxParent = parents.stream().max(comparator).get();
			
			lastParentFinishTime = maxParent.getFinishedTime();			
		}
		
		Double canStartTime = Arrays.asList(submittedTime, lastParentFinishTime, vm.getNextAvailableTime())
								.stream().max(Double::compare).get();
		
		task.setLastParentFinishTime(lastParentFinishTime);
		task.setCanStartTime(canStartTime);		
		task.setWaitingTime();
		task.setFinishedTime(task.getCanStartTime() + task.getRuntimeDouble());
		task.setNotFinish(false);	
		
		vm.recieveTask(task);
		
		printSubmitTaskEvent(true, vm, task);
	}
	
	// Beware of the submitted time in cluster
	
	private void evaluateCloudClusterEvent(CloudVm vm, String clusterId, Double submittedTime) {
		
		List<DaxNode> taskList = workflow.getClusteredTaskByClusterId(clusterId);
		
		vm.submitCluster(submittedTime, clusterId);
		
		List<Integer> levelIdList = taskList.stream().map(m -> m.getLevelId()).distinct().collect(Collectors.toList());
		
		if(levelIdList.size() > 1) {
			taskList = taskList.stream().sorted(Comparator.comparing(DaxNode::getLevelId)).collect(Collectors.toList());;
		} else {
			taskList = getSortedTaskList(taskList);
		}
		
		for(DaxNode task : taskList) {
			evaluateCloudSingleEvent(vm, task, submittedTime);
		}
		
		vm.releaseCluster(vm.getNextAvailableTime(), clusterId);
	}
	
	public void printSubmitTaskEvent(boolean normalMode, CloudVm vm, DaxNode task) {
		
		this.eventId++;
		
		if(normalMode) {
			
			logger.debug("[ScheduleID: " + String.format("%4d", this.eventId)
					+ ", Time: " +  JavaHelper.getTimeDouble(this.globalTime) 
					+ ", VM ID: " + String.format("%2d", vm.getVmId())
					+ ", TaskID: " + task.getId()
					+ ", ClusterID: " + task.getClusterId()
					+ ", Submit: " + JavaHelper.getTimeDouble(task.getSubmittedTime())
					+ ", Parent: " + JavaHelper.getTimeDouble(task.getLastParentFinishTime())
					+ ", Start " + JavaHelper.getTimeDouble(task.getCanStartTime())
					+ ", Runtime: " + JavaHelper.getTimeDouble(task.getRuntimeDouble())
					+ ", Finish: " + JavaHelper.getTimeDouble(task.getFinishedTime())
					+ ", Wait: " + JavaHelper.getTimeDouble(task.getWaitingTime())
					+ "]");
			
		} else {
			printSubmitTaskEventCsv(vm, task);
		}
	}
	
	public void printSubmitTaskEventCsv(CloudVm vm, DaxNode task) {
		
		logger.debug(String.format("%4d", this.eventId)
				+ "," + JavaHelper.getTimeDouble(this.globalTime) 
				+ "," + String.format("%2d", vm.getVmId())
				+ "," + task.getId()
				+ "," + task.getClusterId()
				+ "," + JavaHelper.getTimeDouble(task.getSubmittedTime())
				+ "," + JavaHelper.getTimeDouble(task.getCanStartTime())
				+ "," + JavaHelper.getTimeDouble(task.getRuntimeDouble())
				+ "," + JavaHelper.getTimeDouble(task.getFinishedTime())
				+ "," + JavaHelper.getTimeDouble(task.getCanStartTime() - task.getSubmittedTime())
				);
	}
	
	public void printCloudEvent() {
		logger.info("======Cloud Event List=======");
		logger.debug("");
		
		this.cloud.getCloudEventListSortedByTimeThenEventId().stream()
			.forEach(ev -> logger.info(ev.toString()));
	}
	
	private CloudVm getCloudVm(List<CloudVm> avVmList, boolean mode) {
		if(mode) {
			Random r = new Random();
			return avVmList.get(r.nextInt(avVmList.size()));
		} else {
			return avVmList.get(0);
		}
	}
	
	private List<DaxNode> getSortedTaskList(List<DaxNode> taskList){
		
		List<DaxNode> result;
		
		Comparator<DaxNode> comparator = Comparator.comparing(DaxNode::getRuntimeDouble);
		
		switch (this.TYPE) {
		case MIN:
			result = taskList.stream().sorted(comparator).collect(Collectors.toList());
			break;
		case MAX:
			result = taskList.stream().sorted(comparator.reversed()).collect(Collectors.toList());
			break;
		default:
			result = taskList;
			break;
		}
		
		return result;
		
	}
	
	public void printCloudVmAction() {
		
		printCloudVmActionFirstLine();
		
		List<Double> listOfActionTime = this.cloud.getCloudVmActionListSortedByActionTime().stream().map(m -> m.getActionTime())
											.distinct().sorted(Comparator.comparingDouble(Double::doubleValue))
											.collect(Collectors.toList());
		
		List<CloudVmAction> listOfAction = this.cloud.getCloudVmActionList();
		
		for(Double d : listOfActionTime) {
			
			List<List<String>> listByVmId = new ArrayList<List<String>>();
			int max = 0;
			
			for (int i = 0; i < this.cloud.getNoOfVm(); i++) {
				
				final Integer c = i;
				
				List<String> list = new ArrayList<String>();
				
				list = listOfAction.stream()
							.filter(ac -> Double.compare(ac.getActionTime(), d) == 0 && ac.getVmId() == c)
							.sorted(Comparator.comparingInt(CloudVmAction::getVmActionOrder))
							.map(CloudVmAction::getActionLog)
							.collect(Collectors.toList());
				
				listByVmId.add(list);
				
				max = list.size() > max ? list.size() : max;
			}
			
			for(int m = 0; m < max; m++) {
				
				List<String> lines = new ArrayList<String>();

				lines.add(String.format("|%13.2f|", d));
				
				for(int i = 0; i < this.cloud.getNoOfVm(); i++) {
					
					String log = "";
					
					if(!listByVmId.get(i).isEmpty()) {
						if(m < listByVmId.get(i).size()) {
							log = listByVmId.get(i).get(m);
						}
					}
					
					lines.add(String.format("%20s|", log));
				}
				
				String line = lines.stream().collect(Collectors.joining());
				logger.info(line);
			}			
		}
		
	}
	
	private void printCloudVmActionFirstLine() {

		logger.info("======Cloud Vm Action List=======");
		logger.debug("");
		
		List<String> firstLine = new ArrayList<String>();
		
		firstLine.add(String.format("|%2sAction Time|", ""));
		
		this.cloud.getVmList().stream().map(vm -> vm.getVmId())
				.forEach(s -> {
					String str = String.format("%11sVM ID{%02d}|", "", s);
					firstLine.add(str);
				});
		
		String line = firstLine.stream().collect(Collectors.joining());
		
		logger.info(line);
		logger.info(new String(new char[line.length()]).replace("\0", "="));
		
	}
}
