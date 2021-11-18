package com.kmutt.sit.workflow;

public enum ClusterType {
	
	NONE("none"), P2P("p2p"), HORIZONTAL("hori"), BoT("bot"), LEVEL("level"), SINGLE("single");
	
	private String name;
	
	ClusterType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
