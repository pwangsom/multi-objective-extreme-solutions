package com.kmutt.sit.mop.manager.knapsack;

import java.util.List;

import com.kmutt.sit.mop.manager.configulations.ConfigurationInterface;
import com.kmutt.sit.mop.problem.knapsack.KnapsackDataset;
import com.kmutt.sit.mop.problem.knapsack.KnapsackRepresentationProblemType;

public interface KnapsackConfigurationInterface extends ConfigurationInterface {

	public KnapsackDataset getDataset();
	public void setDataset(KnapsackDataset dataset);
	public String getSubfolderOutputPath();
	public void setSubfolderOutputPath(String subOutputPath);
	public void setProblemList(String[] problems);
	public void setKnapsackRepresentationProblemType(KnapsackRepresentationProblemType problem);
	public KnapsackRepresentationProblemType getKnapsackRepresentationProblemType();
	public List<KnapsackRepresentationProblemType> getKnapsackRepresentationProblemTypeList();
	
}
