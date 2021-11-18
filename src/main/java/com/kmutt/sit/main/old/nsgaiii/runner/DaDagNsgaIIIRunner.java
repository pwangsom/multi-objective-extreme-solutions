package com.kmutt.sit.main.old.nsgaiii.runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.apache.log4j.Logger;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.qualityindicator.impl.Epsilon;
import org.uma.jmetal.qualityindicator.impl.ErrorRatio;
import org.uma.jmetal.qualityindicator.impl.GenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.qualityindicator.impl.Spread;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.util.PointSolution;

import com.kmutt.sit.main.old.nsgaiii.problem.DaDagThreeObjectives;
import com.kmutt.sit.mop.output.DaDagFileOutputContext;
import com.kmutt.sit.mop.output.DaDagSolutionOutput;
import com.kmutt.sit.mop.parameter.Configuration;
import com.kmutt.sit.mop.parameter.Global;
import com.kmutt.sit.utils.JavaHelper;
import com.kmutt.sit.workflow.GeneralClusterWorkflow;

public class DaDagNsgaIIIRunner extends AbstractAlgorithmRunner {	
	
	private Logger logger = Logger.getLogger(DaDagNsgaIIIRunner.class);
	
	private Problem<IntegerSolution> problem;
	private Algorithm<List<IntegerSolution>> algorithm;
	private CrossoverOperator<IntegerSolution> crossover;
	private MutationOperator<IntegerSolution> mutation;
	private SelectionOperator<List<IntegerSolution>, IntegerSolution> selection;
	private AlgorithmRunner algorithmRunner;

    private String referenceParetoFront = Configuration.REFERENCE_PARETO_FRONT;
	
	public DaDagNsgaIIIRunner() {
		
	}
	
