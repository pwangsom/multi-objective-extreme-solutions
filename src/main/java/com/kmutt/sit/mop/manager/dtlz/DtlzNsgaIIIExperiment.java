package com.kmutt.sit.mop.manager.dtlz;

import java.util.stream.IntStream;

import org.apache.log4j.Logger;
import org.uma.jmetal.solution.DoubleSolution;

import com.kmutt.sit.mop.output.DtlzParetoIndicatorCollectionOutput;
import com.kmutt.sit.mop.pareto.collection.proprietary.DtlzParetoIndicator;
import com.kmutt.sit.mop.pareto.collection.proprietary.DtlzParetoIndicatorCollection;
import com.kmutt.sit.mop.pareto.collection.proprietary.DtlzParetoIndicatorCollectionInterface;
import com.kmutt.sit.mop.runner.dtlz.DtlzNsgaIIIRunner;

public class DtlzNsgaIIIExperiment {
	

	private Logger logger = Logger.getLogger(DtlzNsgaIIIExperiment.class);
		
	private DtlzNsgaIIIConfiguration config;	
	private DtlzParetoIndicatorCollectionInterface<DoubleSolution> paretoCollection;
	
	public DtlzNsgaIIIExperiment() {
		paretoCollection = new DtlzParetoIndicatorCollection<DoubleSolution>();
	}
	
	public DtlzNsgaIIIExperiment setConfigurations(DtlzNsgaIIIConfiguration config) {
		this.config = config;
		
		return this;
	}
	
	public void runExperiment() {	
		
		logger.debug("");
		logger.info("Experiment Starting...");
		logger.debug("");
		
		IntStream.range(0, this.config.getMaxRun()).forEach(run -> {						
			this.config.setCurrentRun(run);
			
			DtlzNsgaIIIRunner runner = new DtlzNsgaIIIRunner().setupRunner(config);	
			
			runner.execute();
						
			DtlzParetoIndicator<DoubleSolution> pareto = runner.getParetoIndicator();
			paretoCollection.addParetoSet(pareto.getKey(), pareto);			
			
		});	
		
		paretoCollection.generateConsolidatedPareto(this.config.getProblemName() + "_all_true_pareto", this.config.getReferenceParetoFront());

		logger.debug("");
		logger.info("Experiment Finished...");
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
