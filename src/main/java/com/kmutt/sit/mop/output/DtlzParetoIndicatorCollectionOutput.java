package com.kmutt.sit.mop.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.fileoutput.FileOutputContext;

import com.kmutt.sit.mop.manager.dtlz.DtlzNsgaIIIConfiguration;
import com.kmutt.sit.mop.pareto.collection.proprietary.DtlzParetoIndicator;
import com.kmutt.sit.mop.pareto.collection.proprietary.DtlzParetoIndicatorCollectionInterface;
import com.kmutt.sit.utils.JavaHelper;

public class DtlzParetoIndicatorCollectionOutput<S extends Solution<?>> {
	
	private Logger logger = Logger.getLogger(DtlzParetoIndicatorCollectionOutput.class);
	
	private DtlzParetoIndicatorCollectionInterface<S> collection;
	private DtlzNsgaIIIConfiguration config;

	private String suffixSolutionFile = "_solution.out";
	private String suffixParetoFile = "_objective.out";	
	private String suffixIndicationFile = "_indicator.out";

	public DtlzParetoIndicatorCollectionOutput(DtlzParetoIndicatorCollectionInterface<S> collection, DtlzNsgaIIIConfiguration config) {
		this.collection = collection;
		this.config = config;
		
		createSubfolderOutput();
	}
	
	public void writeParetoSolutionInMapping() {		
		collection.getParetoKeyList().stream().forEach(key -> {
						
			DtlzParetoIndicator<S> paretoIndicator = collection.getParetoIndicatorByKey(key);
			
			if(paretoIndicator.isPrintObjectiveFile()) printObjectiveFile(key, paretoIndicator.getPopulation());
			if(paretoIndicator.isPrintSolutionFile()) printSolutionFile(key, paretoIndicator.getPopulation());
			
		});
	}
	
	private void createSubfolderOutput() {
	    File directory = new File(JavaHelper.appendPathName(this.config.getOutputPath(), this.config.getProgramKey()));
	    
	    if (!directory.exists()){
	        directory.mkdir();
	    }	    
	}
	
	private void printObjectiveFile(String key, List<S> paretoSet) {
		
		String fileName = JavaHelper.appendPathName(this.config.getOutputPath(), this.config.getProgramKey(), key + suffixParetoFile);
		
		FileOutputContext context = new DaDagFileOutputContext(fileName);
		BufferedWriter bufferedWriter = context.getFileWriter();

		try {
			if (paretoSet.size() > 0) {
				int numberOfObjectives = paretoSet.get(0).getNumberOfObjectives();
				for (int i = 0; i < paretoSet.size(); i++) {
					for (int j = 0; j < numberOfObjectives; j++) {
						bufferedWriter.write(paretoSet.get(i).getObjective(j) + context.getSeparator());
					}
					bufferedWriter.newLine();
				}
			}

			bufferedWriter.close();
		} catch (IOException e) {
			logger.error(e, e);
		}
	}
	
	private void printSolutionFile(String key, List<S> paretoSet) {
		
		String fileName = JavaHelper.appendPathName(this.config.getOutputPath(), this.config.getProgramKey(), key + suffixSolutionFile);
		
		FileOutputContext context = new DaDagFileOutputContext(fileName);
		BufferedWriter bufferedWriter = context.getFileWriter();

		try {
			if (paretoSet.size() > 0) {
				int numberOfVariables = paretoSet.get(0).getNumberOfVariables();
				for (int i = 0; i < paretoSet.size(); i++) {
					for (int j = 0; j < numberOfVariables; j++) {
						bufferedWriter.write(paretoSet.get(i).getVariableValueString(j) + context.getSeparator());
					}
					bufferedWriter.newLine();
				}
			}

			bufferedWriter.close();
		} catch (IOException e) {
			logger.error(e, e);
		}
	}
	
	public void writeAllIndicator(boolean isPrintHeader) {
		
		String fileName = JavaHelper.appendPathName(this.config.getOutputPath(), this.config.getProgramKey(), this.config.getProblemName() + "_all" + suffixIndicationFile);
		
		FileOutputContext context = new DaDagFileOutputContext(fileName);
		BufferedWriter bufferedWriter = context.getFileWriter();
		
		try {

			if(collection.getParetoKeyList().size() > 0) {			
				// Print header
				if(isPrintHeader) {
					bufferedWriter.write("key" + context.getSeparator());
					
					Arrays.asList(DtlzParetoIndicator.indicatorNames).stream().forEach(name ->{
						
						try {
							bufferedWriter.write(name.toLowerCase() + context.getSeparator());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							logger.error(e, e);
						}
						
					});					

					bufferedWriter.newLine();
				}
				
				collection.getParetoKeyList().stream().forEach(key -> {
					
					DtlzParetoIndicator<S> paretoIndicator = collection.getParetoIndicatorByKey(key);
					
					try {
						bufferedWriter.write(paretoIndicator.getKey() + context.getSeparator());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						logger.error(e, e);
					}
					
					Arrays.asList(DtlzParetoIndicator.indicatorNames).stream().forEach(name ->{
						
						String indicatorValue = String.format("%.10f", paretoIndicator.getIndicatorByKey(name));
						
						try {
							bufferedWriter.write(indicatorValue + context.getSeparator());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							logger.error(e, e);
						}
						
					});					

					try {
						bufferedWriter.newLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						logger.error(e, e);
					}
					
				});
				
			}

			bufferedWriter.close();
		} catch (IOException e) {
			logger.error(e, e);
		}
	}
	
	public void writeAllRawPareto(boolean isPrintHeader) {
		
		String fileName = JavaHelper.appendPathName(this.config.getOutputPath(), this.config.getProgramKey(), this.config.getProblemName() + "_all_raw" + suffixParetoFile);
		
		FileOutputContext context = new DaDagFileOutputContext(fileName);
		BufferedWriter bufferedWriter = context.getFileWriter();
		
		try {

			if(collection.getParetoKeyList().size() > 0) {			
				// Print header
				if(isPrintHeader) {
					bufferedWriter.write("key" + context.getSeparator());
					bufferedWriter.write("objective1" + context.getSeparator());
					bufferedWriter.write("objective2" + context.getSeparator());
					bufferedWriter.write("objective3" + context.getSeparator());			

					bufferedWriter.newLine();
				}
				
				collection.getParetoIndicatorMapping().values().stream().filter(p -> p.isRawData()).forEach(pareto ->{

					List<S> paretoSet = pareto.getPopulation();
					
					int numberOfObjectives = paretoSet.get(0).getNumberOfObjectives();
					for (int i = 0; i < paretoSet.size(); i++) {						
						try {
							bufferedWriter.write(pareto.getKey() + context.getSeparator());

							for (int j = 0; j < numberOfObjectives; j++) {

								try {
									bufferedWriter.write(paretoSet.get(i).getObjective(j) + context.getSeparator());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									logger.error(e, e);
								}

							}
							bufferedWriter.newLine();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							logger.error(e, e);
						}
					}
					
				});

			bufferedWriter.close();
			
			}
		} catch (IOException e) {
			logger.error(e, e);
		}
	}
	
}
