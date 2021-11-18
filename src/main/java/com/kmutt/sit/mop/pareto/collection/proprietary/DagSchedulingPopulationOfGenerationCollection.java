package com.kmutt.sit.mop.pareto.collection.proprietary;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.log4j.Logger;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionListUtils;

import com.kmutt.sit.mop.pareto.collection.ParetoIndicator;

public class DagSchedulingPopulationOfGenerationCollection<S extends Solution<?>> {
	
	private Logger logger = Logger.getLogger(DagSchedulingPopulationOfGenerationCollection.class);
	
	// unnecessary private Map<String, List<PopulationOfGeneration<S>>> populationOfGenerationMapping;
	private Map<String, List<DagSchedulingPopulationOfGeneration<S>>> popOfGenMaxGenEachGenMapping;
 	
 	private Map<String, ParetoIndicator<S>> indicatorMaxGenEachGenAvgMapping;
 	
	private List<DagSchedulingPopulationOfGeneration<S>> listOfPopulationOfGeneration;
	// unnecessary private List<String> keyList;
	
	private List<S> archivedPareto;	
 	private Map<String, List<S>> archivedParetoOfMaxGenMapping;
 	private Map<String, List<S>> archivedParetoOfClusterMapping;
	
	public DagSchedulingPopulationOfGenerationCollection(){
		// unnecessary populationOfGenerationMapping = new TreeMap<String, List<PopulationOfGeneration<S>>>();
		popOfGenMaxGenEachGenMapping = new TreeMap<String, List<DagSchedulingPopulationOfGeneration<S>>>();
		
		indicatorMaxGenEachGenAvgMapping = new TreeMap<String, ParetoIndicator<S>>();
		listOfPopulationOfGeneration = new ArrayList<DagSchedulingPopulationOfGeneration<S>>();		

		archivedPareto = new ArrayList<S>();
		archivedParetoOfMaxGenMapping = new TreeMap<String, List<S>>();
		archivedParetoOfClusterMapping = new TreeMap<String, List<S>>();
		
		// unnecessary keyList = new ArrayList<String>();
	}
	
	public void addToCollection(String key, List<DagSchedulingPopulationOfGeneration<S>> value, String cluster, String algorithm, int maxGen) {
		
		logger.debug("");
		logger.debug("addToCollection Key: " + key + " Starting...");
		
		// unnecessary keyList.add(key);
		
		// unnecessary populationOfGenerationMapping.put(key, value);
		listOfPopulationOfGeneration.addAll(value);
		
		addToAccomulatedPareto(value, maxGen);
		
		addToClusterMapping(cluster, algorithm, value);
		addToMaxGenMapping(cluster, algorithm, maxGen, value);
		addToMaxGenEachGenMapping(cluster, algorithm, maxGen, value);		
		
		logger.debug("");
		logger.debug("addToCollection Key: " + key + " Finished...");
	}
	
	private void addToAccomulatedPareto(List<DagSchedulingPopulationOfGeneration<S>> value, int maxGen) {

		List<DagSchedulingPopulationOfGeneration<S>> filterValue = value.stream().filter(f -> f.getCurrentGeneration() == maxGen).collect(Collectors.toList());
		List<S> incomingValue = filterValue.stream().flatMap(pop -> pop.getParetoSet().stream()).collect(Collectors.toList());
		
		int currentSize = archivedPareto.size();
		
		archivedPareto.addAll(incomingValue);
		archivedPareto = SolutionListUtils.getNondominatedSolutions(archivedPareto);

		logger.debug("");
		logger.info(String.format("Current + Incoming -> New: %d + %d -> %d", currentSize, incomingValue.size(), archivedPareto.size()));
	}
	
	private void addToMaxGenMapping(String cluster, String algorithm, int maxGen, List<DagSchedulingPopulationOfGeneration<S>> value) {		
		String key = cluster + "_" + algorithm + "_maxgen" + maxGen;
		
		List<DagSchedulingPopulationOfGeneration<S>> filterValue = value.stream().filter(f -> f.getCurrentGeneration() == maxGen).collect(Collectors.toList());
		List<S> incomingValue = filterValue.stream().flatMap(pop -> pop.getParetoSet().stream()).collect(Collectors.toList());
		
		if(archivedParetoOfMaxGenMapping.containsKey(key)) {			
			incomingValue.addAll(archivedParetoOfMaxGenMapping.get(key));
			incomingValue = SolutionListUtils.getNondominatedSolutions(incomingValue);
			
			archivedParetoOfMaxGenMapping.replace(key, incomingValue);		
		} else {
			archivedParetoOfMaxGenMapping.put(key, incomingValue);
		}
	}
	
