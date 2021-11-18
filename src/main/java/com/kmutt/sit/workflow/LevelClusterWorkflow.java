package com.kmutt.sit.workflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.kmutt.sit.pegasus.dax.DaxNode;
import com.kmutt.sit.pegasus.dax.DaxNodeComType;
import com.kmutt.sit.pegasus.dax.DaxNodeType;
import com.kmutt.sit.utils.JavaHelper;
import com.kmutt.sit.workflow.component.TaskGroup;

public class LevelClusterWorkflow extends AbstractClusterWorkflow {

	private Logger logger = Logger.getLogger(LevelClusterWorkflow.class);
	
	private Map<String, Integer> nodeTaskMapping;
	private Map<Integer, Integer> taskLevelMapping;

	public LevelClusterWorkflow() {
		super(ClusterType.LEVEL);
	}

	@Override
	public GeneralClusterWorkflow convertWorkflowByClusterType() {
		// TODO Auto-generated method stub

		List<List<TaskGroup>> p2pCluster = processP2pClustering();
		
		int[] index = {0};
		
		logger.debug("===Print levelCluster===");
		
		for (List<TaskGroup> groupLevel : p2pCluster) {
			
			List<List<String>> levelList = new ArrayList<List<String>>();

			for (TaskGroup group : groupLevel) {
				
				List<DaxNode> nodes = group.getNodes();
				
				List<String> sList = nodes.stream().map(n -> n.getId()).collect(Collectors.toList());
				
				sList.add(0, "{" + group.getTaskGroupId() + "}");
				
				levelList.add(sList);
			}

			logger.debug(index[0] + " -> " + Arrays.asList(levelList).toString());
			
			index[0]++;
		}
		
		processLevelClustering(p2pCluster);
		
		mappingNodeTask();

		return this;
	}
	
	private void processLevelClustering(List<List<TaskGroup>> p2pClusterLevel) {
		
		logger.debug("processLevelClusterWorkflow: processCluster()");	
		
		int size = p2pClusterLevel.stream().mapToInt(list -> list.size()).sum();
		
		Integer[][] taskDistanceMatrix = createDistanceMatrix(p2pClusterLevel, size);
		List<TaskGroup> levelCluster = processLevelClustering(p2pClusterLevel, taskDistanceMatrix, size);
		
		taskGroupSotedByTopogicalOrder.clear();
		taskGroupSotedByTopogicalOrder.addAll(levelCluster);
		
		logger.debug("");
		logger.debug("Task group list: ");
		
		taskGroupSotedByTopogicalOrder.stream().forEach(taskGroup -> {
			logger.debug("[" + taskGroup.getTaskGroupId() + ", " + taskGroup.getTaskGroupLevel() + "] -> " + taskGroup.toString());
		});
		
		int maxLevel = taskGroupSotedByTopogicalOrder.stream().max(Comparator.comparing(TaskGroup::getTaskGroupLevel)).get().getTaskGroupLevel();
		
		logger.debug("");
		logger.debug("Max level: " + maxLevel);
		
		IntStream.range(0, maxLevel + 1).forEach(i -> {
			groupLevel.add(new ArrayList<TaskGroup>());
		});
		
		taskGroupSotedByTopogicalOrder.stream().forEach(task -> {			
			int taskLevel = task.getTaskGroupLevel();
			groupLevel.get(taskLevel).add(task);
			
			String clusterId = getClusterId(taskLevel);
			
			task.getNodes().stream().forEach(node -> {
				node.setClusterId(clusterId);
			});
			
		});
		
		groupLevel.removeIf(List<TaskGroup>::isEmpty);
		
		logger.debug("");
		
		IntStream.range(0, groupLevel.size()).forEach(i -> {
			logger.debug(i + " -> " + groupLevel.get(i).stream().map(m -> m.getTaskGroupId()).collect(Collectors.toList()));
		});

		logger.debug("");
	}
	
