package com.kmutt.sit.mop.pareto.collection.proprietary;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.solution.Solution;

public interface DtlzParetoIndicatorCollectionInterface<S extends Solution<?>> extends Serializable {
	
	public Map<String, DtlzParetoIndicator<S>> getParetoIndicatorMapping();	
	public DtlzParetoIndicator<S> getParetoIndicatorByKey(String paretoKey);
	
	public List<S> getAllPareto();	
	public List<String> getParetoKeyList();
	
	public void addParetoSet(String paretoKey, DtlzParetoIndicator<S> pareto);	
	
	public void generateConsolidatedPareto(String key, String referenceFile);
	
}
