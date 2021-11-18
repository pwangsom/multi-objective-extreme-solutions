package com.kmutt.sit.mop.problem.knapsack;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.solution.PermutationSolution;

public class KnapsackPermutationIntegerEvaluator {	
	
	private KnapsackDataset dataset;
	private PermutationSolution<Integer> solution;
	private List<Integer> solutionList;
	
	private int firstObjectiveValue;
	private int secondObjectiveValue;
	private int thirdObjectiveValue;
	private int weight = 0;
	
	public KnapsackPermutationIntegerEvaluator(KnapsackDataset dataset, PermutationSolution<Integer>  solution) {
		this.dataset = dataset;
		this.solution = solution;
		solutionList = new ArrayList<Integer>();
	}
	
	public void evaluate() {	
		int maxWeight = dataset.getWeightCapacity();
		
		int obj1 = 0;
		int obj2 = 0;
		int obj3 = 0;
		
		for (int i = 0; i < solution.getNumberOfVariables(); i++) {
			
			int itemId = solution.getVariableValue(i)+1;
			
			KnapsackItem item = dataset.getKnapSackItemById(itemId);
			
			if(weight + item.getWeight() <= maxWeight) {
				
				solutionList.add(solution.getVariableValue(i));
				
				obj1 += item.getObjective1();
				obj2 += item.getObjective2();
				obj3 += item.getObjective3();
				
				weight += item.getWeight();				
			}
		}
		
		this.firstObjectiveValue = this.dataset.getMaxObjective1() - obj1;
		this.secondObjectiveValue = this.dataset.getMaxObjective2() - obj2;
		this.thirdObjectiveValue = this.dataset.getMaxObjective3() - obj3;
		
	}

	public int getFirstObjectiveValue() {
		return firstObjectiveValue;
	}

	public void setFirstObjectiveValue(int firstObjectiveValue) {
		this.firstObjectiveValue = firstObjectiveValue;
	}

	public int getSecondObjectiveValue() {
		return secondObjectiveValue;
	}

	public void setSecondObjectiveValue(int secondObjectiveValue) {
		this.secondObjectiveValue = secondObjectiveValue;
	}

	public int getThirdObjectiveValue() {
		return thirdObjectiveValue;
	}

	public void setThirdObjectiveValue(int thirdObjectiveValue) {
		this.thirdObjectiveValue = thirdObjectiveValue;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public List<Integer> getSolutionList() {
		return solutionList;
	}

}
