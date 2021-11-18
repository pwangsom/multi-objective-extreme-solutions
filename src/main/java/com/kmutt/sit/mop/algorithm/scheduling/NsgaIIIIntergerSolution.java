package com.kmutt.sit.mop.algorithm.scheduling;

import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.solution.IntegerSolution;

import com.kmutt.sit.mop.manager.scheduling.DagSchedulingConfiguration;

@SuppressWarnings("serial")
public class NsgaIIIIntergerSolution extends AbstractNsgaIIIIntegerSolution {

	public NsgaIIIIntergerSolution(NSGAIIIBuilder<IntegerSolution> builder, DagSchedulingConfiguration config) {
		super(builder, config);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return "NSGA-III for Integer Solution";
	}

	@Override
	public String getDescription() {
		return "Nondominated Sorting Genetic Algorithm version III for Integer Solution";
	}
}
