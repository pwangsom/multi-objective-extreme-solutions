package com.kmutt.sit.workflow;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.kmutt.sit.pegasus.dax.DaxNode;
import com.kmutt.sit.workflow.component.TaskGroup;

public class NoneClusterWorkflow extends AbstractClusterWorkflow {
	
	private Logger logger = Logger.getLogger(NoneClusterWorkflow.class);	
	
	public NoneClusterWorkflow() {
		super(ClusterType.NONE);
	}

	@Override
	public GeneralClusterWorkflow convertWorkflowByClusterType() {
		// TODO Auto-generated method stub
		
		logger.debug("NoneClusterWorkflow: processCluster()");	
		
		int[] index = {0};		
		
		for(List<DaxNode> nodes : nodeLevel) {
			
			List<TaskGroup> groups = new ArrayList<TaskGroup>();

			nodes = getSortedNodeList(nodes);
			
			nodes.stream().forEach(node -> {
				
				TaskGroup group = new TaskGroup();
				group.addNode(node);
				group.setTaskGroupId(index[0]);
				
				groups.add(group);
				this.taskGroupSotedByTopogicalOrder.add(group);
				
				index[0]++;
			});
			
			this.groupLevel.add(groups);
			
		}
		
		mappingNodeTask();
		
		return this;
	}
}
