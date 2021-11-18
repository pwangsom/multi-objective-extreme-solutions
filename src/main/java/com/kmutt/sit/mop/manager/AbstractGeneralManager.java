package com.kmutt.sit.mop.manager;

import java.io.File;

import com.kmutt.sit.mop.manager.configulations.ConfigurationInterface;
import com.kmutt.sit.utils.JavaHelper;

public abstract class AbstractGeneralManager<C extends ConfigurationInterface> {
	
	protected C config;
	
	public abstract void manage();
	public abstract void displayConfigurations();
	
	public void setConfigProgramKey(String programKey) {
		this.config.setProgramKey(programKey);
	}

	public void setConfigWorkingPath(String path) {
		this.config.setWorkingPath(path);
		this.config.setInputPath(JavaHelper.appendPathName(path, "input"));
		this.config.setOutputPath(JavaHelper.appendPathName(path, "output"));
	}
	
	public void setConfgNameAndIdWorkflowNameSize(String name, String id) {
		this.config.setName(name);
		this.config.setId(id);
	}
	
	public void setConfigMaxRun(int maxRun) {
		this.config.setMaxRun(maxRun);
	}
	
	public void setConfigMaxIteration(int maxIteration) {
		this.config.setAllMaxIteration(maxIteration);
		this.config.setCurrentMaxIteration(maxIteration);
	}
	
	public void setConfigIntervalIteration(int interval) {
		this.config.setIntervalIteration(interval);
	}	
	public void setConfigAlgorithmList(String[] algorithms) {
		this.config.setAlgorithmTypeList(algorithms);
	}
	
	protected void createSubfolderOutput() {
	    File directory = new File(this.config.getOutputPath());
	    
	    if (!directory.exists()){
	        directory.mkdir();
	    }	    
	}
	
	protected void createSubfolderOutput(String... strings) {
	    File directory = new File(JavaHelper.appendPathName(strings));
	    
	    if (!directory.exists()){
	        directory.mkdir();
	    }	    
	}
}
