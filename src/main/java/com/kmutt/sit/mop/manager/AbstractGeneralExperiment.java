package com.kmutt.sit.mop.manager;

import org.uma.jmetal.solution.Solution;

import com.kmutt.sit.mop.manager.configulations.ConfigurationInterface;
import com.kmutt.sit.mop.pareto.collection.ParetoIndicatorInstanceCollection;

public abstract class AbstractGeneralExperiment<C extends ConfigurationInterface, S extends Solution<?>> {	

	protected C config;
	protected ParetoIndicatorInstanceCollection<C, S> instanceCollection;
	
	public abstract void runExperiment();

	public void setConfiguration(C config) {
		this.config = config;
		instanceCollection = new ParetoIndicatorInstanceCollection<C, S>(config);
	}
	
	public ParetoIndicatorInstanceCollection<C, S> getParetoIndicatorInstanceCollection() {
		return instanceCollection;
	}
	
	public C getConfiguration() {
		return config;
	}

}
