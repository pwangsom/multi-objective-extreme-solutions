package com.kmutt.sit.mop.algorithm.scheduling;

import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEAD;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.AlgorithmBuilder;

import com.kmutt.sit.mop.manager.scheduling.DagSchedulingConfiguration;

public class GeneralMoeadIntegerSolutionBuilder implements AlgorithmBuilder<AbstractMOEAD<IntegerSolution>> {

	public enum Variant {
		MOEADD
	};

	protected Problem<IntegerSolution> problem;

	/** T in Zhang & Li paper */
	protected int neighborSize;
	/** Delta in Zhang & Li paper */
	protected double neighborhoodSelectionProbability;
	/** nr in Zhang & Li paper */
	protected int maximumNumberOfReplacedSolutions;

	protected MOEAD.FunctionType functionType;

	protected CrossoverOperator<IntegerSolution> crossover;
	protected MutationOperator<IntegerSolution> mutation;
	protected String dataDirectory;

	protected int populationSize;
	protected int resultPopulationSize;

	protected int maxEvaluations;

	protected int numberOfThreads;

	protected Variant moeadVariant;
	
	protected DagSchedulingConfiguration config;

	/** Constructor */
	public GeneralMoeadIntegerSolutionBuilder(Problem<IntegerSolution> problem, GeneralMoeadIntegerSolutionBuilder.Variant moead) {

		this.problem = problem;
		populationSize = 100;
		resultPopulationSize = 92;
		maxEvaluations = 300;
		functionType = MOEAD.FunctionType.PBI;
		neighborhoodSelectionProbability = 0.9;
		maximumNumberOfReplacedSolutions = 5;
		dataDirectory = "";
		neighborSize = 20;
		numberOfThreads = 1;
		moeadVariant = moead;
	}

	/* Getters/Setters */
	public int getNeighborSize() {
		return neighborSize;
	}

	public int getMaxEvaluations() {
		return maxEvaluations;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public int getResultPopulationSize() {
		return resultPopulationSize;
	}

	public String getDataDirectory() {
		return dataDirectory;
	}

	public MutationOperator<IntegerSolution> getMutation() {
		return mutation;
	}

	public CrossoverOperator<IntegerSolution> getCrossover() {
		return crossover;
	}

	public MOEAD.FunctionType getFunctionType() {
		return functionType;
	}

	public int getMaximumNumberOfReplacedSolutions() {
		return maximumNumberOfReplacedSolutions;
	}

	public double getNeighborhoodSelectionProbability() {
		return neighborhoodSelectionProbability;
	}

	public int getNumberOfThreads() {
		return numberOfThreads;
	}

	public GeneralMoeadIntegerSolutionBuilder setPopulationSize(int populationSize) {
		this.populationSize = populationSize;

		return this;
	}

	public GeneralMoeadIntegerSolutionBuilder setResultPopulationSize(int resultPopulationSize) {
		this.resultPopulationSize = resultPopulationSize;

		return this;
	}

	public GeneralMoeadIntegerSolutionBuilder setMaxEvaluations(int maxEvaluations) {
		this.maxEvaluations = maxEvaluations;

		return this;
	}

	public GeneralMoeadIntegerSolutionBuilder setNeighborSize(int neighborSize) {
		this.neighborSize = neighborSize;

		return this;
	}

	public GeneralMoeadIntegerSolutionBuilder setNeighborhoodSelectionProbability(
			double neighborhoodSelectionProbability) {
		this.neighborhoodSelectionProbability = neighborhoodSelectionProbability;

		return this;
	}

	public GeneralMoeadIntegerSolutionBuilder setFunctionType(MOEAD.FunctionType functionType) {
		this.functionType = functionType;

		return this;
	}

	public GeneralMoeadIntegerSolutionBuilder setMaximumNumberOfReplacedSolutions(
			int maximumNumberOfReplacedSolutions) {
		this.maximumNumberOfReplacedSolutions = maximumNumberOfReplacedSolutions;

		return this;
	}

	public GeneralMoeadIntegerSolutionBuilder setCrossover(CrossoverOperator<IntegerSolution> crossover) {
		this.crossover = crossover;

		return this;
	}

	public GeneralMoeadIntegerSolutionBuilder setMutation(MutationOperator<IntegerSolution> mutation) {
		this.mutation = mutation;

		return this;
	}

	public GeneralMoeadIntegerSolutionBuilder setDataDirectory(String dataDirectory) {
		this.dataDirectory = dataDirectory;

		return this;
	}

	public GeneralMoeadIntegerSolutionBuilder setNumberOfThreads(int numberOfThreads) {
		this.numberOfThreads = numberOfThreads;

		return this;
	}
	
	public GeneralMoeadIntegerSolutionBuilder setConfiguration(DagSchedulingConfiguration config) {
		this.config = config;

		return this;
	}

	@Override
	public AbstractMOEAD<IntegerSolution> build() {

		AbstractMOEAD<IntegerSolution> algorithm = null;

		if (moeadVariant.equals(Variant.MOEADD)) {

			algorithm = new MoeaddIntegerSolution(problem, populationSize, resultPopulationSize, maxEvaluations,
					crossover, mutation, functionType, dataDirectory, neighborhoodSelectionProbability,
					maximumNumberOfReplacedSolutions, neighborSize, config);
		}

		return algorithm;
	}
	
	public MoeaddIntegerSolution buildMoeadd() {

		MoeaddIntegerSolution algorithm = null;

		algorithm = new MoeaddIntegerSolution(problem, populationSize, resultPopulationSize, maxEvaluations, crossover,
				mutation, functionType, dataDirectory, neighborhoodSelectionProbability,
				maximumNumberOfReplacedSolutions, neighborSize, config);

		return algorithm;
	}

}
