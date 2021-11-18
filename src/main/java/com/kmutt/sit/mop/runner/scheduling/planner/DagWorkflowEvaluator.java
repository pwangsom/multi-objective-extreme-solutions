package com.kmutt.sit.mop.runner.scheduling.planner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.util.Precision;
import org.apache.log4j.Logger;

import com.kmutt.sit.mop.parameter.Configuration;
import com.kmutt.sit.workflow.component.PlanedVm;

public class DagWorkflowEvaluator {
	
	private Logger logger = Logger.getLogger(DagWorkflowEvaluator.class);

	private Map<Integer, PlanedVm> plan;

	private Double cost;
	private Double makespan;
	
	private Double idleTime;
	private Double idleTimeRatio;
	private Double idleTimeCost;
	
	private Double commuTime;
	private Integer noOfRemoteCommu;
	private Integer noOfTotalCommu;
	
	private List<Double> costList;
	private List<Double> makespanList;
	
	private List<Double> idleTimeList;
	private List<Double> idleRatioList;
	private List<Double> idleCostList;
	
	private List<Double> commuTimeList;	
	private List<Integer> remoteList;	
	private List<Integer> commuList;	

	public DagWorkflowEvaluator(Map<Integer, PlanedVm> plan) {
		this.plan = plan;
		this.costList = new ArrayList<Double>();
		this.makespanList = new ArrayList<Double>();
		
		this.idleTimeList = new ArrayList<Double>();
		this.idleRatioList = new ArrayList<Double>();
		this.idleCostList = new ArrayList<Double>();
		
		this.commuTimeList = new ArrayList<Double>();
		this.remoteList = new ArrayList<Integer>();
		this.commuList = new ArrayList<Integer>();
	}
	
	public void printReport() {
		logger.debug("");
		logger.info("======================Plan Performance====================");
		logger.debug("");

		logger.info("No of VMs    : " + this.plan.size());
		logger.info("Cost         : " + this.getCost());
		logger.info("Makespan     : " + this.getMakespan());
		logger.info("Idle Time    : " + this.getIdleTime());
		logger.info("Idle Ratio   : " + this.getIdleTimeRatio());
		logger.info("Idle Cost    : " + this.getIdleTimeCost());
		logger.info("Commu. Time  : " + this.getCommunicationTime());
		logger.info("Remote Commu.: " + this.getNoOfRemoteCommunicationTask());
		logger.info("Total Commu. : " + this.getNoOfTotalCommunicationTask());
		
		logger.debug("");
		logger.info("=================Finish Plan Performance==================");
		logger.debug("");
		
	}
	
	public String toString() {
		String result = ("Plan Performanace -> [NoOfVm: %d, Cost: %.4f, Makespan: %.4f, IdleTime: %.4f, IdleRatio: %.4f, "
						+ "IdleCost: %.4f, CommuTime: %.4f, RemoteComTask: %d, TotalComTask: %d]");
		return String.format(result, this.plan.size(), this.getCost(), this.getMakespan(), this.getIdleTime(), this.getIdleTimeRatio(),
						this.getIdleTimeCost(), this.getCommunicationTime(), this.getNoOfRemoteCommunicationTask(), this.getNoOfTotalCommunicationTask());
	}
	
	public void evaluate() {
		
		this.plan.entrySet().stream().map(en -> en.getValue())
			.forEach(vm -> {
				setEvaluatedMeasurements(vm);
			});
		
		this.cost = this.costList.stream().mapToDouble(Double::doubleValue).sum();
		this.makespan = this.makespanList.stream().max(Comparator.comparingDouble(Double::doubleValue)).get();
		
		this.cost = Precision.round(this.cost, Configuration.PRECISION_SCALE);
		this.makespan = Precision.round(this.makespan, Configuration.PRECISION_SCALE);

		this.idleTime = this.idleTimeList.stream().mapToDouble(Double::doubleValue).sum();
		this.idleTimeRatio = this.idleRatioList.stream().mapToDouble(Double::doubleValue).sum();
		this.idleTimeCost = this.idleCostList.stream().mapToDouble(Double::doubleValue).sum();
		
		this.idleTime = Precision.round(this.idleTime, Configuration.PRECISION_SCALE);
		this.idleTimeRatio = Precision.round(this.idleTimeRatio / plan.size(), Configuration.PRECISION_SCALE);
		this.idleTimeCost = Precision.round(this.idleTimeCost, Configuration.PRECISION_SCALE);
		
		this.commuTime = this.commuTimeList.stream().mapToDouble(Double::doubleValue).sum();		
		this.noOfRemoteCommu = this.remoteList.stream().mapToInt(Integer::valueOf).sum();		
		this.noOfTotalCommu = this.commuList.stream().mapToInt(Integer::valueOf).sum();		
		
		this.commuTime = Precision.round(this.commuTime, Configuration.PRECISION_SCALE);
	}	
	
	private void setEvaluatedMeasurements(PlanedVm vm) {
		vm.assessVm();
		
		this.costList.add(vm.getCost());
		this.makespanList.add(vm.getMakespan());
		
		this.idleTimeList.add(vm.getIdleTime());
		this.idleRatioList.add(vm.getIdleTimeRatio());
		this.idleCostList.add(vm.getIdleTimeCost());
		
		this.commuTimeList.add(vm.getCommunicationTime());
		this.remoteList.add(vm.getNoOfRemoteCommuTask());
		this.commuList.add(vm.getNoOfTotalCommuTask());
	}
	
	public Integer getNoOfVMs() {
		return this.plan.size();
	}

	public double getCost() {
		return cost;
	}

	public double getMakespan() {
		return makespan;
	}

	public double getIdleTime() {
		return idleTime;
	}

	public Double getIdleTimeRatio() {
		return idleTimeRatio;
	}

	public Double getIdleTimeCost() {
		return idleTimeCost;
	}

	public double getCommunicationTime() {
		return commuTime;
	}

	public int getNoOfRemoteCommunicationTask() {
		return noOfRemoteCommu;
	}

	public Integer getNoOfTotalCommunicationTask() {
		return noOfTotalCommu;
	}
}
