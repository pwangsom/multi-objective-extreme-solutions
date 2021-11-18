package com.kmutt.sit.mop.pareto.collection;

import java.util.List;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.point.util.PointSolution;

public class ParetoIndicatorInstance<S extends Solution<?>> {	

	private String refParetoFrontKey;
	private String experimentTreatmentKey;
	private String instanceKey;
	
	private int instanceRun;
	private int instanceGeneration;

	private List<S> paretoSet;	
	private List<PointSolution> normalizedParetoSet;	
	private ParetoIndicator<S> indicator;
	
	public void setIndicator(Front referenceFront, Front normalizedReferenceFront, FrontNormalizer frontNormalizer) {
		indicator = new ParetoIndicator<S>();
		indicator.setReferenceFront(referenceFront, normalizedReferenceFront, frontNormalizer);
		indicator.evaluateIndicator(false, paretoSet);
	}
	
	public void measureIndicator() {
		normalizedParetoSet = indicator.evaluateIndicator(false, paretoSet);
	}

	public String getRefParetoFrontKey() {
		return refParetoFrontKey;
	}

	public void setRefParetoFrontKey(String refParetoFrontKey) {
		this.refParetoFrontKey = refParetoFrontKey;
	}

	public String getInstanceKey() {
		return instanceKey;
	}

	public void setInstanceKey(String instanceKey) {
		this.instanceKey = instanceKey;
	}

	public int getInstanceRun() {
		return instanceRun;
	}

	public void setInstanceRun(int instanceRun) {
		this.instanceRun = instanceRun;
	}

	public int getInstanceGeneration() {
		return instanceGeneration;
	}

	public void setInstanceGeneration(int instanceGeneration) {
		this.instanceGeneration = instanceGeneration;
	}

	public List<S> getParetoSet() {
		return paretoSet;
	}

	public void setParetoSet(List<S> paretoSet) {
		this.paretoSet = paretoSet;
	}

	public List<PointSolution> getNormalizedParetoSet() {
		return normalizedParetoSet;
	}

	public void setNormalizedParetoSet(List<PointSolution> normalizedParetoSet) {
		this.normalizedParetoSet = normalizedParetoSet;
	}

	public ParetoIndicator<S> getIndicator() {
		return indicator;
	}

	public void setIndicator(ParetoIndicator<S> indicator) {
		this.indicator = indicator;
	}

	public String getExperimentTreatmentKey() {
		return experimentTreatmentKey;
	}

	public void setExperimentTreatmentKey(String treatmentKey) {
		this.experimentTreatmentKey = treatmentKey;
	}

}
