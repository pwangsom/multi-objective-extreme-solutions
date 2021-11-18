package com.kmutt.sit.mop.parameter;

import com.kmutt.sit.workflow.ClusterType;

public class Global {
	
	public static ClusterType CLUSTER_TYPE = ClusterType.NONE;

	public static String WORKING_SPACE = ".";
	public static String INPUT_PATH = ".";
	public static String OUTPUT_PATH = ".";
	
	
	/*
	 * NSGA III input and output
	 */
	public static String REFERENCE_PARETO_FRONT = "NBI_3_12.pf";
	
	public static String PARETO_OBJECTIVE_FILE = "pareto_objective.out";
	public static String PARETO_VARIABLE_FILE = "pareto_variable.out";
	public static String PARETO_QUALITY_FILE = "pareto_quality.out";
	
	public static String POPULATION_OBJECTIVE_FILE = "population_objective.out";
	public static String POPULATION_VARIABLE_FILE = "population_variable.out";
	public static String POPULATION_QUALITY_FILE = "population_quality.out";
	
	public static Integer MAX_ITERATION = 5;	

	public static String THIRD_OBJECTIVE = "IDLE";
	
	public static String MODEL_RATIO = "";
	
	public static Boolean SUPER_AGENT = false;
	
	public static String RUN_NUMBER = "test";

}
