package com.kmutt.sit.mop.manager;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.log4j.Logger;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.point.util.PointSolution;

import com.kmutt.sit.mop.manager.configulations.ConfigurationInterface;
import com.kmutt.sit.mop.output.ParetoIndicatorCollectionWriter;
import com.kmutt.sit.mop.pareto.collection.ParetoIndicator;
import com.kmutt.sit.mop.pareto.collection.ParetoIndicatorInstance;
import com.kmutt.sit.mop.pareto.collection.ParetoIndicatorInstanceCollection;
import com.kmutt.sit.utils.JavaHelper;

public abstract class AbstractGeneralAnalysis<C extends ConfigurationInterface, S extends Solution<?>> {

	private Logger logger = Logger.getLogger(AbstractGeneralAnalysis.class);
	
	protected C config;
	protected ParetoIndicatorInstanceCollection<C, S> instanceCollection;
	Map<String, ParetoIndicator<S>> indicatorInstanceMapping;
	
	public abstract void analyze() throws JMetalException;
	
	public void setAnalysisConfigurations(ParetoIndicatorInstanceCollection<C, S> instanceCollection, C config) {
		this.config = config;
		this.instanceCollection = instanceCollection;
		indicatorInstanceMapping = new TreeMap<String, ParetoIndicator<S>>();
	}
	
	protected void runAnalysis() throws JMetalException {
		
		logger.debug("");
		logger.info("Analyzer Starting...");
		
		List<S> paretoSetList = instanceCollection.getArchivedRefParetoFrontList();
		
		ReferenceFrontNormailizer reference = createRefParetoFrontEachExperimentGroup(paretoSetList);		

		writeReferenceParetoFile(paretoSetList, reference.getNormalizedParetoSet());
		
		instanceCollection.getIndicatorInstanceListEachExperimentTreatmentMapping().forEach((treatmentKey, value) -> {
			
			measureDefaultIndicator(treatmentKey, value, reference.getReferenceFront(), reference.getNormalizedReferenceFront(), reference.getFrontNormalizer());
			
			writeParetoSetInTheSameFile(treatmentKey, value);
			
			calculateAverageIndicatorEachAlgorithm(treatmentKey, value);
			
		});
		
		instanceCollection.getArchivedParetoFrontListEachExperimentTreatmentMapping().forEach((treatmentKey, value) -> {
			
			ParetoIndicatorInstance<S> instance = instanceCollection.getIndicatorInstanceListEachExperimentTreatmentByTreatmentKey(treatmentKey).get(0);
			
			normalizeParetoSetIndicatorEachExperimentTreatment(treatmentKey, value, reference.getReferenceFront(), reference.getNormalizedReferenceFront(), reference.getFrontNormalizer(), instance);
									
		});

		writeIndicatorFile();
		
		logger.debug("");
		logger.info("Analyzer Finished...");
		
	}
	
	protected ReferenceFrontNormailizer createRefParetoFrontEachExperimentGroup(List<S> paretoSet) throws JMetalException {

		logger.debug("");
		logger.info("Create the reference Pareto Front: " + this.config.getRefParetoFrontKey() + " Starting...");
		
		ReferenceFrontNormailizer reference = new ReferenceFrontNormailizer(paretoSet);

		logger.debug("");
		logger.info("Create the reference Pareto Front: " + this.config.getRefParetoFrontKey() + " Finished...");
		
		return reference;

	}
	
	protected void measureDefaultIndicator(String treatmentKey, List<ParetoIndicatorInstance<S>> indicatorInstanceList, Front referenceFront, Front normalizedReferenceFront, FrontNormalizer frontNormalizer) {		
				
		logger.debug("");
		logger.info("Measure default indicator of eache experiment instance: " + treatmentKey + " Starting...");
		
		int totalSize = indicatorInstanceList.size();
		int[] round = {1};
		int[] count = {0};
		
		if(totalSize > 10) {
			if(totalSize > 100) {
				round[0] = (int) Math.ceil(Math.log10(totalSize/10));
			} else {
				round[0] = (int) Math.ceil(Math.log10(totalSize));
			}
			
			round[0] = (int) Math.pow(10, round[0]-1);
		}
		
		logger.info("Indicator Measurement: Total: " + totalSize + ", log every: " + round[0] + " rounds.");
		
		indicatorInstanceList.stream().forEach(set -> {
			if((count[0] % round[0]) == 0) {
				logger.info("Measuring: " + count[0] + " of " + totalSize);
			}
			
			set.setIndicator(referenceFront, normalizedReferenceFront, frontNormalizer);
			set.measureIndicator();
			indicatorInstanceMapping.put(set.getInstanceKey(), set.getIndicator());		
			
			count[0]++;
		});
		
		logger.info("Measuring: " + totalSize + " of " + totalSize);
		
		logger.debug("");
		logger.info("Measure default indicator of eache experiment instance: " + treatmentKey + " Finished...");		
		
	}
	
