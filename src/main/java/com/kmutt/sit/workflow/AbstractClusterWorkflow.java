/**
 * 
 */
package com.kmutt.sit.workflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.kmutt.sit.cloud.CloudScheduling;
import com.kmutt.sit.mop.parameter.Configuration;
import com.kmutt.sit.pegasus.dax.DaxNode;
import com.kmutt.sit.utils.JavaHelper;
import com.kmutt.sit.workflow.component.TaskGroup;

/**
 * @author Peerasak Wangsom
 * 
 * 
 * In order to implement new clustering technique by extending this abstract class.
 * Two variables required by DagWorkflowPlanner class are taskGroupSotedByTopogicalOrder and nodeTaskMapping.
 * New clustering class should group tasks and put the results to taskGroupSotedByTopogicalOrder by itself.
 * However, it can use mappingNodeTask method for converting the results from taskGroupSotedByTopogicalOrder to nodeTaskMapping.
 *
 */
public abstract class AbstractClusterWorkflow implements GeneralClusterWorkflow {
	
	private Logger logger = Logger.getLogger(AbstractClusterWorkflow.class);	

	protected Map<String, List<DaxNode>> daxCluster = new TreeMap<>();

	protected int noOfNode = 0;
	protected int clusterId = 0;
	protected List<List<DaxNode>> nodeLevel;
	protected List<DaxNode> nodeList;
	protected List<List<TaskGroup>> groupLevel;
	protected List<TaskGroup> taskGroupSotedByTopogicalOrder;

	protected Map<String, DaxNode> daxNodes;
	protected Map<String, TaskGroup> nodeTaskMapping;
	
	protected CloudScheduling.SchedulingType schedulingType = Configuration.SCHEDULING_TYPE;
	protected ClusterType clusterType;
	
	public AbstractClusterWorkflow(ClusterType clusterType) {
		this.clusterType = clusterType;
		this.groupLevel = new ArrayList<List<TaskGroup>>();
		this.taskGroupSotedByTopogicalOrder = new ArrayList<TaskGroup>();
		this.nodeTaskMapping = new HashMap<String, TaskGroup>();
	}

	public abstract GeneralClusterWorkflow convertWorkflowByClusterType();

	/* (non-Javadoc)
	 * @see com.dag.da.workflow.WorkflowInterface#getNodeLevelList()
	 */
	@Override
	public List<List<TaskGroup>> getGroupLevel() {
		// TODO Auto-generated method stub
		return groupLevel;
	}

	/* (non-Javadoc)
	 * @see com.dag.da.workflow.WorkflowInterface#setNodeLevelList(java.util.List)
	 */
	@Override
	public void setNodeGroupList(List<List<TaskGroup>> nodeGroupList) {
		// TODO Auto-generated method stub
		this.groupLevel = nodeGroupList;
	}

	/* (non-Javadoc)
	 * @see com.dag.da.workflow.WorkflowInterface#getTopologyOrderList()
	 */
	@Override
	public List<TaskGroup> getTaskGroupSortedByTopologyOrder() {
		// TODO Auto-generated method stub
		return taskGroupSotedByTopogicalOrder;
	}
	
	public TaskGroup getTaskGroupByTaskGroupId(int taskGroupId) {
		return taskGroupSotedByTopogicalOrder.stream().filter(task -> task.getTaskGroupId() == taskGroupId).findFirst().get();
	}

	/* (non-Javadoc)
	 * @see com.dag.da.workflow.WorkflowInterface#setTopologyOrderList(java.util.List)
	 */
	@Override
	public void setTaskGroupSortedByTopologyOrder(List<TaskGroup> taskGroupSotedByTopogicalOrder) {
		// TODO Auto-generated method stub
		this.taskGroupSotedByTopogicalOrder = taskGroupSotedByTopogicalOrder;
	}

	@Override
	public GeneralClusterWorkflow setNodeLevelList(List<List<DaxNode>> nodeLevelList) {
		// TODO Auto-generated method stub
		this.nodeLevel = nodeLevelList;
		this.nodeList = new ArrayList<DaxNode>();
		this.nodeList = this.nodeLevel.stream().flatMap(List::stream).collect(Collectors.toList());
		this.nodeList = JavaHelper.getSortedNodeListBySchedulingType(this.nodeList, schedulingType);
		
		return this;
	}

	public CloudScheduling.SchedulingType getSchedulingType() {
		return schedulingType;
	}

	public void setSchedulingType(CloudScheduling.SchedulingType schedulingType) {
		this.schedulingType = schedulingType;
	}

	public int getNoOfNode() {
		return noOfNode;
	}
	
	@Override
	public GeneralClusterWorkflow setDaxNodes(Map<String, DaxNode> daxNodes) {
		// TODO Auto-generated method stub
		this.daxNodes = daxNodes;
		this.noOfNode = this.daxNodes.size();
		return this;
	}
	

	public void printWorkflowByGroupLevel() {
		logger.debug("printWorkflowByGroupLevel()");
		
		for(List<TaskGroup> groupList : this.groupLevel) {				
			List<String> sList = groupList.stream().map(n -> n.toString()).collect(Collectors.toList());
			logger.debug(Arrays.asList(sList).toString());
		}
	}

	protected List<DaxNode> getSortedNodeList(List<DaxNode> unsortedNodeList){		
		Comparator<DaxNode> comparator = Comparator.comparing(DaxNode::getLevelId);
		
		if(this.schedulingType == CloudScheduling.SchedulingType.MIN) {
			comparator = comparator.thenComparing(Comparator.comparing(DaxNode::getActualRuntime));			
		} else if(this.schedulingType == CloudScheduling.SchedulingType.MAX) {
			comparator = comparator.thenComparing(Comparator.comparing(DaxNode::getActualRuntime).reversed());			
		} else {
			comparator = comparator.thenComparing(Comparator.comparing(DaxNode::getMatrixId));				
		}
		
		return unsortedNodeList.stream().filter(n -> n.isNotFinish() == true).sorted(comparator).collect(Collectors.toList());		
	}

	public ClusterType getClusterType() {
		return clusterType;
	}

	public Map<String, List<DaxNode>> getDaxCluster() {
		return daxCluster;
	}
	
	@Override
	public Map<String, TaskGroup> getNodeTaskMapping() {
		// TODO Auto-generated method stub
		return nodeTaskMapping;
	}
	
	@Override
	public TaskGroup getTaskByNodeId(String nodeId) {
		return nodeTaskMapping.get(nodeId);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	protected String getClusterId(int levelId) {
		this.clusterId++;
		return String.format("%03d-%03d", levelId, this.clusterId);
	}
	
	protected void mappingNodeTask() {		
		taskGroupSotedByTopogicalOrder.stream().forEach(task -> {
			task.getNodes().stream().forEach(node -> {
				nodeTaskMapping.put(node.getId(), task);
			});
		});		
	}

	public List<DaxNode> getTopologicalOrderNodeList(){
		return this.nodeList;
	}
}