	private List<TaskGroup> processLevelClustering(List<List<TaskGroup>> p2pClusterLevel, Integer[][] taskDistanceMatrix, int size) {

		int[] max = {0};
		max[0] = JavaHelper.getMaxValueIntegerMatrix(taskDistanceMatrix);
		int[] min = {0};
		
		int[] taskGroupIdx = {0};

		logger.debug("");
		
		List<TaskGroup> taskGroupAllList = new ArrayList<TaskGroup>();
		List<Integer> taskGroupIdAllList = new ArrayList<Integer>();
		
		Comparator<DaxNode> daxLevelCompare = Comparator.comparing(DaxNode::getLevelId);
		
		while(max[0] > 0) {
			
			int[] rowIdx = { 0 };

			// logger.debug("");
			// logger.debug("Max value: " + max[0]);
			boolean notFound = true;
			
			Iterator<Integer[]> rows = Stream.of(taskDistanceMatrix).iterator();
			
			while(rows.hasNext() && notFound) {
				
				Integer[] row = rows.next();
				
				if (Arrays.stream(row).filter(i -> i.intValue() == max[0]).count() > 0) {
					notFound = false;

					// logger.debug("");
					// logger.debug("Acquired TaskGroup Id: " + rowIdx[0]);
					
					List<Integer> taskGroupIdList = new ArrayList<Integer>();
					taskGroupIdList.add(rowIdx[0]);
					taskGroupIdAllList.add(rowIdx[0]);
					
					TaskGroup taskGroup = new TaskGroup();
					taskGroup.setTaskGroupId(taskGroupIdx[0]);
					taskGroup.setTaskGroupLevel(getTaskGroupByTaskGroupId(rowIdx[0]).getTaskGroupLevel());
					
					List<DaxNode> nodeList = new ArrayList<DaxNode>();
					
					nodeList.addAll(getTaskGroupByTaskGroupId(rowIdx[0]).getNodes());					

					min[0] = Arrays.stream(row).filter(i -> i.intValue() > 0)
							.min(Comparator.comparing(Integer::intValue)).get();

					for (int[] j = { min[0] }; j[0] <= max[0]; j[0]++) {

						int colMatrix = IntStream.range(0, row.length).filter(i -> j[0] == row[i]).findFirst()
								.orElse(-1);

						if (colMatrix > -1) {
							
							// logger.debug("Acquired TaskGroup Id: " + colMatrix);
							nodeList.addAll(getTaskGroupByTaskGroupId(colMatrix).getNodes());
							taskGroupIdList.add(colMatrix);
							taskGroupIdAllList.add(colMatrix);

							for (int[] w = { 0 }; w[0] < size; w[0]++) {
								taskDistanceMatrix[w[0]][colMatrix] = 0;
								taskDistanceMatrix[colMatrix][w[0]] = 0;
							}

							// JavaHelper.printIntegerMatrix(taskDistanceMatrix, size);

						}
					}

					for (int[] w = { 0 }; w[0] < size; w[0]++) {
						taskDistanceMatrix[rowIdx[0]][w[0]] = 0;
					}
					
					nodeList = nodeList.stream().sorted(daxLevelCompare).collect(Collectors.toList());
					
					taskGroup.addNodes(nodeList);
					
					taskGroup.setLowestNodeLevel(nodeList.get(0).getLevelId());
					taskGroup.setHighestNodeLevel(nodeList.get(nodeList.size()-1).getLevelId());
					
					taskGroupAllList.add(taskGroup);
					
					// logger.debug("");
					// JavaHelper.printIntegerMatrix(taskDistanceMatrix, size);
					// logger.debug(taskGroupIdx[0] + " -> " + taskGroupIdList);
					// logger.debug(taskGroupIdx[0] + " -> " + nodeList.stream().map(DaxNode::getId).collect(Collectors.toList()));
					// logger.debug(taskGroupIdx[0] + " -> " + nodeList.stream().map(DaxNode::getLevelId).collect(Collectors.toList()));

					taskGroupIdx[0]++;
				}				
				
				rowIdx[0]++;
			}
			
			max[0] = JavaHelper.getMaxValueIntegerMatrix(taskDistanceMatrix);
		}
		
		// logger.debug("");
		// logger.debug("Process the remaining tasks");
		
		for (int[] i = { 0 }; i[0] < size; i[0]++) {

			int found = IntStream.range(0, taskGroupIdAllList.size()).filter(j -> taskGroupIdAllList.get(j).intValue() == i[0]).findFirst().orElse(-1);
			if (found == -1) {
				
				taskGroupIdAllList.add(i[0]);
				
				TaskGroup taskGroup = new TaskGroup();
				taskGroup.setTaskGroupId(taskGroupIdx[0]);
				taskGroup.setTaskGroupLevel(getTaskGroupByTaskGroupId(i[0]).getTaskGroupLevel());

				List<DaxNode> nodeList = new ArrayList<DaxNode>();
				nodeList.addAll(getTaskGroupByTaskGroupId(i[0]).getNodes());
				nodeList = nodeList.stream().sorted(daxLevelCompare).collect(Collectors.toList());
				
				taskGroup.addNodes(nodeList);

				taskGroup.setLowestNodeLevel(nodeList.get(0).getLevelId());
				taskGroup.setHighestNodeLevel(nodeList.get(nodeList.size() - 1).getLevelId());
				
				taskGroupAllList.add(taskGroup);

/*				logger.debug("");
				logger.debug(taskGroupIdx[0] + " -> " + i[0]);
				logger.debug(taskGroupIdx[0] + " -> " + nodeList.stream().map(DaxNode::getId).collect(Collectors.toList()));
				logger.debug(taskGroupIdx[0] + " -> " + nodeList.stream().map(DaxNode::getLevelId).collect(Collectors.toList()));*/

				taskGroupIdx[0]++;
			}
		}

/*		logger.debug("");
		
		logger.debug(String.format("Input size: %d, Output size: %d, Output group size: %d", size, taskGroupIdAllList.size(), taskGroupAllList.size()));

		logger.debug("");*/
		
		return taskGroupAllList;
		
	}
	
