package com.kmutt.sit.mop.manager.scheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kmutt.sit.cloud.vm.model.ProviderModelInterface;
import com.kmutt.sit.mop.algorithm.AlgorithmType;
import com.kmutt.sit.mop.problem.scheduling.ThirdObjectiveType;
import com.kmutt.sit.workflow.ClusterType;
import com.kmutt.sit.workflow.GeneralClusterWorkflow;

public class DagSchedulingConfiguration {
	
	private String programKey;
	
	private String workingPath;
	private String inputPath;
	private String outputPath;

	private String workflowName;
	private int workflowSize;
	
	private boolean skipUncompliedP2pWorkflow = true;
	
	private int maxRun;
	private int allMaxIteration;
	private int intervalIteration;
	private int currentMaxIteration;
	private int currentRun;
	
	private ProviderModelInterface providerModel;
	private Double networkDegradation = 0.0;
	
	private Map<ClusterType, GeneralClusterWorkflow> clusterWorkflowMapping;
	
	private AlgorithmType algorithm;
	private ClusterType cluster;
	private ThirdObjectiveType thirdObjectiveType = ThirdObjectiveType.COMMU;
	
	private boolean isThereP2pCluster = false;	
	
	private String thirdObjectiveName;
	
	private List<AlgorithmType> algorithmTypeList;
	private List<ClusterType> clusterTypeList;
	
	public DagSchedulingConfiguration() {
		algorithmTypeList = new ArrayList<AlgorithmType>();
		clusterTypeList = new ArrayList<ClusterType>();	
		clusterWorkflowMapping = new HashMap<ClusterType, GeneralClusterWorkflow>();
	}
	
	public String getRunningKey() {
		return getWorkflowName() + "_" + getWorkflowSize() + "_"
				+ getClusterType().getName() + "_"
				+ getAlgorithmType().getName()
				+ "_run" + getCurrentRun()
				+ "_maxgen" + getCurrentMaxIteration();
	}

	public GeneralClusterWorkflow getCurrentWorkflow() {
		return this.clusterWorkflowMapping.get(this.cluster);
	}
	
	public boolean isThereP2pCluster() {
		return isThereP2pCluster;
	}
	public void setThereP2pCluster(boolean isThereP2pCluster) {
		this.isThereP2pCluster = isThereP2pCluster;
	}
	
	public void addClusterWorkflow(ClusterType clusterType, GeneralClusterWorkflow clusterWorkflow) {
		if(!this.clusterWorkflowMapping.containsKey(clusterType)) {
			this.clusterWorkflowMapping.put(clusterType, clusterWorkflow);
		}
	}
	
	public String getProgramKey() {
		return programKey;
	}
	public void setProgramKey(String programKey) {
		this.programKey = programKey;
	}
	public String getWorkingPath() {
		return workingPath;
	}
	public void setWorkingPath(String workingPath) {
		this.workingPath = workingPath;
	}
	public String getInputPath() {
		return inputPath;
	}
	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}
	public String getOutputPath() {
		return outputPath;
	}
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
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
	public int getMaxRun() {
		return maxRun;
	}
	public void setMaxRun(int maxRun) {
		this.maxRun = maxRun;
	}
	public int getAllMaxIteration() {
		return allMaxIteration;
	}
	public void setAllMaxIteration(int maxIteration) {
		this.allMaxIteration = maxIteration;
	}
	public int getCurrentMaxIteration() {
		return currentMaxIteration;
	}
	public void setCurrentMaxIteration(int currentMaxIteration) {
		this.currentMaxIteration = currentMaxIteration;
	}
	public int getCurrentRun() {
		return currentRun;
	}
	public void setCurrentRun(int currentRun) {
		this.currentRun = currentRun;
	}
	public AlgorithmType getAlgorithmType() {
		return algorithm;
	}
	public void setAlgorithmType(AlgorithmType algorithm) {
		this.algorithm = algorithm;
	}
	public ClusterType getClusterType() {
		return cluster;
	}
	public void setClusterType(ClusterType cluster) {
		this.cluster = cluster;
	}
	public String getThirdObjectiveName() {
		return thirdObjectiveName;
	}
/*	public void setThirdObjectiveName(String thirdObjectiveName) {
		this.thirdObjectiveName = thirdObjectiveName;
	}*/

	public ThirdObjectiveType getThirdObjectiveType() {
		return thirdObjectiveType;
	}

	public void setThirdObjectiveType(ThirdObjectiveType thirdObjectiveType) {
		this.thirdObjectiveType = thirdObjectiveType;
		this.thirdObjectiveName = thirdObjectiveType.getName();
	}

	public int getIntervalIteration() {
		return intervalIteration;
	}

	public void setIntervalIteration(int intervalIteration) {
		this.intervalIteration = intervalIteration;
	}

	public List<AlgorithmType> getAlgorithmTypeList() {
		return algorithmTypeList;
	}

	public void setAlgorithmTypeList(List<AlgorithmType> algorithmTypeList) {
		this.algorithmTypeList = algorithmTypeList;
	}

	public List<ClusterType> getClusterTypeList() {
		return clusterTypeList;
	}

	public void setClusterTypeList(List<ClusterType> clusterTypeList) {
		this.clusterTypeList = clusterTypeList;
	}

	public ProviderModelInterface getProviderModel() {
		return providerModel;
	}

	public void setProviderModel(ProviderModelInterface vmModel) {
		this.providerModel = vmModel;
	}

	public Double getNetworkDegradation() {
		return networkDegradation;
	}

	public void setNetworkDegradation(Double networkDegradation) {
		this.networkDegradation = networkDegradation;
	}

	public boolean isSkipUncompliedP2pWorkflow() {
		return skipUncompliedP2pWorkflow;
	}

	public void setSkipUncompliedP2pWorkflow(boolean skipUncompliedP2pWorkflow) {
		this.skipUncompliedP2pWorkflow = skipUncompliedP2pWorkflow;
	}

}
