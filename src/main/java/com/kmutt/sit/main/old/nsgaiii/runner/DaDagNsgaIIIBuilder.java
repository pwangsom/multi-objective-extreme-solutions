package com.kmutt.sit.main.old.nsgaiii.runner;

import java.util.List;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

import com.kmutt.sit.mop.parameter.Configuration;
import com.kmutt.sit.mop.parameter.Global;

public class DaDagNsgaIIIBuilder<S extends Solution<?>> implements AlgorithmBuilder<DaDagNsgaIII<S>> {
	
	// no access modifier means access from classes within the same package
	private Problem<S> problem;
	private int maxIterations;
	private int populationSize;
	private CrossoverOperator<S> crossoverOperator;
	private MutationOperator<S> mutationOperator;
	private SelectionOperator<List<S>, S> selectionOperator;

	private SolutionListEvaluator<S> evaluator;
	
	private String firstObjectiveName = "Cost";
	private String secondObjectiveName = "Makespan";
	private String thirdObjectiveName = "NoOfVMs";

	/** Builder constructor */
	public DaDagNsgaIIIBuilder(Problem<S> problem) {
		this.problem = problem;
		maxIterations = Global.MAX_ITERATION;
		populationSize = Configuration.MAX_POPULATION;
		evaluator = new SequentialSolutionListEvaluator<S>();
	}

	public DaDagNsgaIIIBuilder<S> setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;

		return this;
	}

	public DaDagNsgaIIIBuilder<S> setPopulationSize(int populationSize) {
		this.populationSize = populationSize;

		return this;
	}

	public DaDagNsgaIIIBuilder<S> setCrossoverOperator(CrossoverOperator<S> crossoverOperator) {
		this.crossoverOperator = crossoverOperator;

		return this;
	}

	public DaDagNsgaIIIBuilder<S> setMutationOperator(MutationOperator<S> mutationOperator) {
		this.mutationOperator = mutationOperator;

		return this;
	}

	public DaDagNsgaIIIBuilder<S> setSelectionOperator(SelectionOperator<List<S>, S> selectionOperator) {
		this.selectionOperator = selectionOperator;

		return this;
	}

	public DaDagNsgaIIIBuilder<S> setSolutionListEvaluator(SolutionListEvaluator<S> evaluator) {
		this.evaluator = evaluator;

		return this;
	}
	
	public DaDagNsgaIIIBuilder<S> setObjectiveName(String firstName, String secondName, String thirdName) {
		this.firstObjectiveName = firstName;
		this.secondObjectiveName = secondName;
		this.thirdObjectiveName = thirdName;

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

	public String getFirstObjectiveName() {
		return firstObjectiveName;
	}

	public String getSecondObjectiveName() {
		return secondObjectiveName;
	}

	public String getThirdObjectiveName() {
		return thirdObjectiveName;
	}

	public DaDagNsgaIII<S> build() {
		return new DaDagNsgaIII<>(this);
	}

}
