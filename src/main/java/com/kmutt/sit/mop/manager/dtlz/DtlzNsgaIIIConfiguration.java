package com.kmutt.sit.mop.manager.dtlz;

public class DtlzNsgaIIIConfiguration {
	
	private String programKey;
	
	private String workingPath;
	private String inputPath;
	private String outputPath;

	private String problemName;
	private String problemClass;
	private String referenceParetoFront = null;
	
	private int maxRun;
	private int maxIteration;
	private int intervalIteration;
	private int currentRun;

	
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
	public String getProblemName() {
		return problemName;
	}
	public void setProblemName(String problemName) {
		this.problemName = problemName;
	}
	public String getProblemClass() {
		return problemClass;
	}
	public void setProblemClass(String problemClass) {
		this.problemClass = problemClass;
	}
	public String getReferenceParetoFront() {
		return referenceParetoFront;
	}
	public void setReferenceParetoFront(String referenceParetoFront) {
		this.referenceParetoFront = referenceParetoFront;
	}
	public int getMaxRun() {
		return maxRun;
	}
	public void setMaxRun(int maxRun) {
		this.maxRun = maxRun;
	}
	public int getMaxIteration() {
		return maxIteration;
	}
	public void setMaxIteration(int maxIteration) {
		this.maxIteration = maxIteration;
	}
	public int getIntervalIteration() {
		return intervalIteration;
	}
	public void setIntervalIteration(int intervalIteration) {
		this.intervalIteration = intervalIteration;
	}	
	public int getCurrentRun() {
		return currentRun;
	}
	public void setCurrentRun(int currentRun) {
		this.currentRun = currentRun;
	}	

}
