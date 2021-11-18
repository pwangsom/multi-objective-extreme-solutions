package com.kmutt.sit.mop.manager.configulations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kmutt.sit.mop.algorithm.AlgorithmType;

public abstract class AbstractGeneralConfiguration implements ConfigurationInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Global configurations for all treatment instances in the experiment.	
	protected String programKey;
	
	protected String workingPath;
	protected String inputPath;
	protected String outputPath;	
	
	protected int maxRun;
	protected int allMaxIteration;
	protected int intervalIteration;
	

	protected String runPrefix = "_run";
	protected String runAvgALLSubfix = "_runAvgALL";	

	protected String maxgenPrefix = "_maxgen";

	protected List<AlgorithmType> algorithmTypeList = new ArrayList<AlgorithmType>();

	// Instance configurations for each treatment instance in the experiment.	
	protected String name;
	protected String id;
	protected int currentMaxIteration;
	protected int currentRun;
	
	protected AlgorithmType algorithmType;
	
	public abstract String getRunningKey();
	public abstract String getExperimentTreatmentKey();
	public abstract String getRefParetoFrontKey();
	
	public abstract String getAverageIndicatorKey(String treatmentKey);
	public abstract String getIndicatorFirstColumnName();
	public abstract String getAnalysisPrefixFileNamePath();
	
	@Override
	public List<String> getAttributeGroupingList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttributeGroupingList(List<String> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAttributeGroupingList(String attribute) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getProgramKey() {
		return programKey;
	}
	@Override
	public void setProgramKey(String programKey) {
		this.programKey = programKey;
	}
	@Override
	public String getWorkingPath() {
		return workingPath;
	}
	@Override
	public void setWorkingPath(String workingPath) {
		this.workingPath = workingPath;
	}
	@Override
	public String getInputPath() {
		return inputPath;
	}
	@Override
	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}
	@Override
	public String getOutputPath() {
		return outputPath;
	}
	@Override
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String getId() {
		return id;
	}
	@Override
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public int getMaxRun() {
		return maxRun;
	}
	@Override
	public void setMaxRun(int maxRun) {
		this.maxRun = maxRun;
	}
	@Override
	public int getAllMaxIteration() {
		return allMaxIteration;
	}
	@Override
	public void setAllMaxIteration(int allMaxIteration) {
		this.allMaxIteration = allMaxIteration;
	}
	@Override
	public int getIntervalIteration() {
		return intervalIteration;
	}
	@Override
	public void setIntervalIteration(int intervalIteration) {
		this.intervalIteration = intervalIteration;
	}
	@Override
	public int getCurrentMaxIteration() {
		return currentMaxIteration;
	}
	@Override
	public void setCurrentMaxIteration(int currentMaxIteration) {
		this.currentMaxIteration = currentMaxIteration;
	}
	@Override
	public int getCurrentRun() {
		return currentRun;
	}
	@Override
	public void setCurrentRun(int currentRun) {
		this.currentRun = currentRun;
	}

	@Override
	public AlgorithmType getAlgorithmType() {
		return algorithmType;
	}
	@Override
	public void setAlgorithmType(AlgorithmType algorithmType) {
		this.algorithmType = algorithmType;
	}
	@Override
	public List<AlgorithmType> getAlgorithmTypeList() {
		return algorithmTypeList;
	}
	@Override
	public void setAlgorithmTypeList(List<AlgorithmType> algorithmTypeList) {
		this.algorithmTypeList = algorithmTypeList;
	}
	@Override
	public void setAlgorithmTypeList(String[] algorithmTypes) {
		Arrays.asList(algorithmTypes).stream().forEach(algorithm -> {
			
			switch (algorithm.trim().toLowerCase()) {
			case "nsgaii":
				algorithmTypeList.add(AlgorithmType.NSGA_II);
				break;
			case "nsgaiii":
				algorithmTypeList.add(AlgorithmType.NSGA_III);
				break;
			case "ensgaiii":
				algorithmTypeList.add(AlgorithmType.ENSGA_III);
				break;
			case "kpnc_nsgaii":
				algorithmTypeList.add(AlgorithmType.KPNC_NSGA_II);
				break;
			case "kpnc_nsgaiii":
				algorithmTypeList.add(AlgorithmType.KPNC_NSGA_III);
				break;
			case "kpnc_ensgaiii":
				algorithmTypeList.add(AlgorithmType.KPNC_ENSGA_III);
				break;
			default:
				break;
			}			
		});
	}
	
}
