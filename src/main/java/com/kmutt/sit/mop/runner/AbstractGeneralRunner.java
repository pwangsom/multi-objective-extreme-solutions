package com.kmutt.sit.mop.runner;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.uma.jmetal.solution.Solution;

import com.kmutt.sit.mop.manager.configulations.ConfigurationInterface;
import com.kmutt.sit.mop.output.ParetoIndicatorCollectionWriter;
import com.kmutt.sit.mop.pareto.collection.ParetoIndicatorInstance;

public abstract class AbstractGeneralRunner<C extends ConfigurationInterface, S extends Solution<?>> implements RunnerInterface<C, S>{
	
	private Logger logger = Logger.getLogger(AbstractGeneralRunner.class);	
	
	protected C config;	
	protected List<ParetoIndicatorInstance<S>> indicatorInstanceList;
	protected List<S> paretoSet;

	public abstract void execute();
	public abstract void setAlgorithmParameters();
	
	protected abstract String getFileNamePathOfLastParetoSet();
	protected abstract String getFileNamePathOfAllParetoSet();
	
	public AbstractGeneralRunner() {
		indicatorInstanceList = new ArrayList<ParetoIndicatorInstance<S>>();
	}
	
	public List<ParetoIndicatorInstance<S>> getIndicatorInstanceList() {
		return indicatorInstanceList;
	}
	
	public void setConfiguration(C config) {
		this.config = config;
		setLogPropertyRunner(this.config.getRunningKey());
	}
	
	protected void displayParetoSetReport() {
		String log = String.format("[Treatment: %s, Pareto Size: %d]", config.getExperimentTreatmentKey(), paretoSet.size());
		logger.debug("");
		logger.info(log);
	}
	
	protected void writeParetoSetFiles() {		
		String paretoSetOfLastGeneration = getFileNamePathOfLastParetoSet();
		String paretoSetOfAllGeneration = getFileNamePathOfAllParetoSet();
		
		indicatorInstanceList = indicatorInstanceList.stream().sorted(Comparator.comparingInt(ParetoIndicatorInstance::getInstanceGeneration)).collect(Collectors.toList());
		
		ParetoIndicatorCollectionWriter<S> writer = new ParetoIndicatorCollectionWriter<S>();
		writer.writeParetoSetFile(paretoSetOfLastGeneration, paretoSet);
		writer.writeParetoSetAllGenerationInTheSameFile(paretoSetOfAllGeneration, indicatorInstanceList);
	}
	
	protected void setLogPropertyRunner(String logFileName) {
		String dateTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

		System.setProperty("log4j.runner.current.date.time", dateTime);
		System.setProperty("log4j.runner.filename", logFileName);

		Properties props = new Properties();
		try {
			InputStream configStream = getClass().getResourceAsStream("/log4j.properties");
			props.load(configStream);
			configStream.close();
		} catch (IOException e) {
			logger.error("Error: Cannot load configuration file!!");
		}

		props.setProperty("log4j.runner.filename", logFileName);
		System.setProperty("log4j.runner.current.date.time", dateTime);
		LogManager.resetConfiguration();
		PropertyConfigurator.configure(props);
	}
}
