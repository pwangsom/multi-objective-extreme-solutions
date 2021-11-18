package com.kmutt.sit.mop.runner.scheduling.planner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.uma.jmetal.solution.IntegerSolution;

import com.kmutt.sit.cloud.vm.model.ProviderModelInterface;
import com.kmutt.sit.mop.manager.scheduling.DagSchedulingConfiguration;
import com.kmutt.sit.pegasus.dax.DaxNode;
import com.kmutt.sit.pegasus.dax.DaxNodeType;
import com.kmutt.sit.workflow.GeneralClusterWorkflow;
import com.kmutt.sit.workflow.component.PlanedVm;
import com.kmutt.sit.workflow.component.TaskGroup;
import com.kmutt.sit.workflow.component.TimeSlot;

public class DagWorkflowPlanner {

	private Logger logger = Logger.getLogger(DagWorkflowPlanner.class);
	
	private GeneralClusterWorkflow workflow;
	
	private IntegerSolution solution;
	private List<Integer> solutionList;
	private List<Integer> vmIdList;
	
	private Map<Integer, PlanedVm> vmMap;

	private Map<Integer, Integer> taskVmMap;
	
	// intend to keep the node that finished only
	private Map<String, Integer> finishedNodeVmMap;
	
	private CommunicationPlanner commuPlanner;
	
	private DagSchedulingConfiguration config;
	private ProviderModelInterface vmModel;
		
	public DagWorkflowPlanner(GeneralClusterWorkflow workflow, IntegerSolution solution, DagSchedulingConfiguration config) {
		this(workflow, solution);
		this.config = config;
		this.vmModel = this.config.getProviderModel();
		this.commuPlanner.setNetworkDegradation(this.config.getNetworkDegradation());
	}	
	
	public DagWorkflowPlanner(GeneralClusterWorkflow workflow, IntegerSolution solution) {
		this.workflow = workflow;
		this.solution = solution;
		this.solutionList = new ArrayList<Integer>();
		this.vmIdList = new ArrayList<Integer>();
		this.vmMap = new TreeMap<Integer, PlanedVm>();
		this.taskVmMap = new HashMap<Integer, Integer>();
		this.finishedNodeVmMap = new HashMap<String, Integer>();
		this.commuPlanner = new CommunicationPlanner();
	}	

	public Map<Integer, PlanedVm> getSchedulePlan() {
		return vmMap;
	}
	
	public void planning() {		
		setSolutionList();
		setVmMapList();
		putSolutionToVmMap();
		putTimeSlotToVmByNode();
		trimTimeSlot();
		
		if(logger.isDebugEnabled()) printSchedulingPlan();
	}
	
	private void trimTimeSlot() {
		List<Integer> indexList = new ArrayList<Integer>();
		
		this.vmMap.entrySet().stream().map(m -> m.getValue())
					.map(vm -> vm.getLastIndexOfNotNullTimeSlot())
					.forEach(index -> indexList.add(index));
		

		// max index of time slot of all VM
		// increase to point the index of unnecessary time slot 
		final int max = Collections.max(indexList) + 1;		

		// sublist function has 2 parameters
		// from index (inclusion) and to index (exclusion)
		this.vmMap.entrySet().stream().map(m -> m.getValue())
					.forEach(vm -> {
						List<TimeSlot> newList = vm.getTimeList().subList(0, max);
						vm.setTimeList(newList);
					});
				
	}
	
	@SuppressWarnings("unused")
	private void putTimeSlotToVmByTask() {
		
		boolean isAllTaskNotFinish = true;
		
		while(isAllTaskNotFinish) {
			this.workflow.getTaskGroupSortedByTopologyOrder().stream().filter(f -> f.isNotFinish() == true)
							.collect(Collectors.toList()).stream().forEach(task -> {
								
								putTimeSlotOfEachTask(task);
								
							});
			
			if(this.workflow.getTaskGroupSortedByTopologyOrder().stream().filter(f -> f.isNotFinish() == true).count() == 0) {
				isAllTaskNotFinish = false;
			}
		}
	}
	
