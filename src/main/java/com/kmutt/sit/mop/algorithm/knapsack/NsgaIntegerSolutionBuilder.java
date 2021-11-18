package com.kmutt.sit.mop.algorithm.knapsack;

import java.util.Comparator;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.comparator.DominanceComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

import com.kmutt.sit.mop.algorithm.AbstractGeneralNsgaBuilder;
import com.kmutt.sit.mop.algorithm.AlgorithmType;
import com.kmutt.sit.mop.manager.knapsack.KnapsackConfigurationInterface;

public class NsgaIntegerSolutionBuilder extends AbstractGeneralNsgaBuilder<KnapsackConfigurationInterface, IntegerSolution> {

	@Override
	public Algorithm<List<IntegerSolution>> buildAlgorithm(AlgorithmType algorithmType) {
		// TODO Auto-generated method stub
		
		Algorithm<List<IntegerSolution>> algorithm = null;
		
		switch (algorithmType) {
		case KPNC_NSGA_II:
			algorithm = buidNsgaII();
			break;
		case KPNC_NSGA_III:
			algorithm = buidNsgaIII();
			break;
		case KPNC_ENSGA_III:
			algorithm = buidExtremeNsgaIII();
			break;
		default:
			break;
		}
		
		return algorithm;
	}
	
	private Algorithm<List<IntegerSolution>> buidNsgaII(){
		
		Comparator<IntegerSolution> dominance = new DominanceComparator<IntegerSolution>();
		SolutionListEvaluator<IntegerSolution> evaluator = new SequentialSolutionListEvaluator<IntegerSolution>();

		Algorithm<List<IntegerSolution>> algorithm = new NsgaIIIntegerSolution<IntegerSolution>(problem, maxIterations, populationSize, crossover,
				mutation, selection, dominance, evaluator, config);
		
		return algorithm;
	}
	
	private Algorithm<List<IntegerSolution>> buidNsgaIII(){
		
		NSGAIIIBuilder<IntegerSolution> builder = new NSGAIIIBuilder<IntegerSolution>(problem)
				.setCrossoverOperator(crossover)
				.setMutationOperator(mutation)
				.setSelectionOperator(selection)
				.setMaxIterations(maxIterations)
				.setPopulationSize(populationSize);
		
		Algorithm<List<IntegerSolution>> algorithm = new NsgaIIIIntegerSolution<IntegerSolution>(builder, config);
		
		return algorithm;
	}
	
	private Algorithm<List<IntegerSolution>> buidExtremeNsgaIII(){
		
		NSGAIIIBuilder<IntegerSolution> builder = new NSGAIIIBuilder<IntegerSolution>(problem)
				.setCrossoverOperator(crossover)
				.setMutationOperator(mutation)
				.setSelectionOperator(selection)
				.setMaxIterations(maxIterations)
				.setPopulationSize(populationSize);
		
		Algorithm<List<IntegerSolution>> algorithm = new ExtremeNsgaIIIIntegerSolution(builder, config);
		
		return algorithm;
	}
}
