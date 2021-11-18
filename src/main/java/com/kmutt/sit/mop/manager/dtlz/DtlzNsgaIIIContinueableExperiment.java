package com.kmutt.sit.mop.manager.dtlz;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.log4j.Logger;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.SolutionListUtils;

import com.kmutt.sit.mop.output.DtlzParetoIndicatorCollectionOutput;
import com.kmutt.sit.mop.pareto.collection.proprietary.DtlzParetoIndicator;
import com.kmutt.sit.mop.pareto.collection.proprietary.DtlzParetoIndicatorCollection;
import com.kmutt.sit.mop.pareto.collection.proprietary.DtlzParetoIndicatorCollectionInterface;
import com.kmutt.sit.mop.runner.dtlz.DtlzNsgaIIIContinueableRunner;

public class DtlzNsgaIIIContinueableExperiment {
	
	private Logger logger = Logger.getLogger(DtlzNsgaIIIContinueableExperiment.class);
	
	private DtlzNsgaIIIConfiguration config;	
	private DtlzParetoIndicatorCollectionInterface<DoubleSolution> paretoCollection;
	
	public DtlzNsgaIIIContinueableExperiment() {
		paretoCollection = new DtlzParetoIndicatorCollection<DoubleSolution>();
	}
	
	public DtlzNsgaIIIContinueableExperiment setConfigurations(DtlzNsgaIIIConfiguration config) {
		this.config = config;
		
		return this;
	}
	
	public void runExperiment() {	
		
		logger.debug("");
		logger.info("Experiment Starting...");
		logger.debug("");
		
		IntStream.range(0, this.config.getMaxRun()).forEach(run -> {						
			this.config.setCurrentRun(run);
			
			DtlzNsgaIIIContinueableRunner runner = new DtlzNsgaIIIContinueableRunner().setupRunner(config);	
			
			runner.execute();
						
			List<DtlzParetoIndicator<DoubleSolution>> paretoList = runner.getParetoIndicatorList();
			
			paretoList.stream().forEach(set -> {
				
				paretoCollection.addParetoSet(set.getKey(), set);	
				
			});		
			
		});	
		
		paretoCollection.generateConsolidatedPareto(this.config.getProblemName() + "_all_true_pareto", this.config.getReferenceParetoFront());

		logger.debug("");
		logger.info("Experiment Finished...");
	}
	
	public void findParetoOfEachInterval() {
		
		int[] currentInterval = {0};
		
		for(currentInterval[0] = config.getIntervalIteration(); currentInterval[0] <= config.getMaxIteration(); currentInterval[0] += config.getIntervalIteration()) {
			
			String intervalKey = config.getProblemName() + "_it" + currentInterval[0] + "_interval";
			List<DoubleSolution> intervalPopulation = new ArrayList<DoubleSolution>();
			
			paretoCollection.getParetoIndicatorMapping().values().stream().filter(f -> f.getKey().contains(config.getProblemName() + "_it" + currentInterval[0])).forEach(set -> {
				intervalPopulation.addAll(set.getPopulation());				
			});	
			
			List<DoubleSolution> intervalPareto = SolutionListUtils.getNondominatedSolutions(intervalPopulation);
			
			DtlzParetoIndicator<DoubleSolution> paretoIndicator = new DtlzParetoIndicator<DoubleSolution>()
																.setPareto(intervalPareto)
																.setParetoKey(intervalKey)
																.setRawData(false);
			
			paretoIndicator.setParetoFrontFile(this.config.getReferenceParetoFront());			
			paretoIndicator.setPrintSolutionFile(false);
			paretoIndicator.evaluateIndicator(true);
			
			paretoCollection.addParetoSet(intervalKey, paretoIndicator);
		}
		
	}
	
	public void writeResult() {
		
		logger.debug("");
		logger.info("Write Output Starting...");
		logger.debug("");
		
		DtlzParetoIndicatorCollectionOutput<DoubleSolution> output = new DtlzParetoIndicatorCollectionOutput<DoubleSolution>(paretoCollection, config);
		
		output.writeParetoSolutionInMapping();
		output.writeAllIndicator(true);
		output.writeAllRawPareto(false);
		
		logger.debug("");
		logger.info("Write Output Finished...");
	}
	
	public DtlzParetoIndicatorCollectionInterface<DoubleSolution> getParetoIndicatorCollection(){
		return paretoCollection;
	}

}
