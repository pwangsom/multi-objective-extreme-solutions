package com.kmutt.sit.coverageratio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.util.Precision;
import org.apache.log4j.Logger;

public class CoverageObjectiveMatching {
	
	private Logger logger = Logger.getLogger(CoverageObjectiveMatching.class);	
	
	private String matchingName;
	
	private Integer firstScore;
	private Integer secondScore;
	
	private Double firstRatio;
	private Double secondRatio;
	
	private List<ThreeObjectives> firstPareto;
	private List<ThreeObjectives> secondPareto;
	
	public CoverageObjectiveMatching() {
		this.firstPareto = new ArrayList<ThreeObjectives>();
		this.firstPareto = new ArrayList<ThreeObjectives>();
	}
	
	public boolean isMatched() {
		return !this.firstPareto.isEmpty() && !this.secondPareto.isEmpty();
	}
	
	public void measureCoverageIndex() {
		if(isMatched()) {
			
			firstScore = assessCoverage(firstPareto, secondPareto);
			secondScore = assessCoverage(secondPareto, firstPareto);
			
			firstRatio = Precision.round(firstScore.doubleValue() / Integer.valueOf(secondPareto.size()).doubleValue(), 4);
			secondRatio = Precision.round(secondScore.doubleValue() / Integer.valueOf(firstPareto.size()).doubleValue(), 4);

			logger.info(this.toString());
		}
	}
	
	public String toString() {
		return String.format("%s,%d,%d,%.4f,%d,%d,%.4f", this.matchingName, this.firstScore, this.firstPareto.size(), this.firstRatio,
				this.secondScore, this.secondPareto.size(), this.secondRatio);
	}
	
	private Integer assessCoverage(List<ThreeObjectives> firstList, List<ThreeObjectives> secondList) {
		// Assess how many solutions in the second list are covered by solutions in the first list
				
		Map<Integer, ThreeObjectives> dominatedList = new HashMap<Integer, ThreeObjectives>();
		
		firstList.stream().forEach(solution -> {
			
			secondList.stream().forEach(another -> {
				if(solution.isDominate(another)) {
					
					if(!dominatedList.containsKey(another.getId())) {
						dominatedList.put(another.getId(), another);
					}
					
				}
			});
			
		});
		
		return dominatedList.size();
	}
	
	public String getMatchingName() {
		return matchingName;
	}
	public void setMatchingName(String matchingName) {
		this.matchingName = matchingName;
	}
	public Integer getFirstScore() {
		return firstScore;
	}
	public void setFirstScore(Integer firstScore) {
		this.firstScore = firstScore;
	}
	public Integer getSecondScore() {
		return secondScore;
	}
	public void setSecondScore(Integer secondScore) {
		this.secondScore = secondScore;
	}
	public Double getFirstRatio() {
		return firstRatio;
	}
	public void setFirstRatio(Double firstRatio) {
		this.firstRatio = firstRatio;
	}
	public Double getSecondRatio() {
		return secondRatio;
	}
	public void setSecondRatio(Double secondRatio) {
		this.secondRatio = secondRatio;
	}
	public List<ThreeObjectives> getFirstPareto() {
		return firstPareto;
	}
	public void setFirstPareto(List<ThreeObjectives> firstPareto) {
		this.firstPareto = firstPareto;
	}
	public List<ThreeObjectives> getSecondPareto() {
		return secondPareto;
	}
	public void setSecondPareto(List<ThreeObjectives> secondPareto) {
		this.secondPareto = secondPareto;
	}
	
	

}
