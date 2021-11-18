package com.kmutt.sit.mop.manager.knapsack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kmutt.sit.mop.manager.configulations.AbstractGeneralConfiguration;
import com.kmutt.sit.mop.problem.knapsack.KnapsackDataset;
import com.kmutt.sit.mop.problem.knapsack.KnapsackRepresentationProblemType;

public class KnapsackConfiguration extends AbstractGeneralConfiguration implements KnapsackConfigurationInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private KnapsackDataset dataset;
	private String subfolderOutputPath;
	private List<KnapsackRepresentationProblemType> problemTypeList = new ArrayList<KnapsackRepresentationProblemType>();
	private KnapsackRepresentationProblemType problemType;

	public KnapsackDataset getDataset() {
		return dataset;
	}

	public void setDataset(KnapsackDataset dataset) {
		this.dataset = dataset;
	}

	@Override
	public String getRefParetoFrontKey() {
		// TODO Auto-generated method stub
		return getName() + "_" + getId();
	}

	@Override
	public String getExperimentTreatmentKey() {
		// TODO Auto-generated method stub
		return getRefParetoFrontKey() + "_" + getKnapsackRepresentationProblemType().getName() + "_" + getAlgorithmType().getName();
	}

	@Override
	public String getRunningKey() {
		// TODO Auto-generated method stub
		return getExperimentTreatmentKey() + runPrefix + getCurrentRun() + maxgenPrefix + getAllMaxIteration();
	}

	@Override
	public String getIndicatorFirstColumnName() {
		// TODO Auto-generated method stub
		return "ProblemSize_InstanceId_ProblemType_AlgorithmType_Run_Maxgen_Gen";
	}

	@Override
	public String getSubfolderOutputPath() {
		// TODO Auto-generated method stub
		return subfolderOutputPath;
	}

	@Override
	public void setSubfolderOutputPath(String subOutputPath) {
		// TODO Auto-generated method stub
		this.subfolderOutputPath = subOutputPath;
	}

	@Override
	public String getAnalysisPrefixFileNamePath() {
		// TODO Auto-generated method stub
		return this.subfolderOutputPath;
	}

	@Override
	public void setProblemList(String[] problems) {
		// TODO Auto-generated method stub
		Arrays.asList(problems).stream().forEach(problem -> {
			
			switch (problem.trim().toLowerCase()) {
			case "binary":
				problemTypeList.add(KnapsackRepresentationProblemType.BINARY);
				break;
			case "permutation":
				problemTypeList.add(KnapsackRepresentationProblemType.PERMUTATION);
				break;
			default:
				break;
			}			
		});		
	}

	@Override
	public KnapsackRepresentationProblemType getKnapsackRepresentationProblemType() {
		// TODO Auto-generated method stub
		return problemType;
	}

	@Override
	public void setKnapsackRepresentationProblemType(KnapsackRepresentationProblemType problem) {
		// TODO Auto-generated method stub
		this.problemType = problem;
	}

	@Override
	public List<KnapsackRepresentationProblemType> getKnapsackRepresentationProblemTypeList() {
		// TODO Auto-generated method stub
		return problemTypeList;
	}

	@Override
	public String getAverageIndicatorKey(String treatmentKey) {
		// TODO Auto-generated method stub
		return treatmentKey + runAvgALLSubfix + maxgenPrefix + getAllMaxIteration();
	}
}
