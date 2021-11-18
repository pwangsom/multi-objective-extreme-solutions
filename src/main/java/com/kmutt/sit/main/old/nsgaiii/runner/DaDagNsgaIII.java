package com.kmutt.sit.main.old.nsgaiii.runner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.uma.jmetal.algorithm.impl.AbstractGeneticAlgorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.util.EnvironmentalSelection;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.util.ReferencePoint;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.solutionattribute.Ranking;
import org.uma.jmetal.util.solutionattribute.impl.DominanceRanking;

import com.kmutt.sit.cloud.vm.model.VmAwsModelMapping;
import com.kmutt.sit.mop.parameter.Global;

@SuppressWarnings("serial")
public class DaDagNsgaIII<S extends Solution<?>> extends AbstractGeneticAlgorithm<S, List<S>> {

	private Logger logger = Logger.getLogger(DaDagNsgaIII.class);

	protected int iterations;
	protected int maxIterations;

	protected SolutionListEvaluator<S> evaluator;

	protected Vector<Integer> numberOfDivisions;
	protected List<ReferencePoint<S>> referencePoints = new Vector<>();
	
	private String firstObjectiveName;
	private String secondObjectiveName;
	private String thirdObjectiveName;

	/** Constructor */
	public DaDagNsgaIII(DaDagNsgaIIIBuilder<S> builder) { // can be created from the NSGAIIIBuilder within the same
															// package
		super(builder.getProblem());
		maxIterations = builder.getMaxIterations();

		crossoverOperator = builder.getCrossoverOperator();
		mutationOperator = builder.getMutationOperator();
		selectionOperator = builder.getSelectionOperator();
		
		firstObjectiveName = builder.getFirstObjectiveName();
		secondObjectiveName = builder.getSecondObjectiveName();
		thirdObjectiveName = builder.getThirdObjectiveName();

		evaluator = builder.getEvaluator();

		/// NSGAIII
		numberOfDivisions = new Vector<>(1);
		numberOfDivisions.add(12); // Default value for 3D problems

		(new ReferencePoint<S>()).generateReferencePoints(referencePoints, getProblem().getNumberOfObjectives(),
				numberOfDivisions);

		int populationSize = referencePoints.size();
		while (populationSize % 4 > 0) {
			populationSize++;
		}

		setMaxPopulationSize(populationSize);

		logger.info("Reference Point: " + referencePoints.size());
		logger.info("Population Size: " + populationSize);
		;
	}

	@Override
	protected void initProgress() {
		iterations = 1;
	}

	@Override
	protected void updateProgress() {
		iterations++;
	}

	@Override
	protected boolean isStoppingConditionReached() {
		return iterations >= maxIterations;
	}

	@Override
	protected List<S> evaluatePopulation(List<S> population) {
		
		logger.info("DA-DAG NSGA-III: [Iteration: " + iterations + ", Current Population Size: " + population.size() + "]");
		
		population = evaluator.evaluate(population, getProblem());

		tracePopulation(population);
		
		return population;
	}
	
	@SuppressWarnings("unchecked")
	private void tracePopulation(List<S> population) {
		
		List<IntegerSolution> solutions = (List<IntegerSolution>) population;
		
		Comparator<IntegerSolution> byCost = (IntegerSolution s1, IntegerSolution s2) ->Double.compare(s1.getObjective(0), s2.getObjective(0));
		Comparator<IntegerSolution> byMakespan = (IntegerSolution s1, IntegerSolution s2) ->Double.compare(s1.getObjective(1), s2.getObjective(1));
		Comparator<IntegerSolution> byThird = (IntegerSolution s1, IntegerSolution s2) ->Double.compare(s1.getObjective(2), s2.getObjective(2));
		
		IntegerSolution cost = solutions.stream().min(byCost).get();
		IntegerSolution makespan = solutions.stream().min(byMakespan).get();
		IntegerSolution third = solutions.stream().min(byThird).get();
		
		List<Integer> costSolution = new ArrayList<Integer>();
		List<Integer> makespanSolution = new ArrayList<Integer>();
		List<Integer> thirdSolution = new ArrayList<Integer>();
		
		for (int i = 0; i < cost.getNumberOfVariables(); i++) {
			costSolution.add(cost.getVariableValue(i));			
			makespanSolution.add(makespan.getVariableValue(i));			
			thirdSolution.add(third.getVariableValue(i));			
		}
		
		String trace = ("DA-DAG-NSGA-III: [Iteration, Population, %s, %s, %s]: [%d, %d, %.4f, %.4f, %.4f]");
		
		logger.info(String.format(trace, this.firstObjectiveName, this.secondObjectiveName, this.thirdObjectiveName, iterations, solutions.size(), cost.getObjective(0), makespan.getObjective(1), third.getObjective(2)));
		
		logger.info("DA-DAG-NSGA-III: [" + cost.getObjective(0) + ", " + cost.getObjective(1) + ", " + cost.getObjective(2) + "]: " + costSolution);
		logger.info("DA-DAG-NSGA-III: [" + makespan.getObjective(0) + ", " + makespan.getObjective(1) + ", " + makespan.getObjective(2) + "]: " + makespanSolution);
		logger.info("DA-DAG-NSGA-III: [" + third.getObjective(0) + ", " + third.getObjective(1) + ", " + third.getObjective(2) + "]: " + thirdSolution);
		
	}

