package com.kmutt.sit.workflow.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.math3.util.Precision;

import com.kmutt.sit.cloud.vm.model.VmModel;
import com.kmutt.sit.mop.parameter.Configuration;
import com.kmutt.sit.pegasus.dax.DaxNode;
import com.kmutt.sit.utils.JavaHelper;

public class PlanedVm {	
	
	private int vmId;
	private VmModel vmModel;
	private List<TimeSlot> timeList;
	private List<TaskGroup> taskList;
	private Map<String, Integer> nodeTimeSlotMap;
	private List<Integer> timeSlotIndexList;
	private List<DaxNode> nodeList;
	
	private Double vmBilledHours;
	private Double cost;
	private Double makespan;
	
	private List<Double> idleTimeList;
	private Double idleTime;
	private Double idleTimeRatio;
	private Double idleTimeCost;
	
	private int noOfLocalCommuTask;
	private int noOfRemoteCommuTask;
	private int noOfTotalCommuTask;
	private Double commuTime;
	
	private Double startVm;
	private Double actualRuntime;

	public PlanedVm() {
		this.timeList = new ArrayList<TimeSlot>();
		this.taskList = new ArrayList<TaskGroup>();
		this.timeSlotIndexList = new ArrayList<Integer>();
		this.nodeTimeSlotMap = new HashMap<String, Integer>();
		this.nodeList = new ArrayList<DaxNode>();
	}

	public PlanedVm(int vmId) {
		this();
		this.vmId = vmId;
	}
	
	public void assessVm() {
		assessTime();
		assessCost();
		assessIdleTime();
		assessCommunication();
	}
	
	private void assessTime() {		
		this.startVm = timeList.get(timeSlotIndexList.get(0)).getStartTime();
		this.makespan = timeList.get(timeSlotIndexList.get(timeSlotIndexList.size() - 1)).getFinishTime();

		// actualRuntime is second
		this.actualRuntime = this.makespan - this.startVm;
		
		// convert vmActualRuntime in second to hour and round it up for billed hour
		this.vmBilledHours = Math.ceil(this.actualRuntime/3600);
	}
	
	private void assessCost() {
		this.cost = Precision.round(this.vmBilledHours * vmModel.getCost(), Configuration.PRECISION_SCALE);
	}
	
	private void assessIdleTime() {
		
		idleTimeList = new ArrayList<Double>();		

		Double lastFinishTime = timeList.get(timeSlotIndexList.get(0)).getFinishTime();
		
		if(timeSlotIndexList.size() > 1) {
			
			for(int i = 1; i < this.timeSlotIndexList.size(); i++) {
				Double nextStartTime = timeList.get(timeSlotIndexList.get(i)).getStartTime();
				
				if(nextStartTime > lastFinishTime) {
					idleTimeList.add(nextStartTime - lastFinishTime);
				}
				
				lastFinishTime = timeList.get(timeSlotIndexList.get(i)).getFinishTime();
				
			}
		}
		
		// Waste time after VM stop but bill is still running
		Double stopBilledTime = this.startVm + (this.vmBilledHours * 3600);
		
		if(stopBilledTime > lastFinishTime) {
			idleTimeList.add(stopBilledTime - lastFinishTime);
		}
		
		if(this.idleTimeList.isEmpty()) {
			this.idleTime = Precision.round(0.00, Configuration.PRECISION_SCALE);
			this.idleTimeRatio = Precision.round(0.00, Configuration.PRECISION_SCALE);
			this.idleTimeCost = Precision.round(0.00, Configuration.PRECISION_SCALE);
		} else {			
			this.idleTime = Precision.round(this.idleTimeList.stream().mapToDouble(Double::doubleValue).sum(), Configuration.PRECISION_SCALE);
			this.idleTimeRatio =  Precision.round((this.idleTime / (this.vmBilledHours * 3600)), Configuration.PRECISION_SCALE);
			this.idleTimeCost =  Precision.round((this.idleTime / (this.vmBilledHours * 3600)) * vmModel.getCost(), Configuration.PRECISION_SCALE);
		}
		
	}
	
	private void assessCommunication() {
		
		this.commuTime = 0.00;
		this.noOfLocalCommuTask = 0;
		this.noOfRemoteCommuTask = 0;
		this.noOfTotalCommuTask = 0;
		
		for(int i = 0; i < this.timeSlotIndexList.size(); i++) {
			
			TimeSlot time = timeList.get(timeSlotIndexList.get(i));
			
			this.commuTime += time.getCommunicationTime();
			this.noOfLocalCommuTask += time.getLocalCommunicationTask();
			this.noOfRemoteCommuTask += time.getRemoteCommunicationTask();
			
		}
		
		this.commuTime = Precision.round(commuTime, Configuration.PRECISION_SCALE);
		this.noOfTotalCommuTask = this.noOfLocalCommuTask + this.noOfRemoteCommuTask;
		
	}

	public Double getVmBilledHours() {
		return vmBilledHours;
	}

	public Double getCost() {
		return cost;
	}

	public Double getMakespan() {
		return makespan;
	}

	public List<Double> getIdleTimeList() {
		return idleTimeList;
	}

	public Double getIdleTime() {
		return idleTime;
	}

	public Double getIdleTimeRatio() {
		return idleTimeRatio;
	}

	public Double getIdleTimeCost() {
		return idleTimeCost;
	}

