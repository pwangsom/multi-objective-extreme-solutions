package com.kmutt.sit.mop.manager.scheduling;

import java.util.stream.IntStream;

import org.apache.log4j.Logger;
import org.uma.jmetal.solution.IntegerSolution;

import com.kmutt.sit.mop.algorithm.AlgorithmType;
import com.kmutt.sit.mop.pareto.collection.proprietary.DagSchedulingPopulationOfGenerationCollection;
import com.kmutt.sit.mop.runner.scheduling.GeneralRunner;
import com.kmutt.sit.mop.runner.scheduling.MoeadRunner;
import com.kmutt.sit.mop.runner.scheduling.NsgaIIIRunner;
import com.kmutt.sit.mop.runner.scheduling.NsgaIIRunner;

public class DagSchedulingExperiment {
	
	private Logger logger = Logger.getLogger(DagSchedulingExperiment.class);
	
	private DagSchedulingConfiguration config;	
	private DagSchedulingPopulationOfGenerationCollection<IntegerSolution> populationOfGenerationCollection;
	
	private int currentMaxIteration = 0;
	
	public DagSchedulingExperiment() {
		config = new DagSchedulingConfiguration();	
		populationOfGenerationCollection = new DagSchedulingPopulationOfGenerationCollection<IntegerSolution>();
	}

	public DagSchedulingExperiment setConfigurations(DagSchedulingConfiguration config) {
		this.config = config;
		
		return this;
	}
	
	public void runExperiment() {	
		
		logger.debug("");
		logger.info("Experiment Starting...");
				
		IntStream.range(0, this.config.getMaxRun()).forEach(run -> {			
			
			int countRun = run + 1;
			
			currentMaxIteration = 0;
			while(isNotReachMaxIteration()) {
				currentMaxIteration += this.config.getIntervalIteration();
				runExperimentOfEachInterval(currentMaxIteration, countRun);
			}
			
		});

		logger.debug("");
		logger.info("Experiment Finished...");
	}
	
	public DagSchedulingPopulationOfGenerationCollection<IntegerSolution> getPopulationOfGenerationCollection() {
		return populationOfGenerationCollection;
	}
	
	public DagSchedulingConfiguration getConfiguration() {
		return config;
	}
	
	
	private void runExperimentOfEachInterval(int currentMaxIteration, int run) {
		
		logger.debug("");
		logger.info("Run: " + run + " Max Gen: " + currentMaxIteration + " Starting...");	
		
		this.config.getClusterTypeList().stream().forEach(clusterType -> {
			
			this.config.getAlgorithmTypeList().stream().forEach(algorithm -> {
				
				this.config.setClusterType(clusterType);
				this.config.setAlgorithmType(algorithm);
				this.config.setCurrentRun(run);
				this.config.setCurrentMaxIteration(currentMaxIteration);
				
				logger.debug("");
				logger.info(this.config.getRunningKey() + " Starting...");
				
				switch (algorithm) {
				case NSGA_II:
					runNsgaII(algorithm);
					break;
				case NSGA_III:
					runNsgaIII(algorithm);
					break;
				case ENSGA_III:
					runNsgaIII(algorithm);
					break;
				case MOEADD:
					runMoeadd(algorithm);
					break;
				default:
					break;
				}
				
				logger.debug("");
				logger.info(this.config.getRunningKey() + " Finished...");
				
			});
		});

		logger.debug("");
		logger.info("Run: " + run + " Max Gen: " + currentMaxIteration + " Finished...");	
		
	}
	
	private void runNsgaII(AlgorithmType algorithm) {
		GeneralRunner runner = new NsgaIIRunner();
		runner.setConfiguration(config);
		runner.setAlgorithmParameters(algorithm);
		runner.execute();
		
		populationOfGenerationCollection.addToCollection(this.config.getRunningKey(), runner.getListOfPopulationOfGeneration(),
														this.config.getClusterType().getName(),
														this.config.getAlgorithmType().getName(),
														this.config.getCurrentMaxIteration());
		
	}
	
	private void runNsgaIII(AlgorithmType algorithm) {
		GeneralRunner runner = new NsgaIIIRunner();
		runner.setConfiguration(config);
		runner.setAlgorithmParameters(algorithm);
		runner.execute();
		
		populationOfGenerationCollection.addToCollection(this.config.getRunningKey(), runner.getListOfPopulationOfGeneration(),
														this.config.getClusterType().getName(),
														this.config.getAlgorithmType().getName(),
														this.config.getCurrentMaxIteration());
	}
	
	private void runMoeadd(AlgorithmType algorithm) {
		GeneralRunner runner = new MoeadRunner();
		runner.setConfiguration(config);
		runner.setAlgorithmParameters(algorithm);
		runner.execute();

		populationOfGenerationCollection.addToCollection(this.config.getRunningKey(), runner.getListOfPopulationOfGeneration(),
														this.config.getClusterType().getName(),
														this.config.getAlgorithmType().getName(),
														this.config.getCurrentMaxIteration());		
		
	}
	
	private boolean isNotReachMaxIteration() {
		return currentMaxIteration < this.config.getAllMaxIteration();
	}
}
