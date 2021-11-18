package com.kmutt.sit.mop.algorithm.scheduling;

import java.util.List;

import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.IntegerSolution;

import com.kmutt.sit.mop.manager.scheduling.DagSchedulingConfiguration;

public class GeneralNsgaIIIIntegerSolutionBuilder {
	
	private NSGAIIIBuilder<IntegerSolution> builder;
	
	private DagSchedulingConfiguration config;
	
	private String firstObjectiveName = "Cost";
	private String secondObjectiveName = "Makespan";
	private String thirdObjectiveName = "Third";

	public GeneralNsgaIIIIntegerSolutionBuilder(Problem<IntegerSolution> problem, CrossoverOperator<IntegerSolution> crossover, MutationOperator<IntegerSolution> mutation,
			SelectionOperator<List<IntegerSolution>, IntegerSolution> selection, int maxIterations) {
		
		builder = new NSGAIIIBuilder<IntegerSolution>(problem)
				.setCrossoverOperator(crossover)
				.setMutationOperator(mutation)
				.setSelectionOperator(selection)
				.setMaxIterations(maxIterations);
	}
	
	public GeneralNsgaIIIIntegerSolutionBuilder setDagSchedulingConfiguration(DagSchedulingConfiguration config) {
		this.config = config;
		
		return this;
	}
	
	public GeneralNsgaIIIIntegerSolutionBuilder setObjectiveName(String first, String second, String third) {
		this.firstObjectiveName = first;
		this.secondObjectiveName = second;
		this.thirdObjectiveName = third;
		
		return this;
	}
	
	public GeneralNsgaIIIIntegerSolutionBuilder setThirdObjectiveName(String thirdObjectiveName) {
		this.thirdObjectiveName = thirdObjectiveName;
		
		return this;
	}

	public String getFirstObjectiveName() {
		return firstObjectiveName;
	}

	public String getSecondObjectiveName() {
		return secondObjectiveName;
	}

	public String getThirdObjectiveName() {
		return thirdObjectiveName;
	}
	
	public NsgaIIIIntergerSolution buildNsgaIIIIntegerSolution() {
		
		NsgaIIIIntergerSolution algorithm = new NsgaIIIIntergerSolution(builder, config);
		algorithm.setThirdObjectiveName(this.thirdObjectiveName);
		
		return algorithm;
	}
	
	public ExtremeNsgaIIIIntegerSolution buildExtremeNsgaIIIIntegerSolution() {
		
		ExtremeNsgaIIIIntegerSolution algorithm = new ExtremeNsgaIIIIntegerSolution(builder, config);
		algorithm.setThirdObjectiveName(this.thirdObjectiveName);		
		
	    return algorithm;
	}
}
