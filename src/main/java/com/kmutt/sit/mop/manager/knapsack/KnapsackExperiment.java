package com.kmutt.sit.mop.manager.knapsack;

import java.nio.file.Path;
import java.util.stream.IntStream;

import org.apache.log4j.Logger;
import org.uma.jmetal.solution.IntegerSolution;

import com.kmutt.sit.mop.algorithm.AlgorithmType;
import com.kmutt.sit.mop.manager.AbstractGeneralExperiment;
import com.kmutt.sit.mop.problem.knapsack.KnapsackDataset;
import com.kmutt.sit.mop.problem.knapsack.KnapsackRepresentationProblemType;
import com.kmutt.sit.mop.runner.knapsack.KnapsackBinaryIntegerProblemRunner;
import com.kmutt.sit.mop.runner.knapsack.KnapsackPermutationIntegerProblemRunner;

public class KnapsackExperiment extends AbstractGeneralExperiment<KnapsackConfigurationInterface, IntegerSolution> {
	
	private Logger logger = Logger.getLogger(KnapsackExperiment.class);
	
	private Path file;
	
	public KnapsackExperiment(Path file) {
		this.file = file;
	}

	@Override
	public void runExperiment() {
		// TODO Auto-generated method stub
		
		KnapsackDataset dataset = getDataSet(file);

		logger.debug("");
		logger.info("Experiment input file: " + dataset.getSize() + "_" + dataset.getInstanceId() + " Starting...");
		
		this.config.setDataset(dataset);
		this.config.setName(String.valueOf(dataset.getSize()));
		this.config.setId(dataset.getInstanceId());
		
		this.config.getKnapsackRepresentationProblemTypeList().stream().forEach(problem -> {
			
			this.config.setKnapsackRepresentationProblemType(problem);
			
			executeEachProblem(problem);
			
		});

		logger.debug("");
		logger.info("Experiment input file: " + dataset.getSize() + "_" + dataset.getInstanceId() + " Finished...");

	}
	
	private void executeEachProblem(KnapsackRepresentationProblemType problem) {

		logger.debug("");
		logger.info("Problem: " + problem.getName() + " Starting...");	
		
		this.config.getAlgorithmTypeList().stream().forEach(algorithm -> {		

			this.config.setAlgorithmType(algorithm);
			
			executeEachAlgorithm(algorithm);

		});

		logger.debug("");
		logger.info("Problem: " + problem.getName() + " Finished...");	
		
	}
	
	private void executeEachAlgorithm(AlgorithmType algorithm) {

		logger.debug("");
		logger.info("Algorithm: " + algorithm.getName() + " Starting...");	
		
		IntStream.range(0, this.config.getMaxRun()).forEach(run -> {
			
			this.config.setCurrentRun(run);
			
			switch (this.config.getKnapsackRepresentationProblemType()) {
			case BINARY:
				runBinaryIntegerProblemInstance();
				break;
			case PERMUTATION:
				runPermutationBinaryProblemInstance();
				break;
			default:
				break;
			}

		});

		logger.debug("");
		logger.info("Algorithm: " + algorithm.getName() + " Finished...");		

	}
	
	private void runPermutationBinaryProblemInstance() {
		logger.debug("");
		logger.info(this.config.getRunningKey() + " Starting...");
		
		KnapsackPermutationIntegerProblemRunner runner = new KnapsackPermutationIntegerProblemRunner();
		runner.setConfiguration(config);
		runner.setAlgorithmParameters();
		runner.execute();
		
		instanceCollection.addInstanceToCollection(config.getRunningKey(), config.getExperimentTreatmentKey(), runner.getParetoIndicatorInstanceListOfIntegerSolution());

		logger.debug("");
		logger.info(this.config.getRunningKey() + " Finished...");
	}
	
	private void runBinaryIntegerProblemInstance() {
		logger.debug("");
		logger.info(this.config.getRunningKey() + " Starting...");
		
		KnapsackBinaryIntegerProblemRunner runner = new KnapsackBinaryIntegerProblemRunner();
		runner.setConfiguration(config);
		runner.setAlgorithmParameters();
		runner.execute();
		
		instanceCollection.addInstanceToCollection(config.getRunningKey(), config.getExperimentTreatmentKey(), runner.getIndicatorInstanceList());	

		logger.debug("");
		logger.info(this.config.getRunningKey() + " Finished...");
	}
	
	private KnapsackDataset getDataSet(Path file) {
		String fileNamePath = file.toAbsolutePath().toString();
		String fileName = file.getFileName().toString();

		String[] fileProps = fileName.split("\\.")[0].split("_");

		KnapsackDataset mokpDataset = new KnapsackDataset(fileNamePath, Integer.valueOf(fileProps[1]), fileProps[2]);
		mokpDataset.loadDatasetFile();
		logger.info("Get dataset: " + fileName + " -> " + mokpDataset.toString());

		return mokpDataset;
	}
}
