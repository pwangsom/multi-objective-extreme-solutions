package com.kmutt.sit.mop.runner.scheduling;

import org.apache.log4j.Logger;
import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.util.AlgorithmRunner;

import com.kmutt.sit.mop.algorithm.scheduling.GeneralMoeadIntegerSolutionBuilder;
import com.kmutt.sit.mop.algorithm.scheduling.MoeaddIntegerSolution;

public class MoeadRunner extends GeneralRunner {

	private Logger logger = Logger.getLogger(MoeadRunner.class);

	@Override
	public void execute() {
		// TODO Auto-generated method stub

		logger.debug("");
		logger.info("Runner Starting...");
		
		MoeaddIntegerSolution algorithm = null;
		
		algorithm = new GeneralMoeadIntegerSolutionBuilder(problem, GeneralMoeadIntegerSolutionBuilder.Variant.MOEADD)
		            .setCrossover(crossover)
		            .setMutation(mutation)
		            .setMaxEvaluations(this.config.getCurrentMaxIteration())
		            .setPopulationSize(100)
		            .setResultPopulationSize(92)
		            .setNeighborhoodSelectionProbability(0.9)
		            .setMaximumNumberOfReplacedSolutions(5)
		            .setNeighborSize(20)
		            .setFunctionType(AbstractMOEAD.FunctionType.PBI)
		            .setDataDirectory("MOEAD_Weights")
		            .setConfiguration(config)
		            .buildMoeadd();

		new AlgorithmRunner.Executor(algorithm).execute();
		popOfGenList.addAll(algorithm.getListOfPopulationOfGeneration());
		writeOutPutFile(algorithm.getResult());

		logger.debug("");
		logger.info("Runner Finished...");

	}

}
