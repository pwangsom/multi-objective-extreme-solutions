package com.kmutt.sit.mop.pareto.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.uma.jmetal.qualityindicator.impl.Epsilon;
import org.uma.jmetal.qualityindicator.impl.ErrorRatio;
import org.uma.jmetal.qualityindicator.impl.GenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.qualityindicator.impl.Spread;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.util.PointSolution;

public class ParetoIndicator<S extends Solution<?>> {
	
	private Logger logger = Logger.getLogger(ParetoIndicator.class);
	
    private Front referenceFront;
    private Front normalizedReferenceFront;
    private FrontNormalizer frontNormalizer;
	
	private Double hvN = 0.0;
	private Double hv = 0.0;
	private Double epsiN = 0.0;
	private Double epsi = 0.0;
	private Double gdN = 0.0;
	private Double gd = 0.0;
	private Double igdN = 0.0;
	private Double igd = 0.0;
	private Double igdpN = 0.0;
	private Double igdp = 0.0;
	private Double spreadN = 0.0;
	private Double spread = 0.0;
	private Double error = 0.0;
	
	private Map<String, Double> indicatorMapping;
	private List<String> indicatorDisplayList;
	private List<PointSolution> normalizedPopulation;
	
	private Double paretoSize;
	
	public static String[] indicatorNames = {"NormalizedHypervolume", "Hypervolume", "NormalizedEpsilon", "Epsilon",
											 "NormalizedGD", "GD", "NormalizedIGD", "IGD", "NormalizedIGDP", "IGDP",
											 "NormalizedSpread", "Spread", "ErrorRatio"};
	
	public ParetoIndicator() {
		indicatorDisplayList = new ArrayList<String>();
		indicatorMapping = new HashMap<String, Double>();
	}
	
	public void setReferenceFront(Front referenceFront, Front normalizedReferenceFront, FrontNormalizer frontNormalizer) {
		this.referenceFront = referenceFront;
		this.normalizedReferenceFront = normalizedReferenceFront;
		this.frontNormalizer = frontNormalizer;
	}
	
	public List<PointSolution> evaluateIndicator(boolean isDisplay, List<S> population) {
		
		setParetoSize((double) population.size());
		normalizedPopulation = new ArrayList<>();
		
		try {				
				Front normalizedFront = frontNormalizer.normalize(new ArrayFront(population));
				
				normalizedPopulation = FrontUtils.convertFrontToSolutionList(normalizedFront);

				hvN = new PISAHypervolume<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
				hv = new PISAHypervolume<S>(referenceFront).evaluate(population);
				
				indicatorMapping.put(indicatorNames[0], hvN);
				indicatorMapping.put(indicatorNames[1], hv);
				
				epsiN = new Epsilon<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
				epsi = new Epsilon<S>(referenceFront).evaluate(population);

				indicatorMapping.put(indicatorNames[2], epsiN);
				indicatorMapping.put(indicatorNames[3], epsi);

				gdN = new GenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
				gd = new GenerationalDistance<S>(referenceFront).evaluate(population);
				
				indicatorMapping.put(indicatorNames[4], gdN);
				indicatorMapping.put(indicatorNames[5], gd);
				
				igdN = new InvertedGenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
				igd = new InvertedGenerationalDistance<S>(referenceFront).evaluate(population);			

				indicatorMapping.put(indicatorNames[6], igdN);
				indicatorMapping.put(indicatorNames[7], igd);
				
				igdpN = new InvertedGenerationalDistancePlus<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
				igdp = new InvertedGenerationalDistancePlus<S>(referenceFront).evaluate(population);

				indicatorMapping.put(indicatorNames[8], igdpN);
				indicatorMapping.put(indicatorNames[9], igdp);

				spreadN = new Spread<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
				spread = new Spread<S>(referenceFront).evaluate(population);

				indicatorMapping.put(indicatorNames[10], spreadN);
				indicatorMapping.put(indicatorNames[11], spread);
				
				error = new ErrorRatio<List<? extends Solution<?>>>(referenceFront).evaluate(population);
				
				indicatorMapping.put(indicatorNames[12], error);			
				
				indicatorDisplayList.add(String.format("%-22s: %.10f", indicatorNames[0], hvN));
				indicatorDisplayList.add(String.format("%-22s: %.10f", indicatorNames[1], hv));
				indicatorDisplayList.add(String.format("%-22s: %.10f", indicatorNames[2], epsiN));
				indicatorDisplayList.add(String.format("%-22s: %.10f", indicatorNames[3], epsi));
				indicatorDisplayList.add(String.format("%-22s: %.10f", indicatorNames[4], gdN));
				indicatorDisplayList.add(String.format("%-22s: %.10f", indicatorNames[5], gd));
				indicatorDisplayList.add(String.format("%-22s: %.10f", indicatorNames[6], igdN));
				indicatorDisplayList.add(String.format("%-22s: %.10f", indicatorNames[7], igd));
				indicatorDisplayList.add(String.format("%-22s: %.10f", indicatorNames[8], igdpN));
				indicatorDisplayList.add(String.format("%-22s: %.10f", indicatorNames[9], igdp));
				indicatorDisplayList.add(String.format("%-22s: %.10f", indicatorNames[10], spreadN));
				indicatorDisplayList.add(String.format("%-22s: %.10f", indicatorNames[11], spread));
				indicatorDisplayList.add(String.format("%-22s: %.10f", indicatorNames[12], error));
				
				if(isDisplay) {
					displayIndicator();
				}
				
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e, e);
		}
		