	public int getNoOfLocalCommuTask() {
		return noOfLocalCommuTask;
	}

	public int getNoOfRemoteCommuTask() {
		return noOfRemoteCommuTask;
	}

	public int getNoOfTotalCommuTask() {
		return noOfTotalCommuTask;
	}

	public Double getCommunicationTime() {
		return commuTime;
	}

	public void addTask(TaskGroup task) {
		this.taskList.add(task);
	}

	public void addTimeSlot(TimeSlot timeSlot) {
		this.timeList.add(timeSlot);
	}

	public int getVmId() {
		return vmId;
	}

	public void setVmId(int vmId) {
		this.vmId = vmId;
	}

	public VmModel getVmModel() {
		return vmModel;
	}

	public void setVmModel(VmModel vmModel) {
		this.vmModel = vmModel;
	}

	public List<TimeSlot> getTimeList() {
		return timeList;
	}

	public void setTimeList(List<TimeSlot> timeList) {
		this.timeList = timeList;
	}

	public List<TaskGroup> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<TaskGroup> taskList) {
		this.taskList = taskList;
	}

	public List<DaxNode> getNodeList() {
		return nodeList;
	}
	
/*	
	Comparator.comparing((Person p)->p.firstName)
    .thenComparing(p->p.lastName)
    .thenComparingInt(p->p.age);
	*/
	
	public void setTopologicalOrderOfNodeList() {		
		List<List<DaxNode>> nodeListList = taskList.stream().map(m -> m.getNodes()).collect(Collectors.toList());
		this.nodeList = nodeListList.stream().flatMap(List::stream).collect(Collectors.toList());
				
		this.nodeList = JavaHelper.getSortedNodeListBySchedulingType(nodeList, Configuration.SCHEDULING_TYPE);		
	}

	public TimeSlot getTimeSlotByIndex(int index) {
		return timeList.get(index);
	}

	public void setTimeSlotByIndex(int index, TimeSlot time) {
		timeList.set(index, time);
		nodeTimeSlotMap.put(time.getComputeTask().getId(), index);
		if(!this.timeSlotIndexList.contains(index)) {
			this.timeSlotIndexList.add(index);
		}
	}

	public int getIndexOfTimeSlotByNodeId(String nodeId) {
		return nodeTimeSlotMap.get(nodeId);
	}
	
	public TimeSlot getTimeSlotByNodeId(String nodeId) {
		return timeList.get(getIndexOfTimeSlotByNodeId(nodeId));
	}
	
	public List<Integer> getTimeSlotIndexList() {
		Collections.sort(timeSlotIndexList); 
		return timeSlotIndexList;
	}
	
	public void setTimeSlotForNode(DaxNode node, TimeSlot dependentTimeSlot) {

		TimeSlot time = new TimeSlot();
		int index = 0;
		
		if(JavaHelper.isNull(dependentTimeSlot)) {
			time.setTimeIndex(index);
			time.setStartTime(0.0000);
			
			Double computeTime = node.getActualRuntime() * this.vmModel.getSlowdownRatio();
			computeTime = computeTime + (computeTime * this.vmModel.getVmDegradation());
			
			time.setComputeTime(computeTime);
		} else {
			index = dependentTimeSlot.getTimeIndex() + 1;
			time.setTimeIndex(index);
			time.setStartTime(dependentTimeSlot.getFinishTime());
			
			Double computeTime = node.getActualRuntime() * this.vmModel.getSlowdownRatio();
			computeTime = computeTime + (computeTime * this.vmModel.getVmDegradation());
			
			time.setComputeTime(computeTime);
		}

		time.setComputeTask(node);
		this.setTimeSlotByIndex(index, time);
	}

	public void setTimeSlotForHeadNode(DaxNode node, int index) {

		TimeSlot time = new TimeSlot();
		time.setTimeIndex(index);

		// index = 0 is the first time slot
		if (index == 0) {
			time.setStartTime(0.0000);
			
			Double computeTime = node.getActualRuntime() * this.vmModel.getSlowdownRatio();
			computeTime = computeTime + (computeTime * this.vmModel.getVmDegradation());
			computeTime += this.vmModel.getVmBootTime();
			
			time.setComputeTime(computeTime);
			
		} else {
			TimeSlot previous = this.getTimeSlotByIndex(index - 1);

			if (!previous.isEmptyTimeSlot()) {
				time.setStartTime(previous.getFinishTime());
				
				Double computeTime = node.getActualRuntime() * this.vmModel.getSlowdownRatio();
				computeTime = computeTime + (computeTime * this.vmModel.getVmDegradation());
				
				time.setComputeTime(computeTime);
			}
		}

		time.setComputeTask(node);
		this.setTimeSlotByIndex(index, time);
	}
	
	public TimeSlot getLastNotNullTimeSlot() {
		
		TimeSlot result = new TimeSlot();

		if(!this.timeSlotIndexList.isEmpty()) {
			int index = Collections.max(this.timeSlotIndexList);
			result = timeList.get(index);
		}
		
		return result;		
	}

	public int getLastIndexOfNotNullTimeSlot() {

		// return -1 if there is no not null time slot it list
		int index = -1;

		if(!this.timeSlotIndexList.isEmpty()) {
			index = Collections.max(this.timeSlotIndexList);
		}
		
		return index;
	}

}
