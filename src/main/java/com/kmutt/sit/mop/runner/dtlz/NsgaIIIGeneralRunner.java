package com.kmutt.sit.mop.runner.dtlz;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.uma.jmetal.solution.Solution;

import com.kmutt.sit.mop.manager.dtlz.DtlzNsgaIIIConfiguration;
import com.kmutt.sit.mop.pareto.collection.proprietary.DtlzParetoIndicator;

public class NsgaIIIGeneralRunner<S extends Solution<?>> {
	
	private Logger logger = Logger.getLogger(NsgaIIIGeneralRunner.class);
	
	protected DtlzNsgaIIIConfiguration config;
	protected List<DtlzParetoIndicator<S>> paretoIndicatorList;
	
	protected int currentInterval = 0;
	
	public NsgaIIIGeneralRunner() {
		paretoIndicatorList = new ArrayList<DtlzParetoIndicator<S>>();
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

	public List<DtlzParetoIndicator<S>> getParetoIndicatorList() {
		return paretoIndicatorList;
	}
	
	public boolean isNotFinishedRunner() {
		return currentInterval < this.config.getMaxIteration();
	}
	
}
