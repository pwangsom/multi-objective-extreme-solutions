package com.kmutt.sit.mop.runner.knapsack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.log4j.Logger;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.PMXCrossover;
import org.uma.jmetal.operator.impl.mutation.PermutationSwapMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.comparator.DominanceComparator;

import com.kmutt.sit.mop.algorithm.NsgaInterface;
import com.kmutt.sit.mop.algorithm.knapsack.NsgaPermutationIntegerSolutionBuilder;
import com.kmutt.sit.mop.manager.knapsack.KnapsackConfigurationInterface;
import com.kmutt.sit.mop.pareto.collection.ParetoIndicatorInstance;
import com.kmutt.sit.mop.problem.knapsack.KnapsackBinaryIntegerProblem;
import com.kmutt.sit.mop.problem.knapsack.KnapsackPermutationIntegerProblem;
import com.kmutt.sit.mop.runner.AbstractGeneralRunner;
import com.kmutt.sit.utils.JavaHelper;

public class KnapsackPermutationIntegerProblemRunner extends AbstractGeneralRunner<KnapsackConfigurationInterface, PermutationSolution<Integer>> {

	private Logger logger = Logger.getLogger(KnapsackPermutationIntegerProblemRunner.class);
	
	private final int POPULATION_SIZE = 92;

	protected Problem<PermutationSolution<Integer>> problem;
	protected CrossoverOperator<PermutationSolution<Integer>> crossover;
	protected MutationOperator<PermutationSolution<Integer>> mutation;
	protected SelectionOperator<List<PermutationSolution<Integer>>, PermutationSolution<Integer>> selection;

	@SuppressWarnings("unchecked")
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		logger.debug("");
		logger.info("Runner: " + config.getRunningKey() + " Starting...");
		
		setAlgorithmParameters();
		
		NsgaPermutationIntegerSolutionBuilder builder = new NsgaPermutationIntegerSolutionBuilder();
		builder.setConfig(config);
		builder.setProblem(problem);
		builder.setCrossover(crossover);
		builder.setMutation(mutation);
		builder.setSelection(selection);
		builder.setMaxIterations(config.getAllMaxIteration());
		builder.setPopulationSize(POPULATION_SIZE);
		
		
		Algorithm<List<PermutationSolution<Integer>>> algorithm = builder.buildAlgorithm(config.getAlgorithmType());

		new AlgorithmRunner.Executor(algorithm).execute();
		
		paretoSet = algorithm.getResult();
		
		indicatorInstanceList.addAll(((NsgaInterface<KnapsackConfigurationInterface, PermutationSolution<Integer>>) algorithm).getIndicatorInstanceList());
		writeParetoSetFiles();
		
		displayParetoSetReport();
		
		logger.debug("");
		logger.info("Runner: " + config.getRunningKey() + " Finished...");		
	}

	@Override
	public void setAlgorithmParameters() {
		// TODO Auto-generated method stub
		
		problem = new KnapsackPermutationIntegerProblem(this.config);
		
	    crossover = new PMXCrossover(1.0);

	    double mutationProbability = 1.0 / problem.getNumberOfVariables();
	    mutation = new PermutationSwapMutation<Integer>(mutationProbability) ;

	    selection = new BinaryTournamentSelection<PermutationSolution<Integer>>(new DominanceComparator<PermutationSolution<Integer>>());
	}

	@Override
	protected String getFileNamePathOfLastParetoSet() {
		// TODO Auto-generated method stub
		return JavaHelper.appendPathName(this.config.getSubfolderOutputPath(), this.config.getRunningKey() + "_gen" + this.config.getAllMaxIteration());
	}

	@Override
	protected String getFileNamePathOfAllParetoSet() {
		// TODO Auto-generated method stub
		return JavaHelper.appendPathName(this.config.getSubfolderOutputPath(), this.config.getRunningKey() + "_genALL");
	}

	public List<ParetoIndicatorInstance<IntegerSolution>> getParetoIndicatorInstanceListOfIntegerSolution() {
		// TODO Auto-generated method stub
		
		 List<ParetoIndicatorInstance<IntegerSolution>> integerList = new ArrayList<ParetoIndicatorInstance<IntegerSolution>>();
		 
		 indicatorInstanceList.stream().forEach(instance ->{
			 integerList.add(convertInstance(instance));
		 });
		 
		 return integerList;
	}
	
	
	private ParetoIndicatorInstance<IntegerSolution> convertInstance(ParetoIndicatorInstance<PermutationSolution<Integer>> original){
		
		ParetoIndicatorInstance<IntegerSolution> newInstance = new ParetoIndicatorInstance<IntegerSolution>();

		newInstance.setRefParetoFrontKey(original.getRefParetoFrontKey());
		newInstance.setInstanceKey(original.getInstanceKey());
		
		newInstance.setExperimentTreatmentKey(original.getExperimentTreatmentKey());
		newInstance.setInstanceRun(original.getInstanceRun());

		newInstance.setInstanceGeneration(original.getInstanceGeneration());		

		newInstance.setParetoSet(convertParetoSet(original.getParetoSet()));
		
		return newInstance;
	}
	
	private List<IntegerSolution> convertParetoSet(List<PermutationSolution<Integer>> original){
		
		KnapsackBinaryIntegerProblem integerProblem = new KnapsackBinaryIntegerProblem(this.config);
		int size = integerProblem.getNumberOfVariables();
		int objective = integerProblem.getNumberOfObjectives();
		
		List<IntegerSolution> integerSolutionList = new ArrayList<>(original.size());
		
		original.stream().forEach(instance -> {
			
			IntegerSolution solution = integerProblem.createSolution();
			
			IntStream.range(0, size).forEach(v -> {				
				solution.setVariableValue(v, instance.getVariableValue(v));
			});			
			
			IntStream.range(0, objective).forEach(v -> {				
				solution.setObjective(v, instance.getObjective(v));
			});	
			
		});
		
		return integerSolutionList;
	}
}