	public void setupRunner(GeneralClusterWorkflow workflow) {
		
		problem = new DaDagThreeObjectives(workflow);

		double crossoverProbability = Configuration.CROSSOVER_PROBABILITY;
		double crossoverDistributionIndex = Configuration.CROSSOVER_DISTRIBUTION_INDEX;
		crossover = new IntegerSBXCrossover(crossoverProbability, crossoverDistributionIndex);

		double mutationProbability = Configuration.MUTATION_PROBABILITY / problem.getNumberOfVariables();
		double mutationDistributionIndex = Configuration.MUTATION_DISTRIBUTION_INDEX;
		mutation = new IntegerPolynomialMutation(mutationProbability, mutationDistributionIndex);

		selection = new BinaryTournamentSelection<IntegerSolution>();
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void executeRunner() {
		
		algorithm = new DaDagNsgaIIIBuilder<>(problem)
				.setCrossoverOperator(crossover)
				.setMutationOperator(mutation)
				.setSelectionOperator(selection)
				.setMaxIterations(Global.MAX_ITERATION)
				.setObjectiveName("Cost", "Makespan", getThirdObjectiveName())
				.build();
		
		algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
		long computingTime = algorithmRunner.getComputingTime();

		List<IntegerSolution> population = ((DaDagNsgaIII) algorithm).getAllPopulation();
		List<IntegerSolution> paretoFront = algorithm.getResult();
		
		printObjectivesAndVariablesValues(population, Global.POPULATION_OBJECTIVE_FILE, Global.POPULATION_VARIABLE_FILE);		
		printObjectivesAndVariablesValues(paretoFront, Global.PARETO_VARIABLE_FILE, Global.PARETO_OBJECTIVE_FILE);		

		logger.debug("");
		logger.info("Execution Time: " + computingTime + " ms");
		logger.debug("");
		logger.info("Pareto Size         : " + paretoFront.size());
		logger.info("Pareto Objective: " + Global.PARETO_OBJECTIVE_FILE);
		logger.info("Pareto Variable : " + Global.PARETO_VARIABLE_FILE);
		logger.info("Pareto Quality  : " + Global.PARETO_QUALITY_FILE);
		logger.debug("");
		logger.info("Population Size     : " + population.size());
		logger.info("Population Objective: " + Global.POPULATION_OBJECTIVE_FILE);
		logger.info("Population Variable : " + Global.POPULATION_VARIABLE_FILE);
		logger.info("Population Quality  : " + Global.POPULATION_QUALITY_FILE);
		
		
		if (!referenceParetoFront.equals("")) {
			try {
				String qualityStr = printQualityIndicatorsOfAlgorithm(paretoFront, referenceParetoFront, computingTime);
				
				JavaHelper.writeStringToFile(Global.PARETO_QUALITY_FILE, qualityStr);
				
				qualityStr = printQualityIndicatorsOfAlgorithm(population, referenceParetoFront, computingTime);
				
				JavaHelper.writeStringToFile(Global.POPULATION_QUALITY_FILE, qualityStr);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				logger.error(e);
				e.printStackTrace();
			}
		}		
	}
	
	public void printObjectivesAndVariablesValues(List<IntegerSolution> population, String variableFile, String objectiveFile) {
		new DaDagSolutionOutput(population).setSeparator(",")
				.setVarFileOutputContext(new DaDagFileOutputContext(variableFile))
				.setFunFileOutputContext(new DaDagFileOutputContext(objectiveFile)).print();
		
	}

	  /**
	   * Print all the available quality indicators
	   * @param population
	   * @param paretoFrontFile
	   * @throws FileNotFoundException
	   */
	public String printQualityIndicatorsOfAlgorithm(List<IntegerSolution> population, String paretoFrontFile, long computing) throws FileNotFoundException {
		
		String outputString = "\n";
		Front referenceFront;
		
		try {
			
			File f = new File(paretoFrontFile);

			if (!f.exists()) {
				logger.info("File not found: " + paretoFrontFile + " is missing!!!");
				paretoFrontFile = Global.REFERENCE_PARETO_FRONT;
				logger.info("Trying : " + paretoFrontFile);				
			}
			
			referenceFront = new ArrayFront(paretoFrontFile);

			FrontNormalizer frontNormalizer = new FrontNormalizer(referenceFront);

			Front normalizedReferenceFront = frontNormalizer.normalize(referenceFront);
			Front normalizedFront = frontNormalizer.normalize(new ArrayFront(population));
			List<PointSolution> normalizedPopulation = FrontUtils.convertFrontToSolutionList(normalizedFront);

			Double hvN = new PISAHypervolume<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
			Double hv = new PISAHypervolume<IntegerSolution>(referenceFront).evaluate(population);
			Double epsiN = new Epsilon<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
			Double epsi = new Epsilon<IntegerSolution>(referenceFront).evaluate(population);
			
			Double gdN = new GenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
			Double gd = new GenerationalDistance<IntegerSolution>(referenceFront).evaluate(population);
			Double igdN = new InvertedGenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
			Double igd = new InvertedGenerationalDistance<IntegerSolution>(referenceFront).evaluate(population);
			Double igdpN = new InvertedGenerationalDistancePlus<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
			Double igdp = new InvertedGenerationalDistancePlus<IntegerSolution>(referenceFront).evaluate(population);
			
			Double spreadN = new Spread<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
			Double spread = new Spread<IntegerSolution>(referenceFront).evaluate(population);
			Double error = new ErrorRatio<List<? extends Solution<?>>>(referenceFront).evaluate(population);
			
			outputString += "Hypervolume (N) : " + hvN + "\n";
			outputString += "Hypervolume     : " + hv + "\n";
			outputString += "Epsilon (N)     : " + epsiN + "\n";
			outputString += "Epsilon         : " + epsi + "\n";
			outputString += "GD (N)          : " + gdN + "\n";
			outputString += "GD              : " + gd + "\n";
			outputString += "IGD (N)         : " + igdN + "\n";
			outputString += "IGD             : " + igd + "\n";
			outputString += "IGD+ (N)        : " + igdpN + "\n";
			outputString += "IGD+            : " + igdp + "\n";
			outputString += "Spread (N)      : " + spreadN + "\n";
			outputString += "Spread          : " + spread + "\n";
			outputString += "Error ratio     : " + error + "\n";

			outputString += "\n";
			outputString += "====Data Below====\n";
			outputString += computing + "\n";
			outputString += hvN + "," + epsiN + "," + gdN + "," + igdN + "," + igdpN + "," + spreadN + "\n";
			outputString += Global.MODEL_RATIO + "\n";
			
			logger.info(outputString);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error(e);
			e.printStackTrace();
		}
		
		return outputString;
	}
	
	private String getThirdObjectiveName() {
		
		String result = "NoOfVMs";
		
		switch (Global.THIRD_OBJECTIVE.toUpperCase()) {
		case "COMMU":
			result = "NoOfCommTasks";
			break;
		case "IDLE":
			result = "IdleTime";
			break;			
		default:
			break;
		}
		
		return result;
	}
}
