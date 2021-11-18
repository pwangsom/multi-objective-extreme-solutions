package com.kmutt.sit.mop.problem.knapsack;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import com.kmutt.sit.mop.manager.knapsack.KnapsackConfigurationInterface;

public class KnapsackBinaryIntegerProblem extends AbstractIntegerProblem implements ConstrainedProblem<IntegerSolution> {
	
	private Logger logger = Logger.getLogger(KnapsackBinaryIntegerProblem.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final private int NO_OBJECTIVES = 3;
	final private int NO_CONSTRAINTS = 1;
	
	public OverallConstraintViolation<IntegerSolution> overallConstraintViolationDegree;
	public NumberOfViolatedConstraints<IntegerSolution> numberOfViolatedConstraints;

	private KnapsackConfigurationInterface config;
	private KnapsackDataset dataset;
	
	public KnapsackBinaryIntegerProblem(KnapsackConfigurationInterface config) {
		
		setNumberOfObjectives(NO_OBJECTIVES);
	    setNumberOfConstraints(NO_CONSTRAINTS);
	    
		setName("KnapsackConstrainedBinaryProblem");

		this.config = config;
		this.dataset = this.config.getDataset();

		setNumberOfVariables(this.dataset.getSize());
		
		List<Integer> lowerLimit = new ArrayList<>(getNumberOfVariables());
		List<Integer> upperLimit = new ArrayList<>(getNumberOfVariables());

		int lower = 0;
		int upper = 1;

		for (int i = 0; i < getNumberOfVariables(); i++) {
			lowerLimit.add(lower);
			upperLimit.add(upper);
		}

		setLowerLimit(lowerLimit);
		setUpperLimit(upperLimit);
		
	    overallConstraintViolationDegree = new OverallConstraintViolation<IntegerSolution>() ;
	    numberOfViolatedConstraints = new NumberOfViolatedConstraints<IntegerSolution>() ;
	}

	@Override
	public void evaluate(IntegerSolution solution) {
		// TODO Auto-generated method stub
		logger.debug("");
		logger.debug("Evaluate solution starting...");
		
		KnapsackBinaryIntegerEvaluator evaluator = new KnapsackBinaryIntegerEvaluator(dataset, solution);
		evaluator.evaluate();
		
		solution.setObjective(0, evaluator.getFirstObjectiveValue());
		solution.setObjective(1, evaluator.getSecondObjectiveValue());
		solution.setObjective(2, evaluator.getThirdObjectiveValue());

		logger.debug("Solution : " + evaluator.getSolutionList());
		logger.debug("Weight   : " + dataset.getWeightCapacity() + " - " + evaluator.getWeight() + " = " + (dataset.getWeightCapacity() - evaluator.getWeight()));
		logger.debug("Objective: " + evaluator.getFirstObjectiveValue() + ", " + evaluator.getSecondObjectiveValue() + ", " + evaluator.getThirdObjectiveValue());
		logger.debug("");
		logger.debug("Evaluate solution finished...");
	}

	@Override
	public void evaluateConstraints(IntegerSolution solution) {
		// TODO Auto-generated method stub
		
		KnapsackBinaryIntegerEvaluator evaluator = new KnapsackBinaryIntegerEvaluator(dataset, solution);
		evaluator.evaluate();
		
	    double overallConstraintViolation = 0.0;
	    int violatedConstraints = 0;
		
		if (evaluator.isViolateWeightCapacity()) {
			overallConstraintViolation += 1.0;
			violatedConstraints = 1;

			logger.debug("");
			logger.debug("Weight Error: " + dataset.getWeightCapacity() + " - " + evaluator.getWeight() + " = " + (dataset.getWeightCapacity() - evaluator.getWeight()));
			logger.debug("");
		}

	    overallConstraintViolationDegree.setAttribute(solution, overallConstraintViolation);
	    numberOfViolatedConstraints.setAttribute(solution, violatedConstraints);
	}

}
