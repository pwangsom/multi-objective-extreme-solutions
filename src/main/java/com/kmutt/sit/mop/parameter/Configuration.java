package com.kmutt.sit.mop.parameter;

import com.kmutt.sit.cloud.CloudScheduling;

public class Configuration {
	
	final public static Integer PRECISION_SCALE = 4;
	
	final public static Double MIN_OVERHEAD_RUNTIME = 0.00;
	final public static Double MAX_OVERHEAD_RUNTIME = 0.00;
	
	final public static Double MIN_OVERHEAD_COMMU = 0.00;
	final public static Double MAX_OVERHEAD_COMMU = 0.00;
	

	/*
	 * Bandwidth = 1Gbps ->  1,000,000,000 bps -> 125,000,000 Bps
	 * Bandwidth = 512Mbps ->   512,000,000 bps -> 64,000,000 Bps
	 * 
	 * In the Pegasus DAX file
	 * 
	 * Runtime is in seconds, and datasize in kB.
	 */
	
	final public static Double INTER_VM_THROUGHPUT = 64000000.00;
	final public static Double FILE_SIZE_MAGNITUDE = Math.pow(1024, 1);
	
	public static Double FILE_SIZE_SCALE = 1.00;
	public static Double RUNTIME_SCALE = 1.00;
	
	/*
	 * For NSGA III PARAMETER
	 * 
	 */
	final public static String REFERENCE_PARETO_FRONT = "src/main/resources/input/NBI_3_12.pf";
	
	final public static Double CROSSOVER_PROBABILITY = 1.0;
	final public static Double CROSSOVER_DISTRIBUTION_INDEX = 30.0;
	
	final public static Double MUTATION_PROBABILITY = 1.0;
	final public static Double MUTATION_DISTRIBUTION_INDEX = 20.0;	
	
	final public static Integer MAX_POPULATION = 92;
	
	final public static CloudScheduling.SchedulingType SCHEDULING_TYPE = CloudScheduling.SchedulingType.MIN;
}
