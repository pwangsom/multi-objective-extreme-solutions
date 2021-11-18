package com.kmutt.sit.workflow.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.kmutt.sit.pegasus.dax.DaxNode;

public class TaskGroup {

	private int taskGroupId = -1;
	private List<DaxNode> nodes = new ArrayList<DaxNode>();
	private boolean isNotFinish = true;
	
	private int taskGroupLevel = -1;
	
	private int lowestNodeLevel = -1;
	private int highestNodeLevel = -1;

	public boolean isNotFinish() {
		return isNotFinish;
	}

	public void setNotFinish(boolean isNotFinish) {
		this.isNotFinish = isNotFinish;
	}

	public List<DaxNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<DaxNode> nodes) {
		this.nodes = nodes;
	}
	
	public void addNode(DaxNode node) {
		this.nodes.add(node);
	}
	
	public void addNodes(List<DaxNode> nodes) {
		this.nodes.addAll(nodes);
	}
	
	public Double getTotalActualRuntime() {
		return this.nodes.stream().map(n -> n.getActualRuntime())
				.collect(Collectors.toList()).stream()
				.mapToDouble(Double::doubleValue).sum();
	}

	public int getTaskGroupId() {
		return taskGroupId;
	}

	public void setTaskGroupId(int taskGroupId) {
		this.taskGroupId = taskGroupId;
	}
	
	public int getTaskGroupLevel() {
		return taskGroupLevel;
	}

	public void setTaskGroupLevel(int taskGroupLevel) {
		this.taskGroupLevel = taskGroupLevel;
	}

	public int getLowestNodeLevel() {
		return lowestNodeLevel;
	}

	public void setLowestNodeLevel(int lowestNodeLevel) {
		this.lowestNodeLevel = lowestNodeLevel;
	}

	public int getHighestNodeLevel() {
		return highestNodeLevel;
	}

	public void setHighestNodeLevel(int highestNodeLevel) {
		this.highestNodeLevel = highestNodeLevel;
	}

/*	public String toString() {		
		return String.valueOf(taskGroupId);
	}*/
	
	public String toString() {
		
		List<String> sList = this.nodes.stream().map(m -> m.getId()).collect(Collectors.toList());
		
		return Arrays.asList(sList).toString();
	}
}
