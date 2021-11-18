package com.kmutt.sit.mop.runner.scheduling;

import org.apache.log4j.Logger;
import org.uma.jmetal.util.AlgorithmRunner;

import com.kmutt.sit.mop.algorithm.scheduling.GeneralNsgaIIIntergerSolutionBuilder;
import com.kmutt.sit.mop.algorithm.scheduling.NsgaIIIntegerSolution;

public class NsgaIIRunner extends GeneralRunner {
	
	private Logger logger = Logger.getLogger(NsgaIIRunner.class);

	@Override
	public void execute() {
		logger.debug("");
		logger.info("Runner Starting...");

		NsgaIIIntegerSolution algorithm = new GeneralNsgaIIIntergerSolutionBuilder(problem, crossover, mutation, selection,
												this.config.getCurrentMaxIteration(), 92)
												.setDagSchedulingConfiguration(this.config)
												.setThirdObjectiveName(this.config.getThirdObjectiveName())
												.buildNsgaIIIntegerSolution();

		new AlgorithmRunner.Executor(algorithm).execute();
		popOfGenList.addAll(algorithm.getListOfPopulationOfGeneration());
		writeOutPutFile(algorithm.getResult());

		logger.debug("");
		logger.info("Runner Finished...");
	}
}
