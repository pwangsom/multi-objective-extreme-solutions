package com.kmutt.sit.mop.algorithm;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIII;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.solution.Solution;

import com.kmutt.sit.mop.manager.configulations.ConfigurationInterface;
import com.kmutt.sit.mop.output.object.GeneralSolutionObjectWriter;
import com.kmutt.sit.mop.pareto.collection.ParetoIndicatorInstance;

public abstract class AbstractGeneralNsgaIII<C extends ConfigurationInterface, S extends Solution<?>> extends NSGAIII<S> implements NsgaInterface<C, S> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	

	private Logger logger = Logger.getLogger(AbstractGeneralNsgaIII.class);
	
	protected C config;	
	protected List<ParetoIndicatorInstance<S>> indicatorInstanceList;

	public AbstractGeneralNsgaIII(NSGAIIIBuilder<S> builder, C config) {
		super(builder);
		// TODO Auto-generated constructor stub
		
		this.config = config;
		indicatorInstanceList = new ArrayList<ParetoIndicatorInstance<S>>();
		
		logger.debug("");
		logger.info("Algorithm: " + getName());
		logger.info("Population Size: " + maxPopulationSize);
		logger.info("Max Generation : " + maxIterations);
		logger.debug("");
	}
	
	@Override
	public abstract String getAddPopulationInstanceLog();
	
	@Override
	protected void initProgress() {
		iterations = 1;
	}
	
	@Override
	protected boolean isStoppingConditionReached() {
		
		ParetoIndicatorInstance<S> popInstance = new ParetoIndicatorInstance<S>();		

		popInstance.setRefParetoFrontKey(this.config.getRefParetoFrontKey());
		popInstance.setInstanceKey(this.config.getRunningKey() + "_gen" + iterations);
		
		popInstance.setExperimentTreatmentKey(this.config.getExperimentTreatmentKey());
		popInstance.setInstanceRun(this.config.getCurrentRun());

		popInstance.setInstanceGeneration(iterations);
		
		popInstance.setParetoSet(getResult());
		
		indicatorInstanceList.add(popInstance);
		
		GeneralSolutionObjectWriter<S> writer = new GeneralSolutionObjectWriter<S>();
		writer.writeSolutionList(this.config, this.config.getRunningKey() + "_gen" + iterations, getResult());
		
		String generationLog = getAddPopulationInstanceLog();

		logger.debug("");
		logger.info(generationLog);
				
		return iterations >= maxIterations;
	}
	
	@Override
	protected void updateProgress() {		
		iterations++ ;
	}

	public List<ParetoIndicatorInstance<S>> getIndicatorInstanceList() {
		return indicatorInstanceList;
	}
}
