package com.kmutt.sit.mop.pareto.collection.proprietary;

import java.util.List;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.point.util.PointSolution;

import com.kmutt.sit.mop.algorithm.AlgorithmType;
import com.kmutt.sit.mop.pareto.collection.ParetoIndicator;
import com.kmutt.sit.workflow.ClusterType;

public class DagSchedulingPopulationOfGeneration<S extends Solution<?>> {
	
	private String workflowName;
	private int workflowSize;
	
	private int maxGeneration;
	private int currentGeneration;
	private int currentRun;
	
	private AlgorithmType algorithmType;
	private ClusterType clusterType;
	
	private List<S> paretoSet;
	
	private List<PointSolution> normalizedParetoSet;
	
	private ParetoIndicator<S> indicator;
	
	public void setIndicator(Front referenceFront, Front normalizedReferenceFront, FrontNormalizer frontNormalizer) {
		indicator = new ParetoIndicator<S>();
		indicator.setReferenceFront(referenceFront, normalizedReferenceFront, frontNormalizer);
		indicator.evaluateIndicator(false, paretoSet);
	}
	
	public void measureIndicator() {
		normalizedParetoSet = indicator.evaluateIndicator(false, paretoSet);
	}
	
	public List<PointSolution> getNormalizedParetoSet(){
		return normalizedParetoSet;
	}
	
	public ParetoIndicator<S> getIndicator(){
		return indicator;
	}
	
	public String getPopulationKey() {
		
		String runStr;
		String maxGenStr;
		String genStr;
				
		runStr = getCurrentRun() == 999? "ALL": String.valueOf(getCurrentRun()); 
		maxGenStr = getMaxGeneration() == 999? "ALL": String.valueOf(getMaxGeneration()); 
		genStr = getCurrentGeneration() == 999? "LAST": String.valueOf(getCurrentGeneration());
		
		String result = String.format("%s_%s_%s_%s_run%s_maxgen%s_gen%s", getWorkflowName(), getWorkflowSize(),
				getClusterType().getName(), getAlgorithmType().getName(),
				runStr, maxGenStr, genStr);
		
		return result;
	}
	
	public String getWorkflowName() {
		return workflowName;
	}
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}
	public int getWorkflowSize() {
		return workflowSize;
	}
	public void setWorkflowSize(int workflowSize) {
		this.workflowSize = workflowSize;
	}
	public int getMaxGeneration() {
		return maxGeneration;
	}
	public void setMaxGeneration(int maxGeneration) {
		this.maxGeneration = maxGeneration;
	}
	public int getCurrentGeneration() {
		return currentGeneration;
	}
	public void setCurrentGeneration(int currentGeneration) {
		this.currentGeneration = currentGeneration;
	}
	public int getCurrentRun() {
		return currentRun;
	}
	public void setCurrentRun(int currentRun) {
		this.currentRun = currentRun;
	}
	public AlgorithmType getAlgorithmType() {
		return algorithmType;
	}
	public void setAlgorithmType(AlgorithmType algorithmType) {
		this.algorithmType = algorithmType;
	}
	public ClusterType getClusterType() {
		return clusterType;
	}
	public void setClusterType(ClusterType clusterType) {
		this.clusterType = clusterType;
	}
	public List<S> getParetoSet() {
		return paretoSet;
	}
	public void setParetoSet(List<S> paretoSet) {
		this.paretoSet = paretoSet;
	}

}
