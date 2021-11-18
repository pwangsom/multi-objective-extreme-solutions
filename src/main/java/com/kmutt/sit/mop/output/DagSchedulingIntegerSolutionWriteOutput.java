package com.kmutt.sit.mop.output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.fileoutput.FileOutputContext;
import org.uma.jmetal.util.point.util.PointSolution;

import com.kmutt.sit.mop.pareto.collection.ParetoIndicator;
import com.kmutt.sit.mop.pareto.collection.proprietary.DagSchedulingPopulationOfGeneration;

public class DagSchedulingIntegerSolutionWriteOutput<S extends Solution<?>> {
	
	private Logger logger = Logger.getLogger(DagSchedulingIntegerSolutionWriteOutput.class);

	private String suffixSolutionFile = "_solution.out";
	private String suffixParetoFile = "_objective.out";
	private String suffixIndicationFile = "_indicator.out";
	
	public void writeIndicatorFile(String fileName, Map<String, ParetoIndicator<S>> indicatorMapping) {
		
		fileName = fileName + suffixIndicationFile;
		
		FileOutputContext context = new DaDagFileOutputContext(fileName);
		BufferedWriter bufferedWriter = context.getFileWriter();
		
		try {
			
			// Print Header
			bufferedWriter.write("Workflow_Size_Cluster_Algorithm_Run_MaxGen_Gen" + context.getSeparator());
			bufferedWriter.write("ParetoSize" + context.getSeparator());
			
			Arrays.asList(ParetoIndicator.indicatorNames).stream().forEach(name ->{
				
				try {
					bufferedWriter.write(name + context.getSeparator());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error(e, e);
				}
				
			});	

			bufferedWriter.newLine();
			
			indicatorMapping.forEach((k, v) -> {
				
				try {
					bufferedWriter.write(k + context.getSeparator());
					bufferedWriter.write(v.getParetoSize() + context.getSeparator());
					
					Arrays.asList(ParetoIndicator.indicatorNames).stream().forEach(name ->{
						
						String indicatorValue = String.format("%.10f", v.getIndicatorByKey(name));
						
						try {
							bufferedWriter.write(indicatorValue + context.getSeparator());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							logger.error(e, e);
						}
						
					});	

					bufferedWriter.newLine();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			});

			bufferedWriter.close();
		} catch (IOException e) {
			logger.error(e, e);
		}
	}
	
	public void writeParetoSetOfGeneration(String fileName, List<DagSchedulingPopulationOfGeneration<IntegerSolution>> popOfGenList) {
		
		fileName = fileName + suffixParetoFile;
		
		FileOutputContext context = new DaDagFileOutputContext(fileName);
		BufferedWriter bufferedWriter = context.getFileWriter();
		
		try {			
			popOfGenList.stream().forEach(set -> {				
				
				List<IntegerSolution> paretoSet = set.getParetoSet();
				
				if (paretoSet.size() > 0) {
					int numberOfObjectives = paretoSet.get(0).getNumberOfObjectives();
					
					for (int i = 0; i < paretoSet.size(); i++) {
						
						try {
							bufferedWriter.write(set.getPopulationKey() + context.getSeparator());
							
							for (int j = 0; j < numberOfObjectives; j++) {
								bufferedWriter.write(paretoSet.get(i).getObjective(j) + context.getSeparator());
							}
							
							bufferedWriter.newLine();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							logger.error(e, e);
						}
						
					}
				}
				
			});

			bufferedWriter.close();
		} catch (IOException e) {
			logger.error(e, e);
		}
	}
	
	public void writeNormalizedParetoSetOfGeneration(String fileName, List<DagSchedulingPopulationOfGeneration<IntegerSolution>> popOfGenList) {
		
		fileName = fileName + suffixParetoFile;
		
		FileOutputContext context = new DaDagFileOutputContext(fileName);
		BufferedWriter bufferedWriter = context.getFileWriter();
		
		try {
			
			popOfGenList.stream().forEach(set -> {				
				
				List<PointSolution> paretoSet = set.getNormalizedParetoSet();
				
				if (paretoSet.size() > 0) {
					int numberOfObjectives = paretoSet.get(0).getNumberOfObjectives();
					for (int i = 0; i < paretoSet.size(); i++) {
						
						try {
							bufferedWriter.write(set.getPopulationKey() + context.getSeparator());
							
							for (int j = 0; j < numberOfObjectives; j++) {
								bufferedWriter.write(paretoSet.get(i).getObjective(j) + context.getSeparator());
							}
							
							bufferedWriter.newLine();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							logger.error(e, e);
						}
						
					}
				}
				
			});

			bufferedWriter.close();
		} catch (IOException e) {
			logger.error(e, e);
		}
	}
	
	public void writeObjectiveFile(String fileName, List<S> paretoSet) {		
		writeObjectiveFile(fileName, suffixParetoFile, paretoSet);
	}
	
	public void writeObjectiveFile(String fileName, String suffix, List<S> paretoSet) {
		
		fileName = fileName + suffix;
		
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
	
	public void writeNormalizedObjectiveFile(String fileName, List<PointSolution> paretoSet) {				
		writeNormalizedObjectiveFile(fileName, suffixParetoFile, paretoSet);
	}
	
	public void writeNormalizedObjectiveFile(String fileName, String suffix, List<PointSolution> paretoSet) {		
		fileName = fileName + suffix;
		
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
	
	public void writeSolutionFile(String fileName, List<S> paretoSet) {
		writeSolutionFile(fileName, suffixSolutionFile, paretoSet);
	}
	
	public void writeSolutionFile(String fileName, String suffix, List<S> paretoSet) {
		
		fileName = fileName + suffix;
		
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
	
	public void writeSolutionOfGeneration(String fileName, List<DagSchedulingPopulationOfGeneration<IntegerSolution>> popOfGenList) {
		
		fileName = fileName + suffixSolutionFile;
		
		FileOutputContext context = new DaDagFileOutputContext(fileName);
		BufferedWriter bufferedWriter = context.getFileWriter();
		
		try {			
			popOfGenList.stream().forEach(set -> {				
				
				List<IntegerSolution> paretoSet = set.getParetoSet();
				
				if (paretoSet.size() > 0) {
					
					int numberOfVaribles = paretoSet.get(0).getNumberOfVariables();
					
					for (int i = 0; i < paretoSet.size(); i++) {
						
						try {
							bufferedWriter.write(set.getPopulationKey() + context.getSeparator());
							
							for (int j = 0; j < numberOfVaribles; j++) {
								bufferedWriter.write(paretoSet.get(i).getVariableValueString(j) + context.getSeparator());
							}
							
							bufferedWriter.newLine();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							logger.error(e, e);
						}
						
					}
				}
				
			});

			bufferedWriter.close();
		} catch (IOException e) {
			logger.error(e, e);
		}
	}

}
