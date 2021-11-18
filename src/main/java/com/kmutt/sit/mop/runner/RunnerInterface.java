package com.kmutt.sit.mop.runner;

import java.util.List;

import org.uma.jmetal.solution.Solution;

import com.kmutt.sit.mop.pareto.collection.ParetoIndicatorInstance;

public interface RunnerInterface<C, S extends Solution<?>> {
	
	public void execute();
	public void setAlgorithmParameters();	
	public List<ParetoIndicatorInstance<S>> getIndicatorInstanceList();	
	public void setConfiguration(C config);

}
