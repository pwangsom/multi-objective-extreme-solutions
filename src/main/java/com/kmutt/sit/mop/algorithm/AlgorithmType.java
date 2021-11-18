package com.kmutt.sit.mop.algorithm;

public enum AlgorithmType {
	NSGA_II("nsgaii"), NSGA_III("nsgaiii"), ENSGA_III("ensgaiii"), MOEADD("moeadd"),
	// kp = knapsack, nc = number item capacity
	KPNC_NSGA_II("nsgaii"), KPNC_NSGA_III("nsgaiii"), KPNC_ENSGA_III("ensgaiii");
	
	private String name;
	
	AlgorithmType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