	private void putTimeSlotToVmByNode() {
		
		boolean isAllNodesNotFinish = true;
		
		while(isAllNodesNotFinish) {
			this.workflow.getTopologicalOrderNodeList().stream().filter(f -> f.isNotFinish() == true).forEach(node ->{
				putTimeSlotOfEachNode(node);
			});
			
			if(this.workflow.getTopologicalOrderNodeList().stream().filter(f -> f.isNotFinish() == true).count() == 0) {
				isAllNodesNotFinish = false;
			}
		}		
	}
	
	private void putTimeSlotOfEachNode(DaxNode node) {
		
		PlanedVm vm = this.vmMap.get(node.getVmId());
		TaskGroup task = this.workflow.getTaskByNodeId(node.getId());
		
		logger.debug("Node Planing: node id -> " + task.getTaskGroupId() + ", node id -> " + node.getId());
		logger.debug("Before: vm id{" + vm.getVmId() + "} -> " + vm.getTimeSlotIndexList());
				
		if(node.getDaxNodeType() == DaxNodeType.HEAD) {
			setTimeSlotForHeadNode(node, vm);
		} else {
			setTimeSlotForInterEndNode(node, vm);				
		}
		
		logger.debug("After : vm id{" + vm.getVmId() + "} -> " + vm.getTimeSlotIndexList());
		logger.debug("");
		
		if(task.getNodes().stream().filter(f -> f.isNotFinish() == true).count() == 0) {
			task.setNotFinish(false);				
		}	
	}
		
	private void putTimeSlotOfEachTask(TaskGroup task) {
		int vmId = this.taskVmMap.get(task.getTaskGroupId());
		PlanedVm vm = this.vmMap.get(vmId);
		
		boolean isAllNodeFinish = true;
		
		for(DaxNode node : task.getNodes()) {
			
			logger.debug("Task Planing: task id -> " + task.getTaskGroupId() + ", node id -> " + node.getId());
			logger.debug("Before: vm id{" + vmId + "} -> " + vm.getTimeSlotIndexList());
			
			node.setVmId(vmId);
			
			if(node.getDaxNodeType() == DaxNodeType.HEAD) {
				setTimeSlotForHeadNode(node, vm);
			} else {
				setTimeSlotForInterEndNode(node, vm);				
			}
			
			logger.debug("After : vm id{" + vmId + "} -> " + vm.getTimeSlotIndexList());
			
			if(node.isNotFinish()) {
				isAllNodeFinish = false;
			}
		}
		
		if(isAllNodeFinish) {
			task.setNotFinish(false);				
		}	
	}
	
	private void setTimeSlotForInterEndNode(DaxNode node, PlanedVm vm) {
		
		List<TimeSlot> allTimeSlotOfParents = getAllTimeSlotOfParents(node);
		
		if(allTimeSlotOfParents.stream().filter(f -> f.isEmptyTimeSlot() == true).count() == 0) {
			
			TimeSlot vmAvailableTimeSlot = vm.getLastNotNullTimeSlot();
			
			Double computeTime = node.getActualRuntime() * vm.getVmModel().getSlowdownRatio();
			computeTime = computeTime + (computeTime * this.config.getProviderModel().getVmDegradation());
			
			if(!vmAvailableTimeSlot.isEmptyTimeSlot()) {
				allTimeSlotOfParents.add(vmAvailableTimeSlot);
			} else {
				computeTime += vm.getVmModel().getVmBootTime();
			}
			
			TimeSlot maxIndexTimeSlot = allTimeSlotOfParents.stream().max(Comparator.comparingInt(TimeSlot::getTimeIndex)).get();
			TimeSlot maxFinishTimeSlot = allTimeSlotOfParents.stream().max(Comparator.comparingDouble(TimeSlot::getFinishTime)).get();
			
			int index = maxIndexTimeSlot.getTimeIndex()+1;
			
			TimeSlot availableTimeSlot = new TimeSlot();
			availableTimeSlot.setEmptyTimeSlot(false);
			availableTimeSlot.setTimeIndex(index);
			availableTimeSlot.setStartTime(maxFinishTimeSlot.getFinishTime());			
			availableTimeSlot.setCommunicationTasks(commuPlanner.accessInpuFile(node, vm.getVmId()));
			
			availableTimeSlot.setComputeTime(computeTime);
			
			node.setNotFinish(false);
			availableTimeSlot.setComputeTask(node);			
			
			vm.setTimeSlotByIndex(index, availableTimeSlot);
			finishedNodeVmMap.put(node.getId(), vm.getVmId());
			
			commuPlanner.recordOutputFile(node, vm.getVmId());
		}
	}
	
