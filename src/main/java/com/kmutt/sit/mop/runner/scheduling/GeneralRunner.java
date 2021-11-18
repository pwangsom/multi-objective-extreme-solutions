package com.kmutt.sit.mop.runner.scheduling;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.IntegerSolution;

import com.kmutt.sit.mop.algorithm.AlgorithmType;
import com.kmutt.sit.mop.manager.scheduling.DagSchedulingConfiguration;
import com.kmutt.sit.mop.output.DagSchedulingIntegerSolutionWriteOutput;
import com.kmutt.sit.mop.pareto.collection.proprietary.DagSchedulingPopulationOfGeneration;
import com.kmutt.sit.mop.problem.scheduling.DagSchedulingIntegerProblem;
import com.kmutt.sit.utils.JavaHelper;

public abstract class GeneralRunner {
	
	private Logger logger = Logger.getLogger(GeneralRunner.class);
	
	protected DagSchedulingConfiguration config;
	
	protected Problem<IntegerSolution> problem;
	protected CrossoverOperator<IntegerSolution> crossover;
	protected MutationOperator<IntegerSolution> mutation;
	protected SelectionOperator<List<IntegerSolution>, IntegerSolution> selection;
	
	protected String outputRunningKeyPath;
	
	protected List<DagSchedulingPopulationOfGeneration<IntegerSolution>> popOfGenList;
	
	public GeneralRunner() {
		popOfGenList = new ArrayList<DagSchedulingPopulationOfGeneration<IntegerSolution>>();
	}
	
	public void setConfiguration(DagSchedulingConfiguration config) {
		this.config = config;
		outputRunningKeyPath = JavaHelper.appendPathName(this.config.getOutputPath(), this.config.getProgramKey(), this.config.getRunningKey());
		setLogPropertyRunner(this.config.getRunningKey());
	}
	
	public abstract void execute();

	public List<DagSchedulingPopulationOfGeneration<IntegerSolution>> getListOfPopulationOfGeneration() {
		return popOfGenList;
	}
	
	protected void writeOutPutFile(List<IntegerSolution> paretoSet) {		
		String objectiveFile = outputRunningKeyPath + "_gen" + this.config.getCurrentMaxIteration();
		// String solutionFile = outputRunningKeyPath + "_gen" + this.config.getCurrentMaxIteration();
		String allObjectiveFile = outputRunningKeyPath + "_genALL";
		// String allSolutionFile = outputRunningKeyPath + "_genALL";
		
		popOfGenList = popOfGenList.stream().sorted(Comparator.comparingInt(DagSchedulingPopulationOfGeneration::getCurrentGeneration)).collect(Collectors.toList());
		
		DagSchedulingIntegerSolutionWriteOutput<IntegerSolution> writer = new DagSchedulingIntegerSolutionWriteOutput<IntegerSolution>();
		// write the Pareto of experiment instance
		writer.writeObjectiveFile(objectiveFile, paretoSet);
		// write solutions of experiment instance
		// writer.writeSolutionFile(solutionFile, paretoSet);
		
		// write the Pareto of each generation in experiment instance
		writer.writeParetoSetOfGeneration(allObjectiveFile, popOfGenList);
		// write write solutions of each generation in experiment instance
		// writer.writeSolutionOfGeneration(allSolutionFile, popOfGenList);
	}
	
	public void setAlgorithmParameters(AlgorithmType algorithm) {
		
		switch (algorithm) {
		case MOEADD:
			setMoeaddParameters();
			break;
		default:
			setNsgaParameters();
			break;
		}
		
	}
	
	private void setNsgaParameters() {

		problem = new DagSchedulingIntegerProblem(this.config);

		double crossoverProbability = 1.0;
		double crossoverDistributionIndex = 30.0;
		crossover = new IntegerSBXCrossover(crossoverProbability, crossoverDistributionIndex);

		double mutationProbability = 1.0 / problem.getNumberOfVariables();
		double mutationDistributionIndex = 20.0;
		mutation = new IntegerPolynomialMutation(mutationProbability, mutationDistributionIndex);

		selection = new BinaryTournamentSelection<IntegerSolution>();

	}
	
	private void setMoeaddParameters() {
		
		problem = new DagSchedulingIntegerProblem(this.config);

		double crossoverProbability = 1.0;
		double crossoverDistributionIndex = 20.0;
		crossover = new IntegerSBXCrossover(crossoverProbability, crossoverDistributionIndex);

		double mutationProbability = 1.0 / problem.getNumberOfVariables();
		double mutationDistributionIndex = 20.0;
		mutation = new IntegerPolynomialMutation(mutationProbability, mutationDistributionIndex);
		
	}
	
	protected void setLogPropertyRunner(String logFileName) {
		String dateTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

		System.setProperty("log4j.runner.current.date.time", dateTime);
		System.setProperty("log4j.runner.filename", logFileName);

		Properties props = new Properties();
		try {
			InputStream configStream = getClass().getResourceAsStream("/log4j.properties");
			props.load(configStream);
			configStream.close();
		} catch (IOException e) {
			logger.error("Error: Cannot load configuration file!!");
		}

		props.setProperty("log4j.runner.filename", logFileName);
		System.setProperty("log4j.runner.current.date.time", dateTime);
		LogManager.resetConfiguration();
		PropertyConfigurator.configure(props);
	}

}
