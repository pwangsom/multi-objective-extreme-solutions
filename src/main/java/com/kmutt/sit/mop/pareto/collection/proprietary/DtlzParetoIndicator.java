package com.kmutt.sit.mop.pareto.collection.proprietary;

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

import com.kmutt.sit.utils.JavaHelper;

public class DtlzParetoIndicator<S extends Solution<?>> {
	
	private Logger logger = Logger.getLogger(DtlzParetoIndicator.class);
		
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
	
	private Long computingTime = Long.valueOf(0);
	
	private Map<String, Double> indicatorMapping;
	
	private String paretoFrontFile = null;
	private List<S> paretoSet;
	
	private String key;
	
	private List<String> indicatorDisplayList;
	
	private boolean isPrintObjectiveFile = true;
	private boolean isPrintSolutionFile = true;	
	private boolean isRawData = true;
	
	public static String[] indicatorNames = {"Normalized-Hypervolume", "Hypervolume", "Normalized-Epsilon", "Epsilon",
											 "Normalized-GD", "GD", "Normalized-IGD", "IGD", "Normalized-IGD+", "IGD+",
											 "Normalized-Spread", "Spread", "Error-Ratio"};
	
	public DtlzParetoIndicator() {
		indicatorDisplayList = new ArrayList<String>();
		indicatorMapping = new HashMap<String, Double>();
	}
	
	public DtlzParetoIndicator<S> setParetoKey(String key){
		this.key = key;
		
		return this;
	}
	
	public DtlzParetoIndicator<S> setPareto(List<S> paretoSet){
		this.paretoSet = paretoSet;
		return this;
	}
	
	public void setParetoFrontFile(String paretoFrontFile){
		this.paretoFrontFile = paretoFrontFile;
	}
	
	public void evaluateIndicator(boolean isDisplay) {
		
		try {
			
			if (!JavaHelper.isNull(this.paretoFrontFile)){
				
				Front referenceFront = new ArrayFront(paretoFrontFile);
				FrontNormalizer frontNormalizer = new FrontNormalizer(referenceFront);

				Front normalizedReferenceFront = frontNormalizer.normalize(referenceFront);
				Front normalizedFront = frontNormalizer.normalize(new ArrayFront(paretoSet));
				
				List<PointSolution> normalizedPopulation = FrontUtils.convertFrontToSolutionList(normalizedFront);

				hvN = new PISAHypervolume<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
				hv = new PISAHypervolume<S>(referenceFront).evaluate(paretoSet);
				
				indicatorMapping.put(indicatorNames[0], hvN);
				indicatorMapping.put(indicatorNames[1], hv);
				
				epsiN = new Epsilon<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
				epsi = new Epsilon<S>(referenceFront).evaluate(paretoSet);

				indicatorMapping.put(indicatorNames[2], epsiN);
				indicatorMapping.put(indicatorNames[3], epsi);

				gdN = new GenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
				gd = new GenerationalDistance<S>(referenceFront).evaluate(paretoSet);
				
				indicatorMapping.put(indicatorNames[4], gdN);
				indicatorMapping.put(indicatorNames[5], gd);
				
				igdN = new InvertedGenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
				igd = new InvertedGenerationalDistance<S>(referenceFront).evaluate(paretoSet);			

				indicatorMapping.put(indicatorNames[6], igdN);
				indicatorMapping.put(indicatorNames[7], gd);
				
				igdpN = new InvertedGenerationalDistancePlus<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
				igdp = new InvertedGenerationalDistancePlus<S>(referenceFront).evaluate(paretoSet);

				indicatorMapping.put(indicatorNames[8], igdpN);
				indicatorMapping.put(indicatorNames[9], igdp);

				spreadN = new Spread<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
				spread = new Spread<S>(referenceFront).evaluate(paretoSet);

				indicatorMapping.put(indicatorNames[10], spreadN);
				indicatorMapping.put(indicatorNames[11], spread);
				
				error = new ErrorRatio<List<? extends Solution<?>>>(referenceFront).evaluate(paretoSet);
				
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
					logger.debug("");
					logger.info("============ Indicator Results ============");
					logger.debug("");
					indicatorDisplayList.stream().forEach(i -> {
						logger.info(i);
					});
				}
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e, e);
		}
	}
	
	public List<String> getIndicatorDisplayList() {
		return indicatorDisplayList;
	}
	
	public Double getHvN() {
		return hvN;
	}
	public Double getHv() {
		return hv;
	}
	public Double getEpsiN() {
		return epsiN;
	}
	public Double getEpsi() {
		return epsi;
	}
	public Double getGdN() {
		return gdN;
	}
	public Double getGd() {
		return gd;
	}
	public Double getIgdN() {
		return igdN;
	}
	public Double getIgd() {
		return igd;
	}
	public Double getIgdpN() {
		return igdpN;
	}
	public Double getIgdp() {
		return igdp;
	}
	public Double getSpreadN() {
		return spreadN;
	}
	public Double getSpread() {
		return spread;
	}
	public Double getError() {
		return error;
	}
	public List<S> getPopulation() {
		return paretoSet;
	}

	public String getParetoFrontFile() {
		return paretoFrontFile;
	}

	public String getKey() {
		return key;
	}

	public boolean isPrintObjectiveFile() {
		return isPrintObjectiveFile;
	}

	public void setPrintObjectiveFile(boolean isPrintObjectiveFile) {
		this.isPrintObjectiveFile = isPrintObjectiveFile;
	}

	public boolean isPrintSolutionFile() {
		return isPrintSolutionFile;
	}

	public void setPrintSolutionFile(boolean isPrintSolutionFile) {
		this.isPrintSolutionFile = isPrintSolutionFile;
	}

	public Map<String, Double> getIndicatorMapping() {
		return indicatorMapping;
	}
	
	public Double getIndicatorByKey(String key) {
		return indicatorMapping.get(key);
	}

	public boolean isRawData() {
		return isRawData;
	}

	public DtlzParetoIndicator<S> setRawData(boolean isRawData) {
		this.isRawData = isRawData;
		
		return this;
	}

	public Long getComputingTime() {
		return computingTime;
	}

	public void setComputingTime(Long computingTime) {
		this.computingTime = computingTime;
	}
}
