package com.kmutt.sit.mop.runner.dtlz;

import java.util.List;

import org.apache.log4j.Logger;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;

import com.kmutt.sit.mop.manager.dtlz.DtlzNsgaIIIConfiguration;
import com.kmutt.sit.mop.pareto.collection.proprietary.DtlzParetoIndicator;

public class DtlzNsgaIIIRunner extends NsgaIIIGeneralRunner<DoubleSolution> {

	private Logger logger = Logger.getLogger(DtlzNsgaIIIRunner.class);

	private Problem<DoubleSolution> problem;
	private Algorithm<List<DoubleSolution>> algorithm;
	private CrossoverOperator<DoubleSolution> crossover;
	private MutationOperator<DoubleSolution> mutation;
	private SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;
	
	private DtlzParetoIndicator<DoubleSolution> paretoIndicator;
	
	private List<DoubleSolution> paretoSolutions;
	private long computingTime;

	public DtlzNsgaIIIRunner() {
		
	}

	public DtlzNsgaIIIRunner setupRunner(DtlzNsgaIIIConfiguration configuration) {
		
		this.config = configuration;
		
		setLogPropertyRunner(this.config.getProblemName() + "_run" + this.config.getCurrentRun());
		
		problem = ProblemUtils.<DoubleSolution> loadProblem(this.config.getProblemClass());

		double crossoverProbability = 1.0;
		double crossoverDistributionIndex = 30.0;
		crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

		double mutationProbability = 1.0 / problem.getNumberOfVariables();
		double mutationDistributionIndex = 20.0;
		mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

		selection = new BinaryTournamentSelection<DoubleSolution>();

		return this;
	}

	public void execute() {
		
		logger.debug("");
		logger.info("DtlzNsgaIIIRunner executing... : Run: " + this.config.getCurrentRun());

		algorithm = new NSGAIIIBuilder<>(problem)
						.setCrossoverOperator(crossover)
						.setMutationOperator(mutation)
						.setSelectionOperator(selection)
						.setMaxIterations(this.config.getMaxIteration())
						.build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		paretoSolutions = algorithm.getResult();
		computingTime = algorithmRunner.getComputingTime();
		
		String key = this.config.getProblemName() + "_it" + this.config.getMaxIteration() + "_run" + this.config.getCurrentRun();
		
		paretoIndicator = new DtlzParetoIndicator<DoubleSolution>()
							.setPareto(paretoSolutions)
							.setParetoKey(key)
							.setRawData(true);
		
		paretoIndicator.setParetoFrontFile(this.config.getReferenceParetoFront());

		paretoIndicator.evaluateIndicator(true);
		
		logger.debug("");
		logger.info("No of Pareto  : " + paretoSolutions.size());
		logger.info("Computing Time: " + computingTime);

		logger.debug("");
		logger.info("DtlzNsgaIIIRunner Finished... : Run: " + this.config.getCurrentRun());
	}

	public List<DoubleSolution> getParetoSolutions() {
		return paretoSolutions;
	}

	public long getComputingTime() {
		return computingTime;
	}
	
	public DtlzParetoIndicator<DoubleSolution> getParetoIndicator(){
		return paretoIndicator;
	}
}
