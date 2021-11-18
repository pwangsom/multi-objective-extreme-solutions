package com.kmutt.sit.mop.algorithm.knapsack;

import java.util.Comparator;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.util.comparator.DominanceComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

import com.kmutt.sit.mop.algorithm.AbstractGeneralNsgaBuilder;
import com.kmutt.sit.mop.algorithm.AlgorithmType;
import com.kmutt.sit.mop.manager.knapsack.KnapsackConfigurationInterface;

public class NsgaPermutationIntegerSolutionBuilder extends AbstractGeneralNsgaBuilder<KnapsackConfigurationInterface, PermutationSolution<Integer>> {

	@Override
	public Algorithm<List<PermutationSolution<Integer>>> buildAlgorithm(AlgorithmType algorithmType) {
		// TODO Auto-generated method stub
		
		Algorithm<List<PermutationSolution<Integer>>> algorithm = null;
		
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
	
	private Algorithm<List<PermutationSolution<Integer>>> buidNsgaII(){
		
		Comparator<PermutationSolution<Integer>> dominance = new DominanceComparator<PermutationSolution<Integer>>();
		SolutionListEvaluator<PermutationSolution<Integer>> evaluator = new SequentialSolutionListEvaluator<PermutationSolution<Integer>>();

		Algorithm<List<PermutationSolution<Integer>>> algorithm = new NsgaIIIntegerSolution<PermutationSolution<Integer>>(problem, maxIterations, populationSize, crossover,
				mutation, selection, dominance, evaluator, config);
		
		return algorithm;
	}
	
	private Algorithm<List<PermutationSolution<Integer>>> buidNsgaIII(){
		
		NSGAIIIBuilder<PermutationSolution<Integer>> builder = new NSGAIIIBuilder<PermutationSolution<Integer>>(problem)
				.setCrossoverOperator(crossover)
				.setMutationOperator(mutation)
				.setSelectionOperator(selection)
				.setMaxIterations(maxIterations)
				.setPopulationSize(populationSize);
		
		Algorithm<List<PermutationSolution<Integer>>> algorithm = new NsgaIIIIntegerSolution<PermutationSolution<Integer>>(builder, config);
		
		return algorithm;
	}
	
	private Algorithm<List<PermutationSolution<Integer>>> buidExtremeNsgaIII(){
		
		NSGAIIIBuilder<PermutationSolution<Integer>> builder = new NSGAIIIBuilder<PermutationSolution<Integer>>(problem)
				.setCrossoverOperator(crossover)
				.setMutationOperator(mutation)
				.setSelectionOperator(selection)
				.setMaxIterations(maxIterations)
				.setPopulationSize(populationSize);
		
		Algorithm<List<PermutationSolution<Integer>>> algorithm = new ExtremeNsgaIIIPermutationIntegerSolution(builder, config);
		
		return algorithm;
	}

}
