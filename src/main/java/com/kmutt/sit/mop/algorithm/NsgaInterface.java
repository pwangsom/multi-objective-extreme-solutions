package com.kmutt.sit.mop.algorithm;

import java.util.List;

import org.uma.jmetal.solution.Solution;

import com.kmutt.sit.mop.manager.configulations.ConfigurationInterface;
import com.kmutt.sit.mop.pareto.collection.ParetoIndicatorInstance;

public interface NsgaInterface<C extends ConfigurationInterface, S extends Solution<?>> {

	String getAddPopulationInstanceLog();

	List<ParetoIndicatorInstance<S>> getIndicatorInstanceList();
}