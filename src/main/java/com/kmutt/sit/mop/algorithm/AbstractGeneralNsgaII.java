package com.kmutt.sit.mop.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;

import com.kmutt.sit.mop.manager.configulations.ConfigurationInterface;
import com.kmutt.sit.mop.pareto.collection.ParetoIndicatorInstance;

public abstract class AbstractGeneralNsgaII<C extends ConfigurationInterface, S extends Solution<?>> extends NSGAII<S> implements NsgaInterface<C, S> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Logger logger = Logger.getLogger(AbstractGeneralNsgaII.class);
	
	protected C config;	
	protected List<ParetoIndicatorInstance<S>> indicatorInstanceList;

	public AbstractGeneralNsgaII(Problem<S> problem, int maxEvaluations, int populationSize,
			CrossoverOperator<S> crossoverOperator, MutationOperator<S> mutationOperator,
			SelectionOperator<List<S>, S> selectionOperator, Comparator<S> dominanceComparator,
			SolutionListEvaluator<S> evaluator, C config) {
		super(problem, maxEvaluations, populationSize, crossoverOperator, mutationOperator, selectionOperator,
				dominanceComparator, evaluator);
		// TODO Auto-generated constructor stub
		
		this.config = config;
		indicatorInstanceList = new ArrayList<ParetoIndicatorInstance<S>>();
		
		logger.debug("");
		logger.info("Algorithm: " + getName());
		logger.info("Population Size: " + maxPopulationSize);
		logger.info("Max Generation : " + maxEvaluations);
		logger.debug("");
	}
	
	@Override
	public abstract String getAddPopulationInstanceLog();
	
	@Override
	protected void initProgress() {
		evaluations = 1;
	}
	
	@Override
	protected boolean isStoppingConditionReached() {
		
		ParetoIndicatorInstance<S> popInstance = new ParetoIndicatorInstance<S>();		

		popInstance.setRefParetoFrontKey(this.config.getRefParetoFrontKey());
		popInstance.setInstanceKey(this.config.getRunningKey() + "_gen" + evaluations);
		
		popInstance.setExperimentTreatmentKey(this.config.getExperimentTreatmentKey());
		popInstance.setInstanceRun(this.config.getCurrentRun());

		popInstance.setInstanceGeneration(evaluations);
		
		popInstance.setParetoSet(getResult());
		
		indicatorInstanceList.add(popInstance);
		
		String generationLog = getAddPopulationInstanceLog();

		logger.debug("");
		logger.info(generationLog);
				
		return evaluations >= maxEvaluations;
	}
	
	@Override
	protected void updateProgress() {		
		evaluations++ ;
	}
	
	public List<ParetoIndicatorInstance<S>> getIndicatorInstanceList() {
		return indicatorInstanceList;
	}
}
