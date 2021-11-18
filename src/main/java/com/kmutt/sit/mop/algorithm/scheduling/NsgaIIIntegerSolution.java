package com.kmutt.sit.mop.algorithm.scheduling;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;

import com.kmutt.sit.mop.manager.scheduling.DagSchedulingConfiguration;
import com.kmutt.sit.mop.pareto.collection.proprietary.DagSchedulingPopulationOfGeneration;

@SuppressWarnings("serial")
public class NsgaIIIntegerSolution extends NSGAII<IntegerSolution> {
	
	private Logger logger = Logger.getLogger(NsgaIIIntegerSolution.class);
	
	private DagSchedulingConfiguration config;
	
	private String firstObjectiveName = "Cost";
	private String secondObjectiveName = "Makespan";
	private String thirdObjectiveName = "Third";
	
	private List<DagSchedulingPopulationOfGeneration<IntegerSolution>> popOfGenList;
	
	public NsgaIIIntegerSolution(NSGAIIBuilder<IntegerSolution> builder, DagSchedulingConfiguration config, Comparator<IntegerSolution> dominanceComparator) {
		
		this(builder.getProblem(), builder.getMaxIterations(), builder.getPopulationSize(),
			 builder.getCrossoverOperator(), builder.getMutationOperator(), builder.getSelectionOperator(),
			 dominanceComparator, builder.getSolutionListEvaluator());
		// TODO Auto-generated constructor stub
		
		this.config = config;
		
		popOfGenList = new ArrayList<DagSchedulingPopulationOfGeneration<IntegerSolution>>();
		this.thirdObjectiveName = this.config.getThirdObjectiveName();

		logger.debug("");
		logger.info("Algorithm: " + getName());
		logger.info("Population Size: " + maxPopulationSize);
		logger.info("Max Generation : " + maxEvaluations);
		logger.debug("");
	}

	public NsgaIIIntegerSolution(Problem<IntegerSolution> problem, int maxEvaluations, int populationSize,
			CrossoverOperator<IntegerSolution> crossoverOperator, MutationOperator<IntegerSolution> mutationOperator,
			SelectionOperator<List<IntegerSolution>, IntegerSolution> selectionOperator,
			Comparator<IntegerSolution> dominanceComparator, SolutionListEvaluator<IntegerSolution> evaluator) {
		
		super(problem, maxEvaluations, populationSize, crossoverOperator, mutationOperator, selectionOperator,
				dominanceComparator, evaluator);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void initProgress() {
		evaluations = 1;
	}

	@Override
	protected void updateProgress() {
		evaluations++;
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

		pop.setCurrentGeneration(evaluations);
		
		pop.setParetoSet(getResult());
		
		popOfGenList.add(pop);
		
		String generationLog = String.format("[%s, %s, %d]: add Gen %d of %d",
							   this.config.getAlgorithmType().getName(),
							   this.config.getClusterType().getName(),
							   this.config.getCurrentRun(),
							   evaluations, maxEvaluations);
		
		logger.debug("");
		logger.info(generationLog);
		
		return evaluations >= maxEvaluations;
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
