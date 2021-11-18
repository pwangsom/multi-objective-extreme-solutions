package com.kmutt.sit.mop.runner.knapsack;

import java.util.List;

import org.apache.log4j.Logger;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.AlgorithmRunner;

import com.kmutt.sit.mop.algorithm.NsgaInterface;
import com.kmutt.sit.mop.algorithm.knapsack.NsgaIntegerSolutionBuilder;
import com.kmutt.sit.mop.manager.knapsack.KnapsackConfigurationInterface;
import com.kmutt.sit.mop.problem.knapsack.KnapsackBinaryIntegerProblem;
import com.kmutt.sit.mop.runner.AbstractGeneralRunner;
import com.kmutt.sit.utils.JavaHelper;

public class KnapsackBinaryIntegerProblemRunner extends AbstractGeneralRunner<KnapsackConfigurationInterface, IntegerSolution> {	

	private Logger logger = Logger.getLogger(KnapsackBinaryIntegerProblemRunner.class);
	
	private final int POPULATION_SIZE = 92;

	protected Problem<IntegerSolution> problem;
	protected CrossoverOperator<IntegerSolution> crossover;
	protected MutationOperator<IntegerSolution> mutation;
	protected SelectionOperator<List<IntegerSolution>, IntegerSolution> selection;

	@SuppressWarnings("unchecked")
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		logger.debug("");
		logger.info("Runner: " + config.getRunningKey() + " Starting...");
		
		setAlgorithmParameters();
		
		NsgaIntegerSolutionBuilder builder = new NsgaIntegerSolutionBuilder();
		builder.setConfig(config);
		builder.setProblem(problem);
		builder.setCrossover(crossover);
		builder.setMutation(mutation);
		builder.setSelection(selection);
		builder.setMaxIterations(config.getAllMaxIteration());
		builder.setPopulationSize(POPULATION_SIZE);
		
		
		Algorithm<List<IntegerSolution>> algorithm = builder.buildAlgorithm(config.getAlgorithmType());

		new AlgorithmRunner.Executor(algorithm).execute();
		
		paretoSet = algorithm.getResult();
		
		indicatorInstanceList.addAll(((NsgaInterface<KnapsackConfigurationInterface, IntegerSolution>) algorithm).getIndicatorInstanceList());
		writeParetoSetFiles();
		
		displayParetoSetReport();
		
		logger.debug("");
		logger.info("Runner: " + config.getRunningKey() + " Finished...");
	}

	@Override
	public void setAlgorithmParameters() {
		// TODO Auto-generated method stub
		
		problem = new KnapsackBinaryIntegerProblem(this.config);
		
		double crossoverProbability = 1.0;
		double crossoverDistributionIndex = 30.0;
		crossover = new IntegerSBXCrossover(crossoverProbability, crossoverDistributionIndex);

		double mutationProbability = 1.0 / problem.getNumberOfVariables();
		double mutationDistributionIndex = 20.0;
		mutation = new IntegerPolynomialMutation(mutationProbability, mutationDistributionIndex);

		selection = new BinaryTournamentSelection<IntegerSolution>();		
		
	}

	@Override
	protected String getFileNamePathOfLastParetoSet() {
		// TODO Auto-generated method stub
		return JavaHelper.appendPathName(this.config.getSubfolderOutputPath(), this.config.getRunningKey() + "_gen" + this.config.getAllMaxIteration());
	}

	@Override
	protected String getFileNamePathOfAllParetoSet() {
		// TODO Auto-generated method stub
		return JavaHelper.appendPathName(this.config.getSubfolderOutputPath(), this.config.getRunningKey() + "_genALL");
	}

}
