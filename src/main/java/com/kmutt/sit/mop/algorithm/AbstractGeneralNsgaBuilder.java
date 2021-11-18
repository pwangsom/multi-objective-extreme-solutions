package com.kmutt.sit.mop.algorithm;

import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

import com.kmutt.sit.mop.manager.configulations.ConfigurationInterface;

public abstract class AbstractGeneralNsgaBuilder<C extends ConfigurationInterface, S extends Solution<?>> {
	
	protected C config;
	protected NSGAIIIBuilder<S> builder;
	
	protected Problem<S> problem;
	protected CrossoverOperator<S> crossover;
	protected MutationOperator<S> mutation;
	protected SelectionOperator<List<S>, S> selection;
	protected int maxIterations;
	protected int populationSize;
	
	public AbstractGeneralNsgaBuilder() {
	}

	public abstract Algorithm<List<S>> buildAlgorithm(AlgorithmType algorithmType);
	
	public void setConfiguration(C config) {
		this.config = config;
	}

	public void setConfig(C config) {
		this.config = config;
	}

	public void setBuilder(NSGAIIIBuilder<S> builder) {
		this.builder = builder;
	}

	public void setProblem(Problem<S> problem) {
		this.problem = problem;
	}

	public void setCrossover(CrossoverOperator<S> crossover) {
		this.crossover = crossover;
	}

	public void setMutation(MutationOperator<S> mutation) {
		this.mutation = mutation;
	}

	public void setSelection(SelectionOperator<List<S>, S> selection) {
		this.selection = selection;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}
}
