package com.kmutt.sit.mop.algorithm.scheduling;

import java.util.Comparator;
import java.util.List;

import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.comparator.DominanceComparator;

import com.kmutt.sit.mop.manager.scheduling.DagSchedulingConfiguration;

public class GeneralNsgaIIIntergerSolutionBuilder {
	
	private NSGAIIBuilder<IntegerSolution> builder;
	
	private DagSchedulingConfiguration config;
	private Comparator<IntegerSolution> dominanceComparator;
	
	private String firstObjectiveName = "Cost";
	private String secondObjectiveName = "Makespan";
	private String thirdObjectiveName = "Third";

	public GeneralNsgaIIIntergerSolutionBuilder(Problem<IntegerSolution> problem, CrossoverOperator<IntegerSolution> crossover, MutationOperator<IntegerSolution> mutation,
			SelectionOperator<List<IntegerSolution>, IntegerSolution> selection, int maxIterations, int populationSize) {
		
		builder = new NSGAIIBuilder<IntegerSolution>(problem, crossover, mutation)
		        .setSelectionOperator(selection)
		        .setMaxEvaluations(maxIterations)
		        .setPopulationSize(populationSize);
		
		dominanceComparator = new DominanceComparator<>();
	}
	
	public GeneralNsgaIIIntergerSolutionBuilder setDagSchedulingConfiguration(DagSchedulingConfiguration config) {
		this.config = config;
		
		return this;
	}
	
	public GeneralNsgaIIIntergerSolutionBuilder setObjectiveName(String first, String second, String third) {
		this.firstObjectiveName = first;
		this.secondObjectiveName = second;
		this.thirdObjectiveName = third;
		
		return this;
	}
	
	public GeneralNsgaIIIntergerSolutionBuilder setThirdObjectiveName(String thirdObjectiveName) {
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
	
	public NsgaIIIntegerSolution buildNsgaIIIntegerSolution(){
		
		NsgaIIIntegerSolution algorithm = new NsgaIIIntegerSolution(builder, config, dominanceComparator);
		algorithm.setThirdObjectiveName(this.thirdObjectiveName);
		
		return algorithm;
	}

}
