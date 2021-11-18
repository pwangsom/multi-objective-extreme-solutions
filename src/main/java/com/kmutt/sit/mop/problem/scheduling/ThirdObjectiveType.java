package com.kmutt.sit.mop.problem.scheduling;

public enum ThirdObjectiveType {
	COMMU("commu"), VM("vm");
	
	private String name;
	
	ThirdObjectiveType(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