	@Override
	protected List<S> selection(List<S> population) {
		List<S> matingPopulation = new ArrayList<>(population.size());
		for (int i = 0; i < getMaxPopulationSize(); i++) {
			S solution = selectionOperator.execute(population);
			matingPopulation.add(solution);
		}

		return matingPopulation;
	}

	@Override
	protected List<S> reproduction(List<S> population) {
		List<S> offspringPopulation = new ArrayList<>(getMaxPopulationSize());
		for (int i = 0; i < getMaxPopulationSize(); i += 2) {
			List<S> parents = new ArrayList<>(2);
			parents.add(population.get(i));
			parents.add(population.get(Math.min(i + 1, getMaxPopulationSize() - 1)));

			List<S> offspring = crossoverOperator.execute(parents);

			mutationOperator.execute(offspring.get(0));
			mutationOperator.execute(offspring.get(1));

			offspringPopulation.add(offspring.get(0));
			offspringPopulation.add(offspring.get(1));
		}
		return offspringPopulation;
	}

	private List<ReferencePoint<S>> getReferencePointsCopy() {
		List<ReferencePoint<S>> copy = new ArrayList<>();
		for (ReferencePoint<S> r : this.referencePoints) {
			copy.add(new ReferencePoint<>(r));
		}
		return copy;
	}

	@Override
	protected List<S> replacement(List<S> population, List<S> offspringPopulation) {

		List<S> jointPopulation = new ArrayList<>();
		jointPopulation.addAll(population);
		jointPopulation.addAll(offspringPopulation);

		Ranking<S> ranking = computeRanking(jointPopulation);

		// List<Solution> pop = crowdingDistanceSelection(ranking);
		List<S> pop = new ArrayList<>();
		List<List<S>> fronts = new ArrayList<>();
		int rankingIndex = 0;
		int candidateSolutions = 0;
		while (candidateSolutions < getMaxPopulationSize()) {
			fronts.add(ranking.getSubfront(rankingIndex));
			candidateSolutions += ranking.getSubfront(rankingIndex).size();
			if ((pop.size() + ranking.getSubfront(rankingIndex).size()) <= getMaxPopulationSize())
				addRankedSolutionsToPopulation(ranking, rankingIndex, pop);
			rankingIndex++;
		}

		// A copy of the reference list should be used as parameter of the environmental
		// selection
		EnvironmentalSelection<S> selection = new EnvironmentalSelection<>(fronts, getMaxPopulationSize(),
				getReferencePointsCopy(), getProblem().getNumberOfObjectives());

		pop = selection.execute(pop);

		return pop;
	}

	@Override
	public List<S> getResult() {
		return getNonDominatedSolutions(getPopulation());
	}
	
	public List<S> getAllPopulation(){
		return getPopulation();		
	}

	protected Ranking<S> computeRanking(List<S> solutionList) {
		Ranking<S> ranking = new DominanceRanking<>();
		ranking.computeRanking(solutionList);

		return ranking;
	}

	protected void addRankedSolutionsToPopulation(Ranking<S> ranking, int rank, List<S> population) {
		List<S> front;

		front = ranking.getSubfront(rank);

		for (int i = 0; i < front.size(); i++) {
			population.add(front.get(i));
		}
	}

	protected List<S> getNonDominatedSolutions(List<S> solutionList) {
		return SolutionListUtils.getNondominatedSolutions(solutionList);
	}

	@Override
	public String getName() {
		return "NSGAIII - DA-DAG";
	}

	@Override
	public String getDescription() {
		return "Nondominated Sorting Genetic Algorithm version III for DA-DAG";
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected List<S> createInitialPopulation() {
		
		List<S> population = new ArrayList<>(getMaxPopulationSize());
		
		int startIdx = 0;
		
		if(Global.SUPER_AGENT) {
			population.add((S) createSuperAgentOfCost());
			population.add((S) createSuperAgentOfMakespan());
			population.add((S) createSuperAgentOfVMs());
			startIdx = 3;
		}
		
		for (int i = startIdx; i < getMaxPopulationSize(); i++) {
			S newIndividual = getProblem().createSolution();
			population.add(newIndividual);
		}
		
		return population;
	}
	
	private IntegerSolution createSuperAgentOfCost() {
		IntegerSolution superAgent = (IntegerSolution) getProblem().createSolution();
		
		for(int i = 0; i < superAgent.getNumberOfVariables(); i++) {
			superAgent.setVariableValue(i, 0);
		}
		
		return superAgent;
	}
	
	private IntegerSolution createSuperAgentOfMakespan() {
		IntegerSolution superAgent = (IntegerSolution) getProblem().createSolution();
		Integer value = 5;
		
		for(int i = 0; i < superAgent.getNumberOfVariables(); i++) {
			superAgent.setVariableValue(i, value);
			value += VmAwsModelMapping.getNoOfTypes();
		}
		
		return superAgent;
	}
	
	private IntegerSolution createSuperAgentOfVMs() {
		IntegerSolution superAgent = (IntegerSolution) getProblem().createSolution();
		JMetalRandom randomGenerator = JMetalRandom.getInstance();
		Integer value = randomGenerator.nextInt(superAgent.getLowerBound(0), superAgent.getUpperBound(0));
				
		for(int i = 0; i < superAgent.getNumberOfVariables(); i++) {
			superAgent.setVariableValue(i, value);
		}
		
		return superAgent;
	}

}