	private Integer[][] createDistanceMatrix(List<List<TaskGroup>> p2pClusterLevel, int size) {
		
		logger.debug("Matrix Size: " + size);
		
		Integer[][] taskDistanceMatrix = JavaHelper.createZeroIntegerMatrix(size);
		
		// Create connected edge in matrix
		
		for (List<TaskGroup> groupLevel : p2pClusterLevel) {
			groupLevel.stream().forEach(taskGroup -> {
				
				DaxNode lastNode = taskGroup.getNodes().get(taskGroup.getNodes().size()-1);
				
				lastNode.getChilds().entrySet().forEach(entry -> {
				    int taskGroupIdOfChild = nodeTaskMapping.get(entry.getValue().getId());
				    
				    taskDistanceMatrix[taskGroup.getTaskGroupId()][taskGroupIdOfChild] = 1;
				}); 
				
			});
		}
		
		
		for (int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {				
				if(taskDistanceMatrix[i][j] > 0) {
					
					for(int k = 0; k < size; k++) {
						if(taskDistanceMatrix[j][k] > 0) {
							taskDistanceMatrix[i][k] = taskDistanceMatrix[i][j] + taskDistanceMatrix[j][k];
						}
					}
				}
			}
		}
		
		logger.debug("");
		// logger.debug("Print Distance Metrix");
		
		// JavaHelper.printIntegerMatrix(taskDistanceMatrix, size);
		
		return taskDistanceMatrix;
	}

	private List<List<TaskGroup>> processP2pClustering() {

		nodeTaskMapping = new HashMap<String, Integer>();
		taskLevelMapping = new HashMap<Integer, Integer>();
		
		List<List<TaskGroup>> p2pCluster = new ArrayList<List<TaskGroup>>();

		int[] index = {0};
		int[] level = {0};

		for (List<DaxNode> nodes : nodeLevel) {

			List<TaskGroup> taskGroupLevel = new ArrayList<TaskGroup>();

			nodes.stream().forEach(node -> {
				
				if(node.getClusterId() == DaxNode.NULL_CLUSTER) {
					
					node.setClusterId(DaxNode.TEMP_CLUSTER);

					TaskGroup group = new TaskGroup();
					group.setTaskGroupId(index[0]);
					group.addNode(node);
					
					nodeTaskMapping.put(node.getId(), index[0]);

					if (node.getDaxNodeComType() == DaxNodeComType.ONE_TO_ONE
							|| node.getDaxNodeComType() == DaxNodeComType.MANY_TO_ONE) {

						List<DaxNode> temp = new ArrayList<DaxNode>();

						travelChildNode(temp, node);

						if (!temp.isEmpty()) {

							if (node.getDaxNodeComType() == DaxNodeComType.ONE_TO_ONE
									|| (node.getDaxNodeComType() == DaxNodeComType.MANY_TO_ONE
											&& temp.get(temp.size() - 1).getDaxNodeType() == DaxNodeType.END)) {

								temp.stream().forEach(n -> {
									n.setClusterId(DaxNode.TEMP_CLUSTER);

									nodeTaskMapping.put(n.getId(), index[0]);
								});

								Comparator<DaxNode> comparator = Comparator.comparing(DaxNode::getLevelId);
								temp = temp.stream().sorted(comparator).collect(Collectors.toList());

								group.addNodes(temp);
							}
						}
					}
					
					group.setTaskGroupLevel(level[0]);
					taskGroupSotedByTopogicalOrder.add(group);
					taskGroupLevel.add(group);
					taskLevelMapping.put(group.getTaskGroupId(), level[0]);

					logger.debug(index[0] + " -> "
							+ group.getNodes().stream().map(e -> e.getId()).collect(Collectors.toList()).toString());

					index[0]++;
				}

			});			

			if (!taskGroupLevel.isEmpty()) {
				p2pCluster.add(taskGroupLevel);				
			}			

			level[0]++;
		}

		return p2pCluster;
	}

	private void travelChildNode(List<DaxNode> list, DaxNode parent) {

		if (!JavaHelper.isNull(parent.getChilds()) && parent.getChilds().size() == 1) {

			DaxNode child = parent.getChilds().entrySet().iterator().next().getValue();

			if (child.getClusterId().equalsIgnoreCase(DaxNode.NULL_CLUSTER)
					&& child.getDaxNodeComType() == DaxNodeComType.ONE_TO_ONE) {
				list.add(child);

				travelChildNode(list, child);
			}

		}
	}
}
