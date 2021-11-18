package com.kmutt.sit.mop.algorithm.scheduling;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.impl.AbstractGeneticAlgorithm;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

import com.kmutt.sit.utils.JavaHelper;

@SuppressWarnings("serial")
public abstract class AbstractContinueableGeneticAlgorithm<S extends Solution<?>> extends AbstractGeneticAlgorithm<S, List<S>> {
	
	protected List<S> initialPopulation;
	

	public AbstractContinueableGeneticAlgorithm(Problem<S> problem, List<S> initialPopulation) {
		super(problem);
		// TODO Auto-generated constructor stub
		
		this.initialPopulation = initialPopulation;
	}
	
	@Override
	public List<S> getResult() {
		return getPopulation();
	}
	
	@Override
	protected List<S> createInitialPopulation() {

		List<S> population = new ArrayList<>(getMaxPopulationSize());

		if (!JavaHelper.isNull(initialPopulation)
				&& !initialPopulation.isEmpty()) {		
			population = initialPopulation;		
		} else {
			for (int i = 0; i < getMaxPopulationSize(); i++) {
				S newIndividual = getProblem().createSolution();
				population.add(newIndividual);
			}
		}

		return population;
	}
}
