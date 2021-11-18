package com.kmutt.sit.mop.algorithm.knapsack;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.solution.PermutationSolution;

import com.kmutt.sit.mop.algorithm.AbstractGeneralNsgaIII;
import com.kmutt.sit.mop.manager.knapsack.KnapsackConfigurationInterface;

public class ExtremeNsgaIIIPermutationIntegerSolution extends AbstractGeneralNsgaIII<KnapsackConfigurationInterface, PermutationSolution<Integer>>  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExtremeNsgaIIIPermutationIntegerSolution(NSGAIIIBuilder<PermutationSolution<Integer>> builder, KnapsackConfigurationInterface config) {
		super(builder, config);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected List<PermutationSolution<Integer>> createInitialPopulation() {
		
		List<PermutationSolution<Integer>> population = new ArrayList<>(getMaxPopulationSize());		
		
		population.addAll(getExtremeSolutions()); // index 0, 1, 2

		int startIdx = 3;
		
		for (int i = startIdx; i < getMaxPopulationSize(); i++) {
			PermutationSolution<Integer> newIndividual = getProblem().createSolution();
			population.add(newIndividual);
		}
		
		return population;
	}
	
	protected List<PermutationSolution<Integer>> getExtremeSolutions() {
		
		List<PermutationSolution<Integer>> extremeSolutions = new ArrayList<>(3);	
		
		PermutationSolution<Integer> extreme1 = getProblem().createSolution();
		PermutationSolution<Integer> extreme2 = getProblem().createSolution();
		PermutationSolution<Integer> extreme3 = getProblem().createSolution();
		
		Integer[] index1 = config.getDataset().getExtremeOrder1();
		Integer[] index2 = config.getDataset().getExtremeOrder2();
		Integer[] index3 = config.getDataset().getExtremeOrder3();		
		
		for(int i = 0; i < getProblem().getNumberOfVariables(); i++) {
			extreme1.setVariableValue(i, index1[i]);
			extreme2.setVariableValue(i, index2[i]);
			extreme3.setVariableValue(i, index3[i]);
		}
		
		extremeSolutions.add(extreme1);
		extremeSolutions.add(extreme2);
		extremeSolutions.add(extreme3);
		
		return extremeSolutions;
	}
	

	@Override
	public String getAddPopulationInstanceLog() {
		// TODO Auto-generated method stub
		return String.format("[%s, %d]: add Gen %d of %d", this.config.getExperimentTreatmentKey(),
				this.config.getCurrentRun(), iterations, maxIterations);
	}
	
	@Override
	public String getName() {
		return "Integer Solution NSGA-III for the Multi-Objective Knapsack";
	}

	@Override
	public String getDescription() {
		return "Nondominated Sorting Genetic Algorithm III for the Multi-Objective Knapsack";
	}

}