	protected void calculateAverageIndicatorEachAlgorithm(String treatmentKey, List<ParetoIndicatorInstance<S>> indicatorInstanceList) {
		
		logger.debug("");
		logger.info("Calculate average indicator of each experiment running: " + treatmentKey + " Starting...");

		IntStream.range(0, config.getAllMaxIteration()).forEach(gen -> {

			int countGen = gen + 1;

			String key = config.getAverageIndicatorKey(treatmentKey) + "_gen" + countGen;
			
			List<ParetoIndicatorInstance<S>> filteredByGeneration = indicatorInstanceList.stream().filter(in -> in.getInstanceGeneration() == countGen).collect(Collectors.toList());
			
			indicatorInstanceMapping.put(key, calculateAverageEachGeneration(filteredByGeneration));
			
		});


		logger.debug("");
		logger.info("Calculate average indicator of each experiment running: " + treatmentKey + " Finished...");
	}
	
	protected void normalizeParetoSetIndicatorEachExperimentTreatment(String treatmentKey, List<S> paretoSet, Front referenceFront, Front normalizedReferenceFront, FrontNormalizer frontNormalizer, ParetoIndicatorInstance<S> instance) {		
				
		logger.debug("");
		logger.info("Normalize the Pareto set of eache experiment treatment: " + treatmentKey + " Starting...");
		
		String instanceIndicatorKey = instance.getExperimentTreatmentKey() + "_runALL_maxgenALL_genLast";
		
		ParetoIndicatorInstance<S> instanceIndicator = new ParetoIndicatorInstance<S>();		

		instanceIndicator.setRefParetoFrontKey(instance.getRefParetoFrontKey());
		instanceIndicator.setInstanceKey(instanceIndicatorKey);
		
		instanceIndicator.setExperimentTreatmentKey(instance.getExperimentTreatmentKey());
		instanceIndicator.setInstanceRun(999);

		instanceIndicator.setInstanceGeneration(instance.getInstanceGeneration());
		
		instanceIndicator.setParetoSet(paretoSet);
		
		instanceIndicator.setIndicator(referenceFront, normalizedReferenceFront, frontNormalizer);
		instanceIndicator.measureIndicator();
		
		indicatorInstanceMapping.put(instanceIndicatorKey, instanceIndicator.getIndicator());
		
		writeNormalizedParetoSetFile(instanceIndicatorKey, paretoSet, instanceIndicator.getNormalizedParetoSet());
		
		logger.debug("");
		logger.info("Normalize the Pareto set of eache experiment treatment: " + treatmentKey + " Finished...");		

	}
	
	protected void writeNormalizedParetoSetFile(String fileKey, List<S> paretoSet, List<PointSolution> normalizedParetoSet) {
				
		logger.debug("");
		logger.info("Write the Pareto set: " + fileKey + " Starting...");
		
		String prefix = JavaHelper.appendPathName(this.config.getAnalysisPrefixFileNamePath(), fileKey);
		
		ParetoIndicatorCollectionWriter<S> writer = new ParetoIndicatorCollectionWriter<S>();
		writer.writeParetoSetFile(prefix, "_pareto.out", paretoSet);
		writer.writeNormalizedParetoSetFile(prefix, "_normalized_pareto.out", normalizedParetoSet);	

		logger.debug("");
		logger.info("Write the Pareto set: " + fileKey + " Finished...");
		
	}
	