	private void addToClusterMapping(String cluster, String algorithm, List<DagSchedulingPopulationOfGeneration<S>> value) {		
		String key = cluster + "_" + algorithm;		
		
		List<DagSchedulingPopulationOfGeneration<S>> filterValue = value.stream().filter(f -> f.getCurrentGeneration() == f.getMaxGeneration()).collect(Collectors.toList());
		List<S> incomingValue = filterValue.stream().flatMap(pop -> pop.getParetoSet().stream()).collect(Collectors.toList());
		
		if(archivedParetoOfClusterMapping.containsKey(key)) {			
			incomingValue.addAll(archivedParetoOfClusterMapping.get(key));
			incomingValue = SolutionListUtils.getNondominatedSolutions(incomingValue);
			
			archivedParetoOfClusterMapping.replace(key, incomingValue);		
		} else {
			archivedParetoOfClusterMapping.put(key, incomingValue);
		}
	}
	
	private void addToMaxGenEachGenMapping(String cluster, String algorithm, int maxGen, List<DagSchedulingPopulationOfGeneration<S>> value) {	
		
		IntStream.range(0, maxGen).forEach(gen -> {
			
			int countGen = gen + 1;
			
			String key = cluster + "_" + algorithm + "_maxgen" + maxGen + "_gen" + countGen;
			
			List<DagSchedulingPopulationOfGeneration<S>> filterValue = value.stream().filter(f -> f.getCurrentGeneration() == countGen).collect(Collectors.toList());
			
			if(popOfGenMaxGenEachGenMapping.containsKey(key)) {
				popOfGenMaxGenEachGenMapping.get(key).addAll(filterValue);
			} else {
				popOfGenMaxGenEachGenMapping.put(key, filterValue);
			}
		});
		
	}
	
/*	private Double hvN = 0.0;
	private Double hv = 0.0;
	private Double epsiN = 0.0;
	private Double epsi = 0.0;
	private Double gdN = 0.0;
	private Double gd = 0.0;
	private Double igdN = 0.0;
	private Double igd = 0.0;
	private Double igdpN = 0.0;
	private Double igdp = 0.0;
	private Double spreadN = 0.0;
	private Double spread = 0.0;
	private Double error = 0.0;*/
	
	
	public Map<String, ParetoIndicator<S>> getIndicatorMaxGenEachGenAvgMapping(){
		
		popOfGenMaxGenEachGenMapping.forEach((k,v) -> {
			
			// cybershake50_runAvgALL_maxgen6_gen2
			// cybershake_50_level_ensgaiii_run1_maxgen3_gen1
			
			String key = v.get(0).getWorkflowName() + "_" + v.get(0).getWorkflowSize() + "_"
						+ v.get(0).getClusterType().getName() + "_" + v.get(0).getAlgorithmType().getName()
						+ "_runAvgALL_maxgen" + v.get(0).getMaxGeneration() + "_gen" + v.get(0).getCurrentGeneration();
			
			ParetoIndicator<S> indicator = new ParetoIndicator<S>();
			
			Double instanceSize = (double) v.size();
			
			Double paretoSize = (double) v.stream().flatMap(p -> p.getParetoSet().stream()).count();
			Double hvN = v.stream().mapToDouble(p -> p.getIndicator().getHvN()).sum();
			Double hv = v.stream().mapToDouble(p -> p.getIndicator().getHv()).sum();
			Double epsiN = v.stream().mapToDouble(p -> p.getIndicator().getEpsiN()).sum();
			Double epsi = v.stream().mapToDouble(p -> p.getIndicator().getEpsi()).sum();
			Double gdN = v.stream().mapToDouble(p -> p.getIndicator().getGdN()).sum();
			Double gd = v.stream().mapToDouble(p -> p.getIndicator().getGd()).sum();
			Double igdN = v.stream().mapToDouble(p -> p.getIndicator().getIgdN()).sum();
			Double igd = v.stream().mapToDouble(p -> p.getIndicator().getIgd()).sum();
			Double igdpN = v.stream().mapToDouble(p -> p.getIndicator().getIgdpN()).sum();
			Double igdp = v.stream().mapToDouble(p -> p.getIndicator().getIgdp()).sum();
			Double spreadN = v.stream().mapToDouble(p -> p.getIndicator().getSpreadN()).sum();
			Double spread = v.stream().mapToDouble(p -> p.getIndicator().getSpread()).sum();
			Double error = v.stream().mapToDouble(p -> p.getIndicator().getError()).sum();
			
			paretoSize = paretoSize.compareTo(0.0) == 0? paretoSize : paretoSize/instanceSize;
			hvN = hvN.compareTo(0.0) == 0? hvN : hvN/instanceSize;
			hv = hv.compareTo(0.0) == 0? hv : hv/instanceSize;
			epsiN = epsiN.compareTo(0.0) == 0? epsiN : epsiN/instanceSize;
			epsi = epsi.compareTo(0.0) == 0? epsi : epsi/instanceSize;
			gdN = gdN.compareTo(0.0) == 0? gdN : gdN/instanceSize;
			gd = gd.compareTo(0.0) == 0? gd : gd/instanceSize;
			igdN = igdN.compareTo(0.0) == 0? igdN : igdN/instanceSize;
			igd = igd.compareTo(0.0) == 0? igd : igd/instanceSize;
			igdpN = igdpN.compareTo(0.0) == 0? igdpN : igdpN/instanceSize;
			igdp = igdp.compareTo(0.0) == 0? igdp : igdp/instanceSize;
			spreadN = spreadN.compareTo(0.0) == 0? spreadN : spreadN/instanceSize;
			spread = spread.compareTo(0.0) == 0? spread : spread/instanceSize;
			error = error.compareTo(0.0) == 0? error : error/instanceSize;
			
			indicator.setParetoSize(paretoSize);
			indicator.setHvN(hvN);
			indicator.setHv(hv);
			indicator.setEpsiN(epsiN);
			indicator.setEpsi(epsi);
			indicator.setGdN(gdN);
			indicator.setGd(gd);
			indicator.setGdN(gdN);
			indicator.setIgdN(igdN);
			indicator.setIgd(igd);
			indicator.setIgdpN(igdpN);
			indicator.setIgdp(igdp);
			indicator.setSpreadN(spreadN);
			indicator.setSpread(spread);
			indicator.setError(error);			
			
			indicatorMaxGenEachGenAvgMapping.put(key, indicator);
		});
		
		return indicatorMaxGenEachGenAvgMapping;
	}
	

/*	public Map<String, List<PopulationOfGeneration<S>>> getPopuationOfGenerationMapping() {
		return populationOfGenerationMapping;
	}*/
	
