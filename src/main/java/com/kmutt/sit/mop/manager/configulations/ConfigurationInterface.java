package com.kmutt.sit.mop.manager.configulations;

import java.io.Serializable;
import java.util.List;

import com.kmutt.sit.mop.algorithm.AlgorithmType;

public interface ConfigurationInterface extends Serializable {

	String getRefParetoFrontKey();
	String getExperimentTreatmentKey();
	String getRunningKey();
	
	String getProgramKey();
	void setProgramKey(String programKey);
	String getWorkingPath();
	void setWorkingPath(String workingPath);
	String getInputPath();
	void setInputPath(String inputPath);
	String getOutputPath();
	void setOutputPath(String outputPath);
	String getName();
	void setName(String name);
	String getId();
	void setId(String id);
	int getMaxRun();
	void setMaxRun(int maxRun);
	int getAllMaxIteration();
	void setAllMaxIteration(int allMaxIteration);
	int getIntervalIteration();
	void setIntervalIteration(int intervalIteration);
	int getCurrentMaxIteration();
	void setCurrentMaxIteration(int currentMaxIteration);
	int getCurrentRun();
	void setCurrentRun(int currentRun);
	List<AlgorithmType> getAlgorithmTypeList();
	void setAlgorithmTypeList(List<AlgorithmType> algorithmTypeList);
	void setAlgorithmTypeList(String[] algorithmTypes);
	AlgorithmType getAlgorithmType();
	void setAlgorithmType(AlgorithmType algorithm);
	List<String> getAttributeGroupingList();
	void setAttributeGroupingList(List<String> list);
	void addAttributeGroupingList(String attribute);

	String getAverageIndicatorKey(String treatmentKey);
	String getIndicatorFirstColumnName();
	
	String getAnalysisPrefixFileNamePath();
}