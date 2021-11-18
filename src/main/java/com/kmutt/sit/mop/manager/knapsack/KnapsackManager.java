package com.kmutt.sit.mop.manager.knapsack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.uma.jmetal.util.JMetalException;

import com.kmutt.sit.mop.manager.AbstractGeneralManager;
import com.kmutt.sit.utils.JavaHelper;

public class KnapsackManager extends AbstractGeneralManager<KnapsackConfigurationInterface> {

	private Logger logger = Logger.getLogger(KnapsackManager.class);
	
	public KnapsackManager() {
		this.config = new KnapsackConfiguration();
	}
	
	@Override
	public void manage() {
		// TODO Auto-generated method stub

		logger.debug("");
		logger.info("MOP-KNAPSACK Starting...");
		
		try {
			Files.list(Paths.get(this.config.getInputPath())).forEach(file -> {
				
				String[] fileProps = file.getFileName().toString().split("\\.")[0].split("_");
				

				String subOutputPath = this.config.getProgramKey() + "_" + fileProps[1] + "_" + fileProps[2];
				
				createSubfolderOutput(this.config.getOutputPath(), subOutputPath);
				
				subOutputPath = JavaHelper.appendPathName(this.config.getOutputPath(), subOutputPath);
				
				this.config.setSubfolderOutputPath(subOutputPath);				
				
				displayConfigurations();
				
				try {
					manageInputFile(file);					
				} catch (JMetalException e) {
					// TODO: handle exception
					logger.error(e);
				}

			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
		
		logger.debug("");
		logger.info("MOP-KNAPSACK Finished...");
		logger.debug("");
	}
	
	private void manageInputFile(Path file) throws JMetalException {
		
		KnapsackExperiment experimentor = new KnapsackExperiment(file);
		experimentor.setConfiguration(config);
		experimentor.runExperiment();
		
		KnapsackAnalysis analyzer = new KnapsackAnalysis();
		analyzer.setAnalysisConfigurations(experimentor.getParetoIndicatorInstanceCollection(), experimentor.getConfiguration());
		analyzer.analyze();
	}
	
	public void setConfigProblemList(String[] problems) {
		this.config.setProblemList(problems);
	}

	@Override
	public void displayConfigurations() {
		// TODO Auto-generated method stub		
		logger.debug("");
		logger.info("=============Configuration=============");
		logger.debug("");

		logger.info("Program Key    : " + this.config.getProgramKey());
		logger.info("Working Path   : " + this.config.getWorkingPath());
		logger.info("Input Path     : " + this.config.getInputPath());
		logger.info("Output Path    : " + this.config.getSubfolderOutputPath());
		logger.info("Max Run        : " + this.config.getMaxRun());
		logger.info("Max Iteration  : " + this.config.getAllMaxIteration());

		logger.debug("");		
	}
}
