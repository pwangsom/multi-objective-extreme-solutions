package com.kmutt.sit.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.kmutt.sit.pegasus.dax.DaxNode;
import com.kmutt.sit.pegasus.dax.DaxNodeComType;
import com.kmutt.sit.pegasus.dax.DaxNodeType;
import com.kmutt.sit.utils.JavaHelper;
import com.kmutt.sit.workflow.component.TaskGroup;

public class P2pClusterWorkflow extends AbstractClusterWorkflow {
	
	private Logger logger = Logger.getLogger(P2pClusterWorkflow.class);	
	
	public P2pClusterWorkflow() {
		super(ClusterType.P2P);
	}

	@Override
	public GeneralClusterWorkflow convertWorkflowByClusterType() {
		// TODO Auto-generated method stub
		
		processCluster();
		convertNodeLevelToTaskGroup();
		mappingNodeTask();
		
		return this;
	}
	
	private void convertNodeLevelToTaskGroup() {
		
		Map<String, TaskGroup> clusterTaksGroupMapping = new HashMap<String, TaskGroup>();

		int[] index = {0};		
		
		for(List<DaxNode> nodes : nodeLevel) {
			
			List<TaskGroup> groups = new ArrayList<TaskGroup>();

			nodes = getSortedNodeList(nodes);
			
			nodes.stream().forEach(node -> {
				
				if(node.getClusterId().equalsIgnoreCase(DaxNode.NULL_CLUSTER) 
						|| !clusterTaksGroupMapping.containsKey(node.getClusterId())) {
					
					TaskGroup taskGroup = new TaskGroup();
					taskGroup.addNode(node);
					taskGroup.setTaskGroupId(index[0]);	
					
					if(!node.getClusterId().equalsIgnoreCase(DaxNode.NULL_CLUSTER)){
						clusterTaksGroupMapping.put(node.getClusterId(), taskGroup);
					}
					
					groups.add(taskGroup);
					this.taskGroupSotedByTopogicalOrder.add(taskGroup);

					index[0]++;
					
				} else {
					clusterTaksGroupMapping.get(node.getClusterId()).addNode(node);
				}
				
				
			});
			
			if(!groups.isEmpty()) this.groupLevel.add(groups);
			
		}
	}
	
	private void processCluster(){	
		logger.debug("P2pClusterWorkflow: processCluster()");	
		
		int i = 0;
		
		for(List<DaxNode> nodeList : this.nodeLevel) {
			i++;
			clustering(nodeList, i);
		}
		
	}

	private void clustering(List<DaxNode> nodeList, int levelId) {
		// TODO Auto-generated method stub
		
		for(DaxNode node : nodeList) {
			
			if(node.getClusterId().equalsIgnoreCase(DaxNode.NULL_CLUSTER) 
					&& (node.getDaxNodeComType() == DaxNodeComType.ONE_TO_ONE
							|| node.getDaxNodeComType() == DaxNodeComType.MANY_TO_ONE)) {
				
				List<DaxNode> temp = new ArrayList<DaxNode>();
				
				travelChildNode(temp, node);
				
				if(!temp.isEmpty()) {
					
					String clusterId;
					
					if(node.getDaxNodeComType() == DaxNodeComType.ONE_TO_ONE
							|| (node.getDaxNodeComType() == DaxNodeComType.MANY_TO_ONE
									&& temp.get(temp.size()-1).getDaxNodeType() == DaxNodeType.END)) {
						temp.add(0, node);
						clusterId = getClusterId(levelId);
						
					} else {
						int nextLevel = levelId + 1;
						clusterId = getClusterId(nextLevel);
					}
					
					if(temp.size() > 1) {						
						temp.stream().forEach(n -> {
							n.setClusterId(clusterId);
						});

						
						this.daxCluster.put(clusterId, temp);
						
						logger.debug(clusterId + " -> " + temp.stream().map(e -> e.getId())
								.collect(Collectors.toList()).toString());
					}
				}
				
			}
			
		}
		
	}
	
	private void travelChildNode(List<DaxNode> list, DaxNode parent) {
		
		if(!JavaHelper.isNull(parent.getChilds()) && parent.getChilds().size() == 1) {
			
			DaxNode child = parent.getChilds().entrySet().iterator().next().getValue();
			
			if(child.getClusterId().equalsIgnoreCase(DaxNode.NULL_CLUSTER) && child.getDaxNodeComType() == DaxNodeComType.ONE_TO_ONE) {				
				list.add(child);				

				travelChildNode(list, child);				
			}
			
		}
	}
}