	public List<DagSchedulingPopulationOfGeneration<S>> getListOfPopulationOfGeneration(){
		return listOfPopulationOfGeneration;
	}

	public List<S> getAccommulatedPareto() {
		return archivedPareto;
	}

/*	public List<String> getKeyList() {
		return keyList;
	}*/
	
	public List<DagSchedulingPopulationOfGeneration<S>> getPopulationOfGenerationGroupedMaxGenEachGenByKey(String key){
		return popOfGenMaxGenEachGenMapping.get(key);
	}
	
	public List<S> getArchivedParetoOfMaxGenMappingByKey(String key) {
		return archivedParetoOfMaxGenMapping.get(key);
	}

	public List<S> getArchivedParetoOfClusterMappingByKey(String key) {
		return archivedParetoOfClusterMapping.get(key);
	}

/*	public List<PopulationOfGeneration<S>> getPopulationOfGenerationByKey(String key){
		return populationOfGenerationMapping.get(key);
	}*/
	
	public void sortedListOfPopulationOfGeneration() {
		Comparator<DagSchedulingPopulationOfGeneration<S>> compare = Comparator.comparingInt(DagSchedulingPopulationOfGeneration::getCurrentRun);
		compare.thenComparingInt(DagSchedulingPopulationOfGeneration::getMaxGeneration);
		compare.thenComparingInt(DagSchedulingPopulationOfGeneration::getCurrentGeneration);
		
		listOfPopulationOfGeneration = listOfPopulationOfGeneration.stream().sorted(compare).collect(Collectors.toList());
		
	}
	
	public void displayMappingGroupping() {
		
		displayArchivedParetoClusterMapping();
		displayArchivedParetoMaxGenMapping();
		displayPopOfGenMaxGenEachGenMapping();
		
	}
	
	private void displayPopOfGenMaxGenEachGenMapping() {
	 	
		logger.debug("");
		logger.debug("popOfGenMaxGenEachGenMapping");
		
		popOfGenMaxGenEachGenMapping.forEach((k, v) -> {
			logger.info("Key: " + k);
			v.stream().forEach(p -> {
				logger.info("Runing Key: " + p.getPopulationKey() + ", Pareto Size: " + p.getParetoSet().size());
			});
		});
		
	}
	
	private void displayArchivedParetoMaxGenMapping() {
	 	
		logger.debug("");
		logger.debug("archivedParetoOfMaxGenMapping");
		
		archivedParetoOfMaxGenMapping.forEach((k, v) -> {
			logger.info("Key: " + k + ", Archived Pareto Size: " + v.size());
		});
		
	}
	

	
	private void displayArchivedParetoClusterMapping() {
	 	
		logger.debug("");
		logger.debug("archivedParetoOfClusterMapping");
		
		archivedParetoOfClusterMapping.forEach((k, v) -> {
			logger.info("Key: " + k + ", Archived Pareto Size: " + v.size());
		});
		
	}
}
