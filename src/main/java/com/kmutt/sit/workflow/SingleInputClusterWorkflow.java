package com.kmutt.sit.workflow;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.kmutt.sit.pegasus.dax.DaxNode;
import com.kmutt.sit.workflow.component.TaskGroup;

public class SingleInputClusterWorkflow extends AbstractClusterWorkflow {	

	private Logger logger = Logger.getLogger(SingleInputClusterWorkflow.class);

	public SingleInputClusterWorkflow() {
		super(ClusterType.SINGLE);
		// TODO Auto-generated constructor stub
	}

	@Override
	public GeneralClusterWorkflow convertWorkflowByClusterType() {
		// TODO Auto-generated method stub
		
		processCluster();
		mappingNodeTask();
		
		return this;
	}
	
	private void processCluster(){	
		logger.debug("SingleInputCluster: processCluster()");
		
		int[] index = {0};
		
		for(List<DaxNode> nodeList : this.nodeLevel) {		
			
			logger.debug("Level: " + nodeList.get(0).getLevelId() + " -> " + nodeList.toString());

			List<TaskGroup> groups = new ArrayList<TaskGroup>();
			
			Comparator<DaxNode> comparator = Comparator.comparing(DaxNode::getLevelId);
			comparator = comparator.thenComparing(Comparator.comparing(DaxNode::getActualRuntime).reversed());
			
			List<DaxNode> sortedNodeList = nodeList.stream().sorted(comparator).collect(Collectors.toList());
			
			logger.debug("    -> " + sortedNodeList.stream().map(m -> m.getId()).collect(Collectors.toList()));
			
			sortedNodeList.stream().filter(n -> n.getClusterId().equalsIgnoreCase(DaxNode.NULL_CLUSTER)).forEach(node -> {
				
				List<DaxNode> childList = node.getChilds().values().stream().collect(Collectors.toCollection(ArrayList::new));
				
				logger.debug("    -> create cluster for: " + node.getId() + " -> " + childList.stream().map(m -> m.getId()).collect(Collectors.toList()));
				
				TaskGroup taskGroup = null;
				
				if(childList.isEmpty()) {
					taskGroup = processClusterNonClusteredNode(index[0], node);
					
				} else {
					List<DaxNode> singleInputChildList = childList.stream().filter(
							c -> c.getClusterId().equalsIgnoreCase(DaxNode.NULL_CLUSTER) && 
							c.getParents().size() == 1
							).collect(Collectors.toList());
					
					if(singleInputChildList.isEmpty()) {
						taskGroup = processClusterNonClusteredNode(index[0], node);
						
					} else {
						Comparator<DaxNode> comparator2 = Comparator.comparing(DaxNode::getLevelId);
						comparator2 = comparator2.thenComparing(Comparator.comparing(DaxNode::getActualRuntime).reversed());
						
						singleInputChildList = singleInputChildList.stream().sorted(comparator2).collect(Collectors.toList());
						
						taskGroup = clustering(index[0], node, singleInputChildList.get(0));
						
					}
				}
				
				groups.add(taskGroup);
				this.taskGroupSotedByTopogicalOrder.add(taskGroup);
				
				index[0]++;
			});;
			
			if(!groups.isEmpty()) this.groupLevel.add(groups);
		}
		
	}
	
	private TaskGroup clustering(int taskGroupId, DaxNode parent, DaxNode child) {				
		TaskGroup taskGroup = new TaskGroup();
		taskGroup.setTaskGroupId(taskGroupId);
		
		List<DaxNode> nodeList = new ArrayList<DaxNode>();		
		nodeList.add(parent);
		nodeList.addAll(travelSingleInputNode(child));
		
		String clusterId = getClusterId(parent.getLevelId());
		
		logger.debug("Task ID: " + taskGroupId + " -> " + clusterId);
		
		nodeList.stream().forEach(node -> {
			node.setClusterId(clusterId);			
			logger.debug("\t -> " + node.toString());
			
		});
		
		taskGroup.setNodes(nodeList);
		
		return taskGroup;
	}
	
	private List<DaxNode> travelSingleInputNode(DaxNode child) {
		
		List<DaxNode> travelList = new ArrayList<DaxNode>();
		
		if(!child.getChilds().isEmpty()) {
			
			List<DaxNode> childList = child.getChilds().values().stream().collect(Collectors.toCollection(ArrayList::new));
			
			List<DaxNode> singleInputChildList = childList.stream().filter(
					c -> c.getClusterId().equalsIgnoreCase(DaxNode.NULL_CLUSTER) && 
					c.getParents().size() == 1
					).collect(Collectors.toList());
			
			if(!singleInputChildList.isEmpty()) {				
				Comparator<DaxNode> comparator = Comparator.comparing(DaxNode::getLevelId);
				comparator = comparator.thenComparing(Comparator.comparing(DaxNode::getActualRuntime).reversed());				
				singleInputChildList = singleInputChildList.stream().sorted(comparator).collect(Collectors.toList());
				
				travelList.add(child);				
				travelList.addAll(travelSingleInputNode(singleInputChildList.get(0)));
			} else {
				travelList.add(child);
			}
			
		} else {
			travelList.add(child);
		}
		
		return travelList;
	}
	
	private TaskGroup processClusterNonClusteredNode(int taskGroupId, DaxNode node) {				
		TaskGroup taskGroup = new TaskGroup();
		taskGroup.addNode(node);
		taskGroup.setTaskGroupId(taskGroupId);
		
		logger.debug("Task ID: " + taskGroupId + " -> " + taskGroup.getNodes().toString());
		
		return taskGroup;
	}
}
