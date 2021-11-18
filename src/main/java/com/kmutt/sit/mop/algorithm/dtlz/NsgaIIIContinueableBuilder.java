package com.kmutt.sit.mop.algorithm.dtlz;

import java.util.List;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

import com.kmutt.sit.mop.algorithm.scheduling.AbstractContinueableGeneticAlgorithm;

public class NsgaIIIContinueableBuilder<S extends Solution<?>>
		implements AlgorithmBuilder<AbstractContinueableGeneticAlgorithm<S>> {

	private Problem<S> problem;
	private int maxIterations;
	private int populationSize;
	private CrossoverOperator<S> crossoverOperator;
	private MutationOperator<S> mutationOperator;
	private SelectionOperator<List<S>, S> selectionOperator;
	private List<S> initialPopulation;

	private SolutionListEvaluator<S> evaluator;
	
	public NsgaIIIContinueableBuilder(Problem<S> problem){
	    this.problem = problem;
	    evaluator = new SequentialSolutionListEvaluator<S>();
	}
	
	public NsgaIIIContinueableBuilder<S> setInitialPopulation(List<S> initialPopulation) {
		this.initialPopulation = initialPopulation;

		return this;
	}

	public NsgaIIIContinueableBuilder<S> setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;

		return this;
	}

	public NsgaIIIContinueableBuilder<S> setPopulationSize(int populationSize) {
		this.populationSize = populationSize;

		return this;
	}

	public NsgaIIIContinueableBuilder<S> setCrossoverOperator(CrossoverOperator<S> crossoverOperator) {
		this.crossoverOperator = crossoverOperator;

		return this;
	}

	public NsgaIIIContinueableBuilder<S> setMutationOperator(MutationOperator<S> mutationOperator) {
		this.mutationOperator = mutationOperator;

		return this;
	}

	public NsgaIIIContinueableBuilder<S> setSelectionOperator(SelectionOperator<List<S>, S> selectionOperator) {
		this.selectionOperator = selectionOperator;

		return this;
	}

	public NsgaIIIContinueableBuilder<S> setSolutionListEvaluator(SolutionListEvaluator<S> evaluator) {
		this.evaluator = evaluator;

		return this;
	}

	public SolutionListEvaluator<S> getEvaluator() {
		return evaluator;
	}

	public Problem<S> getProblem() {
		return problem;
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public List<S> getInitialPopulation() {
		return initialPopulation;
	}
	
	public int getPopulationSize() {
		return populationSize;
	}

	public CrossoverOperator<S> getCrossoverOperator() {
		return crossoverOperator;
	}

	public MutationOperator<S> getMutationOperator() {
		return mutationOperator;
	}

	public SelectionOperator<List<S>, S> getSelectionOperator() {
		return selectionOperator;
	}

	@Override
	public AbstractContinueableGeneticAlgorithm<S> build() {
		// TODO Auto-generated method stub
		return new NsgaIIIContinueable<>(this);
	}

}