	protected ParetoIndicator<S> calculateAverageEachGeneration(List<ParetoIndicatorInstance<S>> indicatorInstanceList){
		
		ParetoIndicator<S> indicator = new ParetoIndicator<S>();
		
		Double instanceSize = (double) indicatorInstanceList.size();
		
		Double paretoSize = (double) indicatorInstanceList.stream().flatMap(p -> p.getParetoSet().stream()).count();
		Double hvN = indicatorInstanceList.stream().mapToDouble(p -> p.getIndicator().getHvN()).sum();
		Double hv = indicatorInstanceList.stream().mapToDouble(p -> p.getIndicator().getHv()).sum();
		Double epsiN = indicatorInstanceList.stream().mapToDouble(p -> p.getIndicator().getEpsiN()).sum();
		Double epsi = indicatorInstanceList.stream().mapToDouble(p -> p.getIndicator().getEpsi()).sum();
		Double gdN = indicatorInstanceList.stream().mapToDouble(p -> p.getIndicator().getGdN()).sum();
		Double gd = indicatorInstanceList.stream().mapToDouble(p -> p.getIndicator().getGd()).sum();
		Double igdN = indicatorInstanceList.stream().mapToDouble(p -> p.getIndicator().getIgdN()).sum();
		Double igd = indicatorInstanceList.stream().mapToDouble(p -> p.getIndicator().getIgd()).sum();
		Double igdpN = indicatorInstanceList.stream().mapToDouble(p -> p.getIndicator().getIgdpN()).sum();
		Double igdp = indicatorInstanceList.stream().mapToDouble(p -> p.getIndicator().getIgdp()).sum();
		Double spreadN = indicatorInstanceList.stream().mapToDouble(p -> p.getIndicator().getSpreadN()).sum();
		Double spread = indicatorInstanceList.stream().mapToDouble(p -> p.getIndicator().getSpread()).sum();
		Double error = indicatorInstanceList.stream().mapToDouble(p -> p.getIndicator().getError()).sum();
		
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
		
		return indicator;
	}
	
	protected void writeParetoSetInTheSameFile(String treatmentKey, List<ParetoIndicatorInstance<S>> indicatorInstanceList) {		
		logger.debug("");
		logger.info("Write all Pareto set in the same file " + treatmentKey + " Starting...");
		
		String prefix = JavaHelper.appendPathName(this.config.getAnalysisPrefixFileNamePath(), this.config.getRefParetoFrontKey());
		
		String pareto = prefix + "_a3";
		String normalizedPareto = prefix + "_a4_normalized";		

		ParetoIndicatorCollectionWriter<S> writer = new ParetoIndicatorCollectionWriter<S>();
		writer.writeParetoSetAllGenerationInTheSameFile(pareto, indicatorInstanceList);
		writer.writeNormalizedParetoSetOAllGenerationInTheSameFile(normalizedPareto, indicatorInstanceList);

		logger.debug("");
		logger.info("Write all Pareto set in the same file " + treatmentKey + " Finished...");
	}
	
	protected void writeReferenceParetoFile(List<S> paretoSet, List<S> normalizedParetoSet) {		
		logger.debug("");
		logger.info("Write the reference Pareto file: " + this.config.getRefParetoFrontKey() + " Starting...");
		
		String prefix = JavaHelper.appendPathName(this.config.getAnalysisPrefixFileNamePath(), this.config.getRefParetoFrontKey());
		
		ParetoIndicatorCollectionWriter<S> writer = new ParetoIndicatorCollectionWriter<S>();
		writer.writeParetoSetFile(prefix, "_a1_referenced_pareto.out", paretoSet);
		writer.writeParetoSetFile(prefix, "_a2_normalized_referenced_pareto.out", normalizedParetoSet);

		logger.debug("");
		logger.info("Write the reference Pareto file: " + this.config.getRefParetoFrontKey() + " Finished...");
	}
	
	protected void writeIndicatorFile() {
		
		logger.debug("");
		logger.info("Write the indicator file: " + this.config.getRefParetoFrontKey() + " Starting...");		

		String prefix = JavaHelper.appendPathName(this.config.getAnalysisPrefixFileNamePath(), this.config.getRefParetoFrontKey());
		
		ParetoIndicatorCollectionWriter<S> writer = new ParetoIndicatorCollectionWriter<S>();
		writer.writeIndicatorFile(config.getIndicatorFirstColumnName(), prefix + "_a5", indicatorInstanceMapping);

		logger.debug("");
		logger.info("Write the indicator file: " + this.config.getRefParetoFrontKey() + " Finished...");
	}

	class ReferenceFrontNormailizer {
		
		private Front referenceFront;
		private FrontNormalizer frontNormalizer;
		private Front normalizedReferenceFront;
		private List<S> normalizedParetoSet;

		@SuppressWarnings("unchecked")
		ReferenceFrontNormailizer(List<S> paretoSet) throws JMetalException {

			referenceFront = new ArrayFront(paretoSet);
			frontNormalizer = new FrontNormalizer(referenceFront);
			normalizedReferenceFront = frontNormalizer.normalize(referenceFront);
			normalizedParetoSet = (List<S>) frontNormalizer.normalize(paretoSet);

		}

		Front getReferenceFront() {
			return referenceFront;
		}

		FrontNormalizer getFrontNormalizer() {
			return frontNormalizer;
		}

		Front getNormalizedReferenceFront() {
			return normalizedReferenceFront;
		}

		List<S> getNormalizedParetoSet() {
			return normalizedParetoSet;
		}

	}
}
