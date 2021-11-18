package com.kmutt.sit.workflow;

import java.util.List;
import java.util.Map;

import com.kmutt.sit.cloud.CloudScheduling;
import com.kmutt.sit.pegasus.dax.DaxNode;
import com.kmutt.sit.workflow.component.TaskGroup;

public interface GeneralClusterWorkflow extends Cloneable {
	public GeneralClusterWorkflow setNodeLevelList(List<List<DaxNode>> nodeList);
	public List<List<TaskGroup>> getGroupLevel();
	public void setNodeGroupList(List<List<TaskGroup>> nodeLevelList);
	public List<TaskGroup> getTaskGroupSortedByTopologyOrder();
	public void setTaskGroupSortedByTopologyOrder(List<TaskGroup> topologyListNormal);
	public GeneralClusterWorkflow convertWorkflowByClusterType();
	public CloudScheduling.SchedulingType getSchedulingType();
	public void setSchedulingType(CloudScheduling.SchedulingType schedulingType);
	public int getNoOfNode();
	public GeneralClusterWorkflow setDaxNodes(Map<String, DaxNode> daxNodes);
	public void printWorkflowByGroupLevel();
	public Map<String, List<DaxNode>> getDaxCluster();
	public Map<String, TaskGroup> getNodeTaskMapping();
	public TaskGroup getTaskByNodeId(String nodeId);
	public List<DaxNode> getTopologicalOrderNodeList();
}