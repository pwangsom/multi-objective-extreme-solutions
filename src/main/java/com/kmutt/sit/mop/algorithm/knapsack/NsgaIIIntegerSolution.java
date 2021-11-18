package com.kmutt.sit.mop.algorithm.knapsack;

import java.util.Comparator;
import java.util.List;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;

import com.kmutt.sit.mop.algorithm.AbstractGeneralNsgaII;
import com.kmutt.sit.mop.manager.knapsack.KnapsackConfigurationInterface;

public class NsgaIIIntegerSolution<S extends Solution<?>> extends AbstractGeneralNsgaII<KnapsackConfigurationInterface, S> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NsgaIIIntegerSolution(Problem<S> problem, int maxEvaluations, int populationSize,
			CrossoverOperator<S> crossoverOperator, MutationOperator<S> mutationOperator,
			SelectionOperator<List<S>, S> selectionOperator,
			Comparator<S> dominanceComparator, SolutionListEvaluator<S> evaluator,
			KnapsackConfigurationInterface config) {
		super(problem, maxEvaluations, populationSize, crossoverOperator, mutationOperator, selectionOperator,
				dominanceComparator, evaluator, config);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getAddPopulationInstanceLog() {
		// TODO Auto-generated method stub
		return String.format("[%s, %d]: add Gen %d of %d", this.config.getExperimentTreatmentKey(),
				this.config.getCurrentRun(), evaluations, maxEvaluations);
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