	private List<TimeSlot> getAllTimeSlotOfParents(DaxNode node){		
		// check in the finishNodeVmMap
		List<TimeSlot> result = new ArrayList<TimeSlot>();
		
		node.getParents().keySet().stream().forEach(key ->{
			if(finishedNodeVmMap.containsKey(key)) {					
				TimeSlot time = vmMap.get(finishedNodeVmMap.get(key)).getTimeSlotByNodeId(key);
				result.add(time);
			} else {
				TimeSlot emptyTime = new TimeSlot();
				result.add(emptyTime);
			}
		});
		
		return result;
	}
	
	private void setTimeSlotForHeadNode(DaxNode node, PlanedVm vm) {
		// -1 is there is no not null time slot in list
		int vmAvailableindex = vm.getLastIndexOfNotNullTimeSlot();
		// increase index for the next available time slot
		vmAvailableindex++;
		
		node.setNotFinish(false);
		vm.setTimeSlotForHeadNode(node, vmAvailableindex);
		finishedNodeVmMap.put(node.getId(), vm.getVmId());
		
		commuPlanner.recordOutputFile(node, vm.getVmId());
	}
	
	private void putSolutionToVmMap() {		
		
		// for debug
		
/*		logger.debug(this.workflow.getTopologyOrderList().stream()
						.map(m -> m.getNodes().get(0).getId())
						.collect(Collectors.toList())
				);
		
		logger.debug(this.solutionList);*/
		
		for(int i = 0; i < this.workflow.getTaskGroupSortedByTopologyOrder().size(); i++) {
			vmMap.get(this.solutionList.get(i)).addTask(this.workflow.getTaskGroupSortedByTopologyOrder().get(i));
		}
		
		vmMap.entrySet().stream().map(entry -> entry.getValue()).forEach(vm -> {	
			
			vm.setTopologicalOrderOfNodeList();
			
			vm.getNodeList().stream().forEach(node -> {
				node.setVmId(vm.getVmId());
			});
			
		});
		
		
		// for debug
		
/*		vmMap.entrySet().stream().forEach(vm -> {
			logger.debug(vm.getKey() + " {"+ vm.getValue().getVmModel().getType() + "} -> " +
					vm.getValue().getNodeList().stream().map(m -> m.getId())
					.collect(Collectors.toList())
		);
		});
		
		vmMap.entrySet().stream().forEach(vm -> {
			logger.debug(vm.getKey() + " {"+ vm.getValue().getVmModel().getType() + "} -> " +
					vm.getValue().getTaskList().stream().map(m -> m.getTaskGroupId())
					.collect(Collectors.toList())
		);
		});
		
		taskVmMap.entrySet().stream().forEach(vm -> {
			logger.debug(vm.getKey() + " -> " +
					vm.getValue()
		);
		});
		
		logger.debug("");*/
		
	}
	
