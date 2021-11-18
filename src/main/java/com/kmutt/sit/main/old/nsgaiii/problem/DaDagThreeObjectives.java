package com.kmutt.sit.main.old.nsgaiii.problem;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.uma.jmetal.problem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.JMetalException;

import com.kmutt.sit.cloud.vm.model.VmAwsModelMapping;
import com.kmutt.sit.mop.parameter.Global;
import com.kmutt.sit.mop.runner.scheduling.planner.DagWorkflowEvaluator;
import com.kmutt.sit.mop.runner.scheduling.planner.DagWorkflowPlanner;
import com.kmutt.sit.workflow.GeneralClusterWorkflow;

@SuppressWarnings("serial")
public class DaDagThreeObjectives extends AbstractIntegerProblem {	

	private Logger logger = Logger.getLogger(DaDagThreeObjectives.class);
	
	final private int NO_OBJECTIVES = 3;	
	
	private GeneralClusterWorkflow workflow;

	public DaDagThreeObjectives() {
		setNumberOfObjectives(NO_OBJECTIVES);
		setName("ThreeObjectiveScheduling");
	}

	public DaDagThreeObjectives(GeneralClusterWorkflow workflow) throws JMetalException {
		
		this();
		
		setWorkflow(workflow);
		setNumberOfVariables(this.workflow.getTaskGroupSortedByTopologyOrder().size());

		List<Integer> lowerLimit = new ArrayList<>(getNumberOfVariables());
		List<Integer> upperLimit = new ArrayList<>(getNumberOfVariables());
		
		int lower = 0;
		int upper = (getNumberOfVariables() * VmAwsModelMapping.getNoOfTypes()) - 1;

		for (int i = 0; i < getNumberOfVariables(); i++) {
			lowerLimit.add(lower);
			upperLimit.add(upper);
		}

		setLowerLimit(lowerLimit);
		setUpperLimit(upperLimit);
	}

	/** Evaluate() method */
	public void evaluate(IntegerSolution solution) {

		logger.debug("");
		logger.debug("Start: Evaluate");
		
		resetWorkflow();
		
		DagWorkflowPlanner planner = new DagWorkflowPlanner(workflow, solution);
		planner.planning();
		if(logger.isDebugEnabled()) planner.printSolutionLits();
		
		DagWorkflowEvaluator evaluator = new DagWorkflowEvaluator(planner.getSchedulePlan());
		evaluator.evaluate();

		logger.debug(evaluator.toString());
		logger.debug("Finished: Evaluate");

		solution.setObjective(0, evaluator.getCost());
		solution.setObjective(1, evaluator.getMakespan());
		
		if(Global.THIRD_OBJECTIVE.equalsIgnoreCase("COMMU")) {
			solution.setObjective(2, evaluator.getNoOfRemoteCommunicationTask());	
		} else if(Global.THIRD_OBJECTIVE.equalsIgnoreCase("VM")) {
			solution.setObjective(2, evaluator.getNoOfVMs());	
		} else if(Global.THIRD_OBJECTIVE.equalsIgnoreCase("IDLE")) {
			solution.setObjective(2, evaluator.getIdleTimeCost());	
		} else {
			solution.setObjective(2, evaluator.getNoOfVMs());			
		}
		
	}
	
	private void resetWorkflow() {

		this.workflow.getTaskGroupSortedByTopologyOrder().stream().forEach(task -> {
			task.setNotFinish(true);
			task.getNodes().stream().forEach(node -> {
				node.setNotFinish(true);
			});
		});
		
	}
	
	private void setWorkflow(GeneralClusterWorkflow workflow) {
		this.workflow = workflow;
	}

}