		return normalizedPopulation;
	}
	public Double getIndicatorByKey(String key) {
		return indicatorMapping.get(key);
	}
	
	public List<String> getIndicatorDisplayList() {
		return indicatorDisplayList;
	}
	
	public Double getParetoSize() {
		return paretoSize;
	}

	public void setParetoSize(Double paretoSize) {
		this.paretoSize = paretoSize;
	}
	
	public Double getHvN() {
		return hvN;
	}

	public void setHvN(Double hvN) {
		this.hvN = hvN;
		indicatorMapping.put(indicatorNames[0], hvN);
	}

	public Double getHv() {
		return hv;
	}

	public void setHv(Double hv) {
		this.hv = hv;
		indicatorMapping.put(indicatorNames[1], hv);
	}

	public Double getEpsiN() {
		return epsiN;
	}

	public void setEpsiN(Double epsiN) {
		this.epsiN = epsiN;
		indicatorMapping.put(indicatorNames[2], epsiN);
	}

	public Double getEpsi() {
		return epsi;
	}

	public void setEpsi(Double epsi) {
		this.epsi = epsi;
		indicatorMapping.put(indicatorNames[3], epsi);
	}

	public Double getGdN() {
		return gdN;
	}

	public void setGdN(Double gdN) {
		this.gdN = gdN;
		indicatorMapping.put(indicatorNames[4], gdN);
	}

	public Double getGd() {
		return gd;
	}

	public void setGd(Double gd) {
		this.gd = gd;
		indicatorMapping.put(indicatorNames[5], gd);
	}

	public Double getIgdN() {
		return igdN;
	}

	public void setIgdN(Double igdN) {
		this.igdN = igdN;
		indicatorMapping.put(indicatorNames[6], igdN);
	}

	public Double getIgd() {
		return igd;
	}

	public void setIgd(Double igd) {
		this.igd = igd;
		indicatorMapping.put(indicatorNames[7], igd);
	}

	public Double getIgdpN() {
		return igdpN;
	}

	public void setIgdpN(Double igdpN) {
		this.igdpN = igdpN;
		indicatorMapping.put(indicatorNames[8], igdpN);
	}

	public Double getIgdp() {
		return igdp;
	}

	public void setIgdp(Double igdp) {
		this.igdp = igdp;
		indicatorMapping.put(indicatorNames[9], igdp);
	}

	public Double getSpreadN() {
		return spreadN;
	}

	public void setSpreadN(Double spreadN) {
		this.spreadN = spreadN;
		indicatorMapping.put(indicatorNames[10], spreadN);
	}

	public Double getSpread() {
		return spread;
	}

	public void setSpread(Double spread) {
		this.spread = spread;
		indicatorMapping.put(indicatorNames[11], spread);
	}

	public Double getError() {
		return error;
	}

	public void setError(Double error) {
		this.error = error;
		indicatorMapping.put(indicatorNames[12], error);
	}

	public void displayIndicator() {
		logger.debug("");
		logger.info("============ Indicator Results ============");
		logger.debug("");
		indicatorDisplayList.stream().forEach(i -> {
			logger.info(i);
		});
	}
}
