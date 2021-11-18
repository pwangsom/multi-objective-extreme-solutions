package com.kmutt.sit.mop.problem.knapsack;

import org.apache.log4j.Logger;
import org.uma.jmetal.problem.impl.AbstractIntegerPermutationProblem;
import org.uma.jmetal.solution.PermutationSolution;

import com.kmutt.sit.mop.manager.knapsack.KnapsackConfigurationInterface;

@SuppressWarnings("serial")
public class KnapsackPermutationIntegerProblem extends AbstractIntegerPermutationProblem {

	private Logger logger = Logger.getLogger(KnapsackPermutationIntegerProblem.class);

	final private int NO_OBJECTIVES = 3;
	
	private int lenght;

	private KnapsackConfigurationInterface config;
	private KnapsackDataset dataset;

	public KnapsackPermutationIntegerProblem(KnapsackConfigurationInterface config) {

		setNumberOfObjectives(NO_OBJECTIVES);
		setName("KnapsackIntegerProblem");

		this.config = config;
		this.dataset = this.config.getDataset();

		lenght = this.dataset.getSize();
		setNumberOfVariables(lenght);
	}

	@Override
	public void evaluate(PermutationSolution<Integer> solution) {
		// TODO Auto-generated method stub

		logger.debug("");
		logger.debug("Evaluate solution starting...");
		
		KnapsackPermutationIntegerEvaluator evaluator = new KnapsackPermutationIntegerEvaluator(dataset, solution);
		evaluator.evaluate();
		
		solution.setObjective(0, evaluator.getFirstObjectiveValue());
		solution.setObjective(1, evaluator.getSecondObjectiveValue());
		solution.setObjective(2, evaluator.getThirdObjectiveValue());		

		logger.debug("Solution -> " + evaluator.getSolutionList());
		logger.debug("Weight: " + evaluator.getWeight() + " -> " + evaluator.getFirstObjectiveValue() + ", " + evaluator.getSecondObjectiveValue() + ", " + evaluator.getThirdObjectiveValue());
		logger.debug("");
		logger.debug("Evaluate solution finished...");
	}

	@Override
	public int getPermutationLength() {
		// TODO Auto-generated method stub
		return lenght;
	}

}