	private void setVmMapList() {
		vmIdList.stream().forEach(id -> {
			PlanedVm vm = new PlanedVm(id);
			vm.setVmModel(vmModel.getVmModel(id%vmModel.getNoOfTypes()));
						
			List<TimeSlot> timeList = new ArrayList<TimeSlot>();
			
			for(int i = 0; i < this.workflow.getNoOfNode(); i++) {
				TimeSlot emptyTime = new TimeSlot();
				timeList.add(emptyTime);
			}
			
			vm.setTimeList(timeList);
			
			vmMap.put(id, vm);
		});
	}
	
	private void setSolutionList() {
		for (int i = 0; i < solution.getNumberOfVariables(); i++) {
			solutionList.add(solution.getVariableValue(i));			

			taskVmMap.put(this.workflow.getTaskGroupSortedByTopologyOrder().get(i).getTaskGroupId(), solution.getVariableValue(i));
		}
		
		vmIdList = solutionList.stream().distinct().sorted().collect(Collectors.toList());
		
		logger.debug("Solution variables -> " + this.solutionList);
		logger.debug("VM ID list -> " + this.vmIdList);
	}
	
	public void printSolutionLits() {
		logger.debug("Solution variables -> " + this.solutionList);
		logger.debug("VM ID list -> " + this.vmIdList);
		logger.debug("No of VMs -> " + this.vmMap.size());
		logger.debug("");
/*		System.out.println(this.solutionList.size());
		System.out.println(this.solutionList);
		System.out.println(this.vmIdList.size());
		System.out.println(this.vmIdList);
		System.out.println(this.vmMap.size());
		System.out.println("");*/
	}

	public IntegerSolution getSolution() {
		return solution;
	}

	public void setSolution(IntegerSolution solution) {
		this.solution = solution;
	}

	public void printSchedulingPlan() {

		logger.debug("");
		logger.debug("======================Scheduling Plan====================");
		logger.debug("");
		
		this.vmMap.entrySet().stream().map(m -> m.getValue())
			.forEach(vm -> {

				List<String> taskStr = new ArrayList<String>();
				
				vm.getTimeList().stream()
								.forEach(t -> {
									if(t.isEmptyTimeSlot()) {
										taskStr.add(String.format("%10s", "-"));
									} else {
										taskStr.add(String.format("%10s", t.getComputeId()));
									}
								});
				
				List<String> startStr = new ArrayList<String>();
				
				vm.getTimeList().stream()
								.forEach(t -> {
									if(t.isEmptyTimeSlot()) {
										startStr.add(String.format("%10s", "-"));
									} else {
										startStr.add(String.format("%10.2f", t.getStartTime()));
									}
								});
				
				List<String> finishStr = new ArrayList<String>();
				
				vm.getTimeList().stream()
								.forEach(t -> {
									if(t.isEmptyTimeSlot()) {
										finishStr.add(String.format("%10s", "-"));
									} else {
										finishStr.add(String.format("%10.2f", t.getFinishTime()));
									}
								});
				

				
				List<String> commuStr = new ArrayList<String>();
				
				vm.getTimeList().stream()
								.forEach(t -> {
									if(t.isEmptyTimeSlot()) {
										commuStr.add(String.format("%10s", "-"));
									} else {
										commuStr.add(String.format("%10.2f", t.getCommunicationTime()));
									}
								});
				
				List<String> ratioStr = new ArrayList<String>();
				
				vm.getTimeList().stream()
								.forEach(t -> {
									if(t.isEmptyTimeSlot()) {
										ratioStr.add(String.format("%10s", "-"));
									} else {
										ratioStr.add(String.format("%10s", t.getRemoteCommunicationTask() + "|" 
												+ (t.getRemoteCommunicationTask() + t.getLocalCommunicationTask())));
									}
								});
				
				logger.debug(String.format("{%3d} -> ", vm.getVmId()) + taskStr);
				logger.debug(String.format("%9s", "") + startStr);
				logger.debug(String.format("%9s", "") + finishStr);
				logger.debug(String.format("%9s", "") + commuStr);
				logger.debug(String.format("%9s", "") + ratioStr);

				logger.debug("");

				
			});
	}
}
