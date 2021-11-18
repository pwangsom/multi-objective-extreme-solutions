package com.kmutt.sit.mop.algorithm.knapsack;

import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.BinarySolution;

import com.kmutt.sit.mop.algorithm.AbstractGeneralNsgaBuilder;
import com.kmutt.sit.mop.algorithm.AlgorithmType;
import com.kmutt.sit.mop.manager.knapsack.KnapsackConfigurationInterface;

public class NsgaBinarySolutionBuilder extends AbstractGeneralNsgaBuilder<KnapsackConfigurationInterface, BinarySolution> {

	@Override
	public Algorithm<List<BinarySolution>> buildAlgorithm(AlgorithmType algorithmType) {
		// TODO Auto-generated method stub
		
		Algorithm<List<BinarySolution>> algorithm = null;
		
		switch (algorithmType) {
		case KPNC_NSGA_II:
			algorithm = buidNsgaII();
			break;
		case KPNC_NSGA_III:
			algorithm = buidNsgaIII();
			break;
		case KPNC_ENSGA_III:
			algorithm = buidEnsgaIII();
			break;
		default:
			break;
		}
		
		return algorithm;
	}
	
	private Algorithm<List<BinarySolution>> buidNsgaII(){
		
/*		Comparator<IntegerSolution> dominance = new DominanceComparator<IntegerSolution>();
		SolutionListEvaluator<IntegerSolution> evaluator = new SequentialSolutionListEvaluator<IntegerSolution>();

		Algorithm<List<IntegerSolution>> algorithm = new NsgaIIIntegerSolution(problem, maxIterations, populationSize, crossover,
				mutation, selection, dominance, evaluator, config);*/
		
		return null;
	}
	
	private Algorithm<List<BinarySolution>> buidNsgaIII(){
		
/*		NSGAIIIBuilder<IntegerSolution> builder = new NSGAIIIBuilder<IntegerSolution>(problem)
				.setCrossoverOperator(crossover)
				.setMutationOperator(mutation)
				.setSelectionOperator(selection)
				.setMaxIterations(maxIterations)
				.setPopulationSize(populationSize);
		
		Algorithm<List<IntegerSolution>> algorithm = new NsgaIIIIntegerSolution(builder, config);*/
		
		return null;
	}

	private Algorithm<List<BinarySolution>> buidEnsgaIII(){
		
/*		NSGAIIIBuilder<IntegerSolution> builder = new NSGAIIIBuilder<IntegerSolution>(problem)
				.setCrossoverOperator(crossover)
				.setMutationOperator(mutation)
				.setSelectionOperator(selection)
				.setMaxIterations(maxIterations)
				.setPopulationSize(populationSize);
		
		Algorithm<List<IntegerSolution>> algorithm = new NsgaIIIIntegerSolution(builder, config);*/
		
		return null;
	}
}
