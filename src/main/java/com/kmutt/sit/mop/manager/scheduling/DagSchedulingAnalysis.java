package com.kmutt.sit.mop.manager.scheduling;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;

import com.kmutt.sit.mop.algorithm.AlgorithmType;
import com.kmutt.sit.mop.output.DagSchedulingIntegerSolutionWriteOutput;
import com.kmutt.sit.mop.pareto.collection.ParetoIndicator;
import com.kmutt.sit.mop.pareto.collection.proprietary.DagSchedulingPopulationOfGeneration;
import com.kmutt.sit.mop.pareto.collection.proprietary.DagSchedulingPopulationOfGenerationCollection;
import com.kmutt.sit.utils.JavaHelper;
import com.kmutt.sit.workflow.ClusterType;

public class DagSchedulingAnalysis {
	
	private Logger logger = Logger.getLogger(DagSchedulingAnalysis.class);
	
	private DagSchedulingConfiguration config;	
	private DagSchedulingPopulationOfGenerationCollection<IntegerSolution> populationOfGenerationCollection;
	
	private List<IntegerSolution> paretoSet;
	private List<IntegerSolution> normalizedParetoSetInteger;
/*	@SuppressWarnings("unused")
	// unnecessary private List<PointSolution> normalizedParetoSetPoint;*/

    private Front referenceFront;
    private Front normalizedReferenceFront;
    private FrontNormalizer frontNormalizer;
    private Map<String, ParetoIndicator<IntegerSolution>> indicatorMapping;
    
    private String experimentOutputKey;
	
	public DagSchedulingAnalysis(DagSchedulingPopulationOfGenerationCollection<IntegerSolution> populationOfGenerationCollection, DagSchedulingConfiguration config) {
		this.populationOfGenerationCollection = populationOfGenerationCollection;
		this.config = config;
		indicatorMapping = new TreeMap<String, ParetoIndicator<IntegerSolution>>();
		setExperimentOutputKey();
	}
	
	private void setExperimentOutputKey() {
		experimentOutputKey = JavaHelper.appendPathName(this.config.getOutputPath(), this.config.getProgramKey(), this.config.getWorkflowName() + "_" + this.config.getWorkflowSize());
	}
	  
	@SuppressWarnings("unchecked")
	public void createReferenceParetoFront() {		
		logger.debug("");
		logger.info("Creating Reference Pareto Front Starting...");		
		
		// populationOfGenerationCollection.sortedListOfPopulationOfGeneration();
		
		paretoSet = populationOfGenerationCollection.getAccommulatedPareto();
		
		referenceFront = new ArrayFront(paretoSet);
		frontNormalizer = new FrontNormalizer(referenceFront);
		
	    normalizedReferenceFront = frontNormalizer.normalize(referenceFront);
		// Front normalizedParetoFront = frontNormalizer.normalize(new ArrayFront(paretoSet));
	    
	    // Method 1
		normalizedParetoSetInteger = (List<IntegerSolution>) frontNormalizer.normalize(paretoSet);
	    // Method 2
	    // unnecessary normalizedParetoSetPoint = FrontUtils.convertFrontToSolutionList(normalizedParetoFront);
	    
	    writeReferenceParetoFile();    
		
		logger.debug("");
		logger.info("Creating Reference Pareto Front Finished...");
	}
	
	public void measureIndicator() {		
		logger.debug("");
		logger.info("Indicator Measurement Starting...");			
		
		measureDefaultIndicator();	
		createParetoByGrouping();
		writeIndicatorFile();
		
		if(logger.isDebugEnabled()) populationOfGenerationCollection.displayMappingGroupping();
		
		logger.debug("");
		logger.info("Indicator Measurement Starting...");		
	}
	
	private void measureDefaultIndicator() {
		logger.debug("");
		logger.info("Default Indicator Measurement Starting...");
		
		int totalSize = populationOfGenerationCollection.getListOfPopulationOfGeneration().size();
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
		
		populationOfGenerationCollection.getListOfPopulationOfGeneration().stream().forEach(set -> {
			if((count[0] % round[0]) == 0) {
				logger.info("Measuring: " + count[0] + " of " + totalSize);
			}
			
			set.setIndicator(referenceFront, normalizedReferenceFront, frontNormalizer);
			set.measureIndicator();
			indicatorMapping.put(set.getPopulationKey(), set.getIndicator());
			
			count[0]++;
		});
		
		logger.info("Measuring: " + totalSize + " of " + totalSize);
		
		writeBigFile();
		
		logger.debug("");
		logger.info("Default Indicator Measurement Finished...");		
	}
	
	private void createParetoByGrouping() {
		
		int[] currentMax = {this.config.getIntervalIteration()};
		
		while(currentMax[0] <= this.config.getAllMaxIteration()) {
			
			this.config.getClusterTypeList().stream().forEach(clusterType -> {
				
				this.config.getAlgorithmTypeList().stream().forEach(algorithm -> {

					logger.debug("");
					logger.info("Cluster Type: " + clusterType + ", Algorithm: " + algorithm.getName() + ", Current Max: " + currentMax[0]);	
					
					createParetoGroupedByCluster(clusterType, algorithm);
					createParetoGroupedByMaxGen(clusterType, algorithm, currentMax[0]);			
					
				});
			});										

			currentMax[0] += this.config.getIntervalIteration();	
		}
			
	}
	
