package com.kmutt.sit.mop.pareto.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionListUtils;

import com.kmutt.sit.mop.manager.configulations.ConfigurationInterface;

public class ParetoIndicatorInstanceCollection<C extends ConfigurationInterface, S extends Solution<?>> {	
	
	private Logger logger = Logger.getLogger(ParetoIndicatorInstanceCollection.class);
	
	private C config;

	// This variable keeps the archived reference front
	// Reducing the complexity
	protected List<S> archivedRefParetoFrontList;
 	
	// This variable keeps the indicator of each experiment treatment and will be calculated the indicator performance
	protected Map<String, List<ParetoIndicatorInstance<S>>> indicatorInstanceListEachExperimentTreatmentMapping;
	protected Map<String, List<S>> archivedParetoFrontListEachExperimentTreatmentMapping;
		
	public ParetoIndicatorInstanceCollection(C config) {
		this.config = config;
		archivedRefParetoFrontList = new ArrayList<S>();
		indicatorInstanceListEachExperimentTreatmentMapping = new TreeMap<String, List<ParetoIndicatorInstance<S>>>();
		archivedParetoFrontListEachExperimentTreatmentMapping = new TreeMap<String, List<S>>();
	}
	
	public void addInstanceToCollection(String runningKey, String treatmentKey, List<ParetoIndicatorInstance<S>> instanceList) {
		
		logger.debug("");
		logger.debug("Add to collection: " + runningKey + " Starting...");

		addToParetoIndicatorInstanceEachExperimentTreatementCollection(treatmentKey, instanceList);
		addToRefParetoFront(treatmentKey, instanceList);
		
		logger.debug("");
		logger.debug("Add to collection: " + runningKey + " Finished...");
	}
	
	private void addToRefParetoFront(String treatmentKey, List<ParetoIndicatorInstance<S>> instanceList) {
		
		List<ParetoIndicatorInstance<S>> filteredList = instanceList.stream().filter(in -> in.getInstanceGeneration() == config.getAllMaxIteration()).collect(Collectors.toList());
		
		List<S> incomingPareotSet = filteredList.stream().flatMap(pop -> pop.getParetoSet().stream()).collect(Collectors.toList());
		
		int currentSize = archivedRefParetoFrontList.size();
		int commingSize = incomingPareotSet.size();
		
		archivedRefParetoFrontList.addAll(incomingPareotSet);
		archivedRefParetoFrontList = SolutionListUtils.getNondominatedSolutions(incomingPareotSet);	
		
		addToRefParetoFrontEachExperimentTreatment(treatmentKey, incomingPareotSet);
		
		int newSize = archivedRefParetoFrontList.size();		

		logger.debug("");
		logger.info("Reference Pareto Front: " + this.config.getRefParetoFrontKey() + ": " + currentSize + " + " + commingSize + " -> " + newSize);		
		logger.debug("");
	}
	
	private void addToRefParetoFrontEachExperimentTreatment(String treatmentKey, List<S> incomingPareotSet) {	
		
		int currentSize = 0;
		int commingSize = incomingPareotSet.size();
		int newSize = commingSize;
		
		if(archivedParetoFrontListEachExperimentTreatmentMapping.containsKey(treatmentKey)) {			
			
			List<S> currentSet = archivedParetoFrontListEachExperimentTreatmentMapping.get(treatmentKey);
			
			currentSize = currentSet.size();
			
			incomingPareotSet.addAll(currentSet);
			incomingPareotSet = SolutionListUtils.getNondominatedSolutions(incomingPareotSet);
			
			newSize = incomingPareotSet.size();
			
			archivedParetoFrontListEachExperimentTreatmentMapping.replace(treatmentKey, incomingPareotSet);		
		} else {
			archivedParetoFrontListEachExperimentTreatmentMapping.put(treatmentKey, incomingPareotSet);
			
		}
		
		logger.debug("");
		logger.info("Treatment Pareto Set: " + treatmentKey + ": " + currentSize + " + " + commingSize + " -> " + newSize);		
		logger.debug("");
		
	}
	
	private void addToParetoIndicatorInstanceEachExperimentTreatementCollection(String treatmentKey, List<ParetoIndicatorInstance<S>> instanceList) {
		
		int currentSize = 0;
		int commingSize = instanceList.size();
		int newSize = commingSize;

		if(indicatorInstanceListEachExperimentTreatmentMapping.containsKey(treatmentKey)) {

			List<ParetoIndicatorInstance<S>> currentList = indicatorInstanceListEachExperimentTreatmentMapping.get(treatmentKey);

			currentSize = currentList.size();
			
			instanceList.addAll(currentList);			

			newSize = instanceList.size();

			indicatorInstanceListEachExperimentTreatmentMapping.replace(treatmentKey, instanceList);
			
		} else {
			indicatorInstanceListEachExperimentTreatmentMapping.put(treatmentKey, instanceList);
		}
		
		logger.debug("");
		logger.info("Pareto Indicator Instance: " + this.config.getRefParetoFrontKey() + ": " + currentSize + " + " + commingSize + " -> " + newSize);		
		logger.debug("");
		
	}

	public List<S> getArchivedRefParetoFrontList() {
		return archivedRefParetoFrontList;
	}

	public List<ParetoIndicatorInstance<S>> getIndicatorInstanceListEachExperimentTreatmentByTreatmentKey(String treatmentKey) {
		return indicatorInstanceListEachExperimentTreatmentMapping.get(treatmentKey);
	}
	
	public Map<String, List<ParetoIndicatorInstance<S>>> getIndicatorInstanceListEachExperimentTreatmentMapping() {
		return indicatorInstanceListEachExperimentTreatmentMapping;
	}

	public List<S> getArchivedParetoFrontListEachExperimentTreatmentMappingByTreatmentKey(String treatmentKey) {
		return archivedParetoFrontListEachExperimentTreatmentMapping.get(treatmentKey);
	}
	
	public Map<String, List<S>> getArchivedParetoFrontListEachExperimentTreatmentMapping() {
		return archivedParetoFrontListEachExperimentTreatmentMapping;
	}

}
