package com.kmutt.sit.mop.runner.scheduling;

import org.apache.log4j.Logger;
import org.uma.jmetal.util.AlgorithmRunner;

import com.kmutt.sit.mop.algorithm.scheduling.ExtremeNsgaIIIIntegerSolution;
import com.kmutt.sit.mop.algorithm.scheduling.GeneralNsgaIIIIntegerSolutionBuilder;
import com.kmutt.sit.mop.algorithm.scheduling.NsgaIIIIntergerSolution;

public class NsgaIIIRunner extends GeneralRunner {

	private Logger logger = Logger.getLogger(NsgaIIIRunner.class);
	
	@Override
	public void execute() {		
		logger.debug("");
		logger.info("Runner Starting...");
		
		switch (this.config.getAlgorithmType()) {
		case ENSGA_III:
			executeExtremeNsgaIII();
			break;
		default:
			executeNsgaIII();
			break;
		}
		
		logger.debug("");
		logger.info("Runner Finished...");
	}
	
	private void executeExtremeNsgaIII() {
		
		ExtremeNsgaIIIIntegerSolution algorithm = new GeneralNsgaIIIIntegerSolutionBuilder(problem, crossover, mutation, selection, this.config.getCurrentMaxIteration())
													.setDagSchedulingConfiguration(this.config)
													.setThirdObjectiveName(this.config.getThirdObjectiveName())
													.buildExtremeNsgaIIIIntegerSolution();		
		
		new AlgorithmRunner.Executor(algorithm).execute();
		popOfGenList.addAll(algorithm.getListOfPopulationOfGeneration());
		writeOutPutFile(algorithm.getResult());
	}
	
	private void executeNsgaIII() {
		
		NsgaIIIIntergerSolution algorithm = new GeneralNsgaIIIIntegerSolutionBuilder(problem, crossover, mutation, selection, this.config.getCurrentMaxIteration())
													.setDagSchedulingConfiguration(this.config)
													.setThirdObjectiveName(this.config.getThirdObjectiveName())
													.buildNsgaIIIIntegerSolution();
		
		new AlgorithmRunner.Executor(algorithm).execute();
		popOfGenList.addAll(algorithm.getListOfPopulationOfGeneration());
		writeOutPutFile(algorithm.getResult());
		
	}
}
