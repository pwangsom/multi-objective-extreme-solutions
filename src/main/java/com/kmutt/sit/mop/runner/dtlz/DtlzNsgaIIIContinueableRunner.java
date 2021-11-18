package com.kmutt.sit.mop.runner.dtlz;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.uma.jmetal.algorithm.Algorithm;
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
import org.uma.jmetal.util.SolutionListUtils;

import com.kmutt.sit.mop.algorithm.dtlz.NsgaIIIContinueableBuilder;
import com.kmutt.sit.mop.manager.dtlz.DtlzNsgaIIIConfiguration;
import com.kmutt.sit.mop.pareto.collection.proprietary.DtlzParetoIndicator;
import com.kmutt.sit.utils.JavaHelper;

public class DtlzNsgaIIIContinueableRunner extends NsgaIIIGeneralRunner<DoubleSolution> {
	
	private Logger logger = Logger.getLogger(DtlzNsgaIIIContinueableRunner.class);

	private Problem<DoubleSolution> problem;
	private CrossoverOperator<DoubleSolution> crossover;
	private MutationOperator<DoubleSolution> mutation;
	private SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;

	public DtlzNsgaIIIContinueableRunner() {
		
	}

	public DtlzNsgaIIIContinueableRunner setupRunner(DtlzNsgaIIIConfiguration configuration) {		
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
		logger.info("DtlzNsgaIIIContinueableRunner executing... : Run: " + this.config.getCurrentRun());
		
		List<DoubleSolution> population = new ArrayList<DoubleSolution>();
		
		while(isNotFinishedRunner()) {
			
			List<DoubleSolution> temp = executeEachInterval(population);
			population.clear();
			population = temp;			
		}
		
		logger.debug("");
		logger.info("DtlzNsgaIIIContinueableRunner Finished... : Run: " + this.config.getCurrentRun());
	}
	
	private List<DoubleSolution> executeEachInterval(List<DoubleSolution> givenPopulation) {
		
		logger.debug("");
		logger.info("DtlzNsgaIIIContinueableRunner executing... : Run: " + this.config.getCurrentRun() + ": Iteration: " + currentInterval);
		
		List<DoubleSolution> initialPopulation = new ArrayList<DoubleSolution>();
		
		if(!JavaHelper.isNull(givenPopulation) || !givenPopulation.isEmpty()) {
			initialPopulation = givenPopulation;
		}

		Algorithm<List<DoubleSolution>> algorithm = new NsgaIIIContinueableBuilder<>(problem)
														.setCrossoverOperator(crossover)
														.setMutationOperator(mutation)
														.setSelectionOperator(selection)
														.setMaxIterations(this.config.getIntervalIteration())
														.setInitialPopulation(initialPopulation)
														.build();
		
		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		List<DoubleSolution> population = algorithm.getResult();
		List<DoubleSolution> paretoSet = SolutionListUtils.getNondominatedSolutions(population);
		Long computingTime = algorithmRunner.getComputingTime();
		
		currentInterval += this.config.getIntervalIteration();
		
		String key = this.config.getProblemName() + "_it" + currentInterval + "_run" + this.config.getCurrentRun();
		
		DtlzParetoIndicator<DoubleSolution> paretoIndicator = new DtlzParetoIndicator<DoubleSolution>()
															.setPareto(paretoSet)
															.setParetoKey(key)
															.setRawData(true);
				
		paretoIndicator.setParetoFrontFile(this.config.getReferenceParetoFront());
		paretoIndicator.evaluateIndicator(true);		
		
		paretoIndicatorList.add(paretoIndicator);
		
		logger.debug("");
		logger.info("No of Pareto  : " + paretoSet.size());
		logger.info("Computing Time: " + computingTime);
		
		
		logger.debug("");
		logger.info("DtlzNsgaIIIContinueableRunner Finished... : Run: " + this.config.getCurrentRun() + ": Iteration: " + currentInterval);
		
		return population;
	}

}
