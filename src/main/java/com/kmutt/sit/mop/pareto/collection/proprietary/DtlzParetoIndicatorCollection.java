package com.kmutt.sit.mop.pareto.collection.proprietary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionListUtils;

@SuppressWarnings("serial")
public class DtlzParetoIndicatorCollection<S extends Solution<?>> implements DtlzParetoIndicatorCollectionInterface<S> {
	
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(DtlzParetoIndicatorCollection.class);
	
	private Map<String, DtlzParetoIndicator<S>> paretoIndicatorMapping;
	private List<S> allPareto;	
	private List<String> paretoIndicatorKeyList;
	
	public DtlzParetoIndicatorCollection() {
		paretoIndicatorMapping = new TreeMap<String, DtlzParetoIndicator<S>>();		
		allPareto = new ArrayList<S>();		
		paretoIndicatorKeyList = new ArrayList<String>();
	}

	@Override
	public Map<String, DtlzParetoIndicator<S>> getParetoIndicatorMapping() {
		// TODO Auto-generated method stub
		return paretoIndicatorMapping;
	}

	@Override
	public DtlzParetoIndicator<S> getParetoIndicatorByKey(String paretoKey) {
		// TODO Auto-generated method stub
		return paretoIndicatorMapping.get(paretoKey);
	}

	@Override
	public List<S> getAllPareto() {
		// TODO Auto-generated method stub
		return allPareto;
	}

	@Override
	public List<String> getParetoKeyList() {
		// TODO Auto-generated method stub
		return paretoIndicatorKeyList;
	}
	
	@Override
	public void addParetoSet(String paretoKey, DtlzParetoIndicator<S> pareto) {
		// TODO Auto-generated method stub
		paretoIndicatorKeyList.add(paretoKey);
		paretoIndicatorMapping.put(paretoKey, pareto);
		allPareto.addAll(pareto.getPopulation());
	}

	@Override
	public void generateConsolidatedPareto(String key, String referenceFile) {
		// TODO Auto-generated method stub
		
		List<S> consoldatedPareto = SolutionListUtils.getNondominatedSolutions(allPareto);
		
		DtlzParetoIndicator<S> trueParetoFront = new DtlzParetoIndicator<S>()
											.setPareto(consoldatedPareto)
											.setParetoKey(key)
											.setRawData(false);
		
		trueParetoFront.setParetoFrontFile(referenceFile);
		trueParetoFront.evaluateIndicator(false);
		
		paretoIndicatorKeyList.add(key);
		paretoIndicatorMapping.put(key, trueParetoFront);
	}
}