	private void createParetoGroupedByMaxGen(ClusterType cluster, AlgorithmType algorithm, int maxGen) {
		
		String maxGenKey = cluster.getName() + "_" + algorithm.getName() + "_maxgen" + maxGen;
	
		logger.debug("");
		logger.debug("Write file: " + maxGenKey + " Starting...");		
		
		DagSchedulingPopulationOfGeneration<IntegerSolution> pop = new DagSchedulingPopulationOfGeneration<IntegerSolution>();
		
		pop.setAlgorithmType(algorithm);
		pop.setClusterType(cluster);
		pop.setMaxGeneration(maxGen);
		pop.setCurrentRun(999);
		
		pop.setWorkflowName(this.config.getWorkflowName());
		pop.setWorkflowSize(this.config.getWorkflowSize());

		pop.setCurrentGeneration(999);

		List<IntegerSolution> paretoSet = populationOfGenerationCollection.getArchivedParetoOfMaxGenMappingByKey(maxGenKey);	
		
		pop.setParetoSet(paretoSet);		

		pop.setIndicator(referenceFront, normalizedReferenceFront, frontNormalizer);
		pop.measureIndicator();
		
		indicatorMapping.put(pop.getPopulationKey(), pop.getIndicator());
		
		String outputKey = JavaHelper.appendPathName(this.config.getOutputPath(), this.config.getProgramKey(), pop.getPopulationKey());
		
		DagSchedulingIntegerSolutionWriteOutput<IntegerSolution> writer = new DagSchedulingIntegerSolutionWriteOutput<IntegerSolution>();
		writer.writeObjectiveFile(outputKey, pop.getParetoSet());
		// Normalized with all big Pareto front of this experiment
		writer.writeNormalizedObjectiveFile(outputKey + "_all_normalized", pop.getNormalizedParetoSet());	
		
		
		logger.debug("");
		logger.debug("Write file: " + maxGenKey + " Finished...");		
	}
	
	private void createParetoGroupedByCluster(ClusterType cluster, AlgorithmType algorithm) {
		
		String clusterKey = cluster.getName() + "_" + algorithm.getName();
		
		logger.debug("");
		logger.debug("Write file: " + clusterKey + " Starting...");		
		
		DagSchedulingPopulationOfGeneration<IntegerSolution> pop = new DagSchedulingPopulationOfGeneration<IntegerSolution>();
		
		pop.setAlgorithmType(algorithm);
		pop.setClusterType(cluster);
		pop.setMaxGeneration(999);
		pop.setCurrentRun(999);
		
		pop.setWorkflowName(this.config.getWorkflowName());
		pop.setWorkflowSize(this.config.getWorkflowSize());

		pop.setCurrentGeneration(999);

		List<IntegerSolution> paretoSet = populationOfGenerationCollection.getArchivedParetoOfClusterMappingByKey(clusterKey);		
		
		pop.setParetoSet(paretoSet);		

		pop.setIndicator(referenceFront, normalizedReferenceFront, frontNormalizer);
		pop.measureIndicator();
		
		indicatorMapping.put(pop.getPopulationKey(), pop.getIndicator());
		
		String outputKey = JavaHelper.appendPathName(this.config.getOutputPath(), this.config.getProgramKey(), pop.getPopulationKey());
		
		DagSchedulingIntegerSolutionWriteOutput<IntegerSolution> writer = new DagSchedulingIntegerSolutionWriteOutput<IntegerSolution>();
		writer.writeObjectiveFile(outputKey, pop.getParetoSet());
		// Normalized with all big Pareto front of this experiment
		writer.writeNormalizedObjectiveFile(outputKey + "_all_normalized", pop.getNormalizedParetoSet());	
		

		logger.debug("");
		logger.debug("Write file: " + clusterKey + " Finished...");	
	}
	
	private void writeIndicatorFile() {		
		logger.debug("");
		logger.info("Write Indicator File Starting...");
		
		indicatorMapping.putAll(this.populationOfGenerationCollection.getIndicatorMaxGenEachGenAvgMapping());
		
		DagSchedulingIntegerSolutionWriteOutput<IntegerSolution> writer = new DagSchedulingIntegerSolutionWriteOutput<IntegerSolution>();
		writer.writeIndicatorFile(experimentOutputKey + "_a5", indicatorMapping);
		

		logger.debug("");
		logger.info("Write Indicator File Finished...");		
	}
	
	private void writeBigFile() {		
		logger.debug("");
		logger.info("Write Big File Starting...");
		
		String bigPareto = experimentOutputKey + "_a3";
		String bigNormalizedPareto = experimentOutputKey + "_a4_normalized";
		
		DagSchedulingIntegerSolutionWriteOutput<IntegerSolution> writer = new DagSchedulingIntegerSolutionWriteOutput<IntegerSolution>();
		writer.writeParetoSetOfGeneration(bigPareto, populationOfGenerationCollection.getListOfPopulationOfGeneration());
		writer.writeNormalizedParetoSetOfGeneration(bigNormalizedPareto, populationOfGenerationCollection.getListOfPopulationOfGeneration());

		logger.debug("");
		logger.info("Write Big File Finished...");
	}
	
	private void writeReferenceParetoFile() {		
		logger.debug("");
		logger.info("Write Reference Pareto File Starting...");
		
		DagSchedulingIntegerSolutionWriteOutput<IntegerSolution> writer = new DagSchedulingIntegerSolutionWriteOutput<IntegerSolution>();
		writer.writeObjectiveFile(experimentOutputKey, "_a1_referenced_pareto.pf", paretoSet);
		writer.writeObjectiveFile(experimentOutputKey, "_a2_normalized_referenced_pareto.pf", normalizedParetoSetInteger);
		// writer.writeNormalizedObjectiveFile(experimentOutputKey, "_normalized_point_referenced_pareto.pf", normalizedParetoSetPoint);

		logger.debug("");
		logger.info("Write Reference Pareto File Finished...");
	}

}
