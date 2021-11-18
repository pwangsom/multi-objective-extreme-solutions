package com.kmutt.sit.mop.algorithm.knapsack;

import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.solution.Solution;

import com.kmutt.sit.mop.algorithm.AbstractGeneralNsgaIII;
import com.kmutt.sit.mop.manager.knapsack.KnapsackConfigurationInterface;

public class NsgaIIIIntegerSolution<S extends Solution<?>> extends AbstractGeneralNsgaIII<KnapsackConfigurationInterface, S> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NsgaIIIIntegerSolution(NSGAIIIBuilder<S> builder, KnapsackConfigurationInterface config) {
		super(builder, config);
		// TODO Auto-generated constructor stub
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
