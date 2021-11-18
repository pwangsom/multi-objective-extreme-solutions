package com.kmutt.sit.cloud.vm.model;

public enum ProviderModelType {
	
	AWS("Amazon Web Services"), GCE("Google Compute Engine");
	
	private String name;
	
	ProviderModelType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
