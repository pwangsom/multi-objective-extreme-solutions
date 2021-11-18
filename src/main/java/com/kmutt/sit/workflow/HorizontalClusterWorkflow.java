package com.kmutt.sit.workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.kmutt.sit.pegasus.dax.DaxNode;
import com.kmutt.sit.workflow.component.TaskGroup;

public class HorizontalClusterWorkflow extends AbstractClusterWorkflow {
	
	private Logger logger = Logger.getLogger(HorizontalClusterWorkflow.class);	

	public HorizontalClusterWorkflow() {
		super(ClusterType.HORIZONTAL);		
	}

	@Override
	public GeneralClusterWorkflow convertWorkflowByClusterType() {
		
		// TODO Auto-generated method stub
		processCluster();		
		mappingNodeTask();
		
		return this;
	}
	
	private void processCluster(){	
		logger.debug("HorizontalClusterWorkflow: processCluster()");	
		
		int[] index = {0};
		int[] levelId = {0};
		
		nodeLevel.stream().forEach(nodes -> {

			logger.debug("Level : " + levelId[0]);
			
			List<TaskGroup> groups = new ArrayList<TaskGroup>();
			
			Map<String, List<DaxNode>> nodesByName = nodes.stream().collect(Collectors.groupingBy(DaxNode::getName));
			
			nodesByName.forEach((k,v) -> {				
				
				logger.debug("Level : " + levelId[0] + ", Group ID: " + index[0] 
						   + ", Task Name: [" + k + "], having " + v.size() + " nodes.");
				
				TaskGroup group = new TaskGroup();
				group.setTaskGroupId(index[0]);
				
				v.stream().forEach(node -> {
					logger.debug("Group ID: " + index[0] + " adding node id: " + node.getId());
					group.addNode(node);
				});
				
				if(v.size() > 1) {
					String clusterId = getClusterId(levelId[0]);
					v.stream().forEach(node -> {
						node.setClusterId(clusterId);
					});
				}

				groups.add(group);
				this.taskGroupSotedByTopogicalOrder.add(group);				

				index[0]++;
			});

			this.groupLevel.add(groups);
			
			levelId[0]++;
		});
		
	}
	
}
