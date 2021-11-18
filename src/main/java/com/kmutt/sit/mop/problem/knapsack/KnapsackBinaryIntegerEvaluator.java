package com.kmutt.sit.mop.problem.knapsack;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.solution.IntegerSolution;

public class KnapsackBinaryIntegerEvaluator {
	
	private KnapsackDataset dataset;
	private IntegerSolution solution;
	private List<Integer> solutionList;
	
	private int firstObjectiveValue;
	private int secondObjectiveValue;
	private int thirdObjectiveValue;
	private int weight = 0;
	
	private boolean isViolateWeightCapacity = false;
	
	public KnapsackBinaryIntegerEvaluator(KnapsackDataset dataset, IntegerSolution  solution) {
		this.dataset = dataset;
		this.solution = solution;
		solutionList = new ArrayList<Integer>();
	}
	
	public void evaluate() {
		
		int obj1 = 0;
		int obj2 = 0;
		int obj3 = 0;
		
		for (int i = 0; i < solution.getNumberOfVariables(); i++) {
			
			if(solution.getVariableValue(i) == 1) {
				
				solutionList.add(i+1);
				
				KnapsackItem item = this.dataset.getKnapSackItemById(i+1);
				
				obj1 += item.getObjective1();
				obj2 += item.getObjective2();
				obj3 += item.getObjective3();
				
				weight += item.getWeight();
				
			}
		}
		
		if(weight > dataset.getSumWeight()) isViolateWeightCapacity = true;
		
		this.firstObjectiveValue = this.dataset.getMaxObjective1() - obj1;
		this.secondObjectiveValue = this.dataset.getMaxObjective2() - obj2;
		this.thirdObjectiveValue = this.dataset.getMaxObjective3() - obj3;
	}


	public List<Integer> getSolutionList() {
		return solutionList;
	}
	public int getFirstObjectiveValue() {
		return firstObjectiveValue;
	}
	public int getSecondObjectiveValue() {
		return secondObjectiveValue;
	}
	public int getThirdObjectiveValue() {
		return thirdObjectiveValue;
	}
	public int getWeight() {
		return weight;
	}
	public boolean isViolateWeightCapacity() {
		return isViolateWeightCapacity;
	}
}
