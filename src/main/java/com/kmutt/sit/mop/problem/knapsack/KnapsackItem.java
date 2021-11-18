package com.kmutt.sit.mop.problem.knapsack;

public class KnapsackItem {
	
	// Intend to use for generating extreme solution in E-NSGA-III
	// this index will be referred to position in a solution
	private int itemIndex = 0;
	
	private int objective1 = 0;
	private int objective2 = 0;
	private int objective3 = 0;
	private int weight = 0;
	
	public KnapsackItem() {
		
	}
	
	public KnapsackItem(int itemIndex, int objective1, int objective2, int objective3, int weight) {
		this.itemIndex = itemIndex;
		this.objective1 = objective1;
		this.objective2 = objective2;
		this.objective3 = objective3;
		this.weight = weight;
	}
	
	public int getItemIndex() {
		return itemIndex;
	}

	public void setItemIndex(int itemIndex) {
		this.itemIndex = itemIndex;
	}

	public int getObjective1() {
		return objective1;
	}
	public void setObjective1(int objective1) {
		this.objective1 = objective1;
	}
	public int getObjective2() {
		return objective2;
	}
	public void setObjective2(int objective2) {
		this.objective2 = objective2;
	}
	public int getObjective3() {
		return objective3;
	}
	public void setObjective3(int objective3) {
		this.objective3 = objective3;
	}		
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}

}
