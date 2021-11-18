package com.kmutt.sit.mop.problem.knapsack;

public enum KnapsackRepresentationProblemType {
	BINARY("binary"), PERMUTATION("permutation");

	private String name;
	
	KnapsackRepresentationProblemType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
