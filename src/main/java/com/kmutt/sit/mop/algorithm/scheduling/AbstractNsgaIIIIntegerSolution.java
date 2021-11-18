package com.kmutt.sit.mop.algorithm.scheduling;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIII;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.solution.IntegerSolution;

import com.kmutt.sit.mop.manager.scheduling.DagSchedulingConfiguration;
import com.kmutt.sit.mop.pareto.collection.proprietary.DagSchedulingPopulationOfGeneration;

@SuppressWarnings("serial")
public abstract class AbstractNsgaIIIIntegerSolution extends NSGAIII<IntegerSolution> {
	
	private Logger logger = Logger.getLogger(AbstractNsgaIIIIntegerSolution.class);
	
	protected DagSchedulingConfiguration config;
	
	private String firstObjectiveName = "Cost";
	private String secondObjectiveName = "Makespan";
	private String thirdObjectiveName = "Third";
	
	private List<DagSchedulingPopulationOfGeneration<IntegerSolution>> popOfGenList;
	
	public AbstractNsgaIIIIntegerSolution(NSGAIIIBuilder<IntegerSolution> builder, DagSchedulingConfiguration config) {
		super(builder);
		// TODO Auto-generated constructor stub
		this.config = config;
		popOfGenList = new ArrayList<DagSchedulingPopulationOfGeneration<IntegerSolution>>();
		this.thirdObjectiveName = this.config.getThirdObjectiveName();

		logger.debug("");
		logger.info("Algorithm: " + getName());
		logger.info("Population Size: " + maxPopulationSize);
		logger.info("Max Generation : " + maxIterations);
		logger.debug("");
	}
	
	@Override
	protected void initProgress() {
		iterations = 1;
	}
	
	@Override
	protected boolean isStoppingConditionReached() {
		
		DagSchedulingPopulationOfGeneration<IntegerSolution> pop = new DagSchedulingPopulationOfGeneration<IntegerSolution>();
		
		pop.setAlgorithmType(this.config.getAlgorithmType());
		pop.setClusterType(this.config.getClusterType());
		pop.setMaxGeneration(this.config.getCurrentMaxIteration());
		pop.setCurrentRun(this.config.getCurrentRun());
		
		pop.setWorkflowName(this.config.getWorkflowName());
		pop.setWorkflowSize(this.config.getWorkflowSize());

		pop.setCurrentGeneration(iterations);
		
		pop.setParetoSet(getResult());
		
		popOfGenList.add(pop);
		
		String generationLog = String.format("[%s, %s, %d]: add Gen %d of %d",
							   this.config.getAlgorithmType().getName(),
							   this.config.getClusterType().getName(),
							   this.config.getCurrentRun(),
							   iterations, maxIterations);

		logger.debug("");
		logger.info(generationLog);
				
		return iterations >= maxIterations;
	}
	
	@Override
	protected void updateProgress() {		
		iterations++ ;
	}

	public String getFirstObjectiveName() {
		return firstObjectiveName;
	}

	public void setFirstObjectiveName(String firstObjectiveName) {
		this.firstObjectiveName = firstObjectiveName;
	}

	public String getSecondObjectiveName() {
		return secondObjectiveName;
	}

	public void setSecondObjectiveName(String secondObjectiveName) {
		this.secondObjectiveName = secondObjectiveName;
	}

	public String getThirdObjectiveName() {
		return thirdObjectiveName;
	}

	public void setThirdObjectiveName(String thirdObjectiveName) {
		this.thirdObjectiveName = thirdObjectiveName;
	}

	public List<DagSchedulingPopulationOfGeneration<IntegerSolution>> getListOfPopulationOfGeneration() {
		return popOfGenList;
	}

}
