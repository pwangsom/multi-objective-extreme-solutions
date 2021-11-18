package com.kmutt.sit.mop.problem.scheduling;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.uma.jmetal.problem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;

import com.kmutt.sit.mop.manager.scheduling.DagSchedulingConfiguration;
import com.kmutt.sit.mop.runner.scheduling.planner.DagWorkflowEvaluator;
import com.kmutt.sit.mop.runner.scheduling.planner.DagWorkflowPlanner;
import com.kmutt.sit.workflow.GeneralClusterWorkflow;

@SuppressWarnings("serial")
public class DagSchedulingIntegerProblem extends AbstractIntegerProblem {

	private Logger logger = Logger.getLogger(DagSchedulingIntegerProblem.class);
	
	final private int NO_OBJECTIVES = 3;
	
	private DagSchedulingConfiguration config;
	private GeneralClusterWorkflow workflow;
	
	public DagSchedulingIntegerProblem(DagSchedulingConfiguration config) {

		setNumberOfObjectives(NO_OBJECTIVES);
		setName("DagSchedulingIntegerProblem");
		
		this.config = config;
		
		workflow = this.config.getCurrentWorkflow();
		setNumberOfVariables(this.workflow.getTaskGroupSortedByTopologyOrder().size());

		List<Integer> lowerLimit = new ArrayList<>(getNumberOfVariables());
		List<Integer> upperLimit = new ArrayList<>(getNumberOfVariables());
		
		int lower = 0;
		int upper = (getNumberOfVariables() * this.config.getProviderModel().getNoOfTypes()) - 1;

		for (int i = 0; i < getNumberOfVariables(); i++) {
			lowerLimit.add(lower);
			upperLimit.add(upper);
		}

		setLowerLimit(lowerLimit);
		setUpperLimit(upperLimit);
	}
	

	@Override
	public void evaluate(IntegerSolution solution) {
		// TODO Auto-generated method stub
		
		logger.debug("");
		logger.debug("Start: Evaluate");
		
		resetWorkflow();
		
		DagWorkflowPlanner planner = new DagWorkflowPlanner(workflow, solution, this.config);
		planner.planning();
		if(logger.isDebugEnabled()) planner.printSolutionLits();
		
		DagWorkflowEvaluator evaluator = new DagWorkflowEvaluator(planner.getSchedulePlan());
		evaluator.evaluate();

		solution.setObjective(0, evaluator.getCost());
		solution.setObjective(1, evaluator.getMakespan());
		
		if(this.config.getThirdObjectiveType() == ThirdObjectiveType.COMMU) {			
			solution.setObjective(2, evaluator.getNoOfRemoteCommunicationTask());
			logger.debug(String.format("[Cost: %.4f, Makespan: %.4f, NoOfCommu: %d]", evaluator.getCost(), evaluator.getMakespan(), evaluator.getNoOfRemoteCommunicationTask()));
		} else if(this.config.getThirdObjectiveType() == ThirdObjectiveType.VM) {
			solution.setObjective(2, evaluator.getNoOfVMs());
			logger.debug(String.format("[Cost: %.4f, Makespan: %.4f, NoOfVm: %d]", evaluator.getCost(), evaluator.getMakespan(), evaluator.getNoOfVMs()));
		}

		logger.debug(evaluator.toString());
		logger.debug("Finished: Evaluate");
	}
	
	private void resetWorkflow() {
		this.workflow.getTaskGroupSortedByTopologyOrder().stream().forEach(task -> {
			task.setNotFinish(true);
			task.getNodes().stream().forEach(node -> {
				node.setNotFinish(true);
			});
		});
		
	}

}
