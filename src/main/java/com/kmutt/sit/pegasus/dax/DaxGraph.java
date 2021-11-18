package com.kmutt.sit.pegasus.dax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.kmutt.sit.mop.parameter.Global;
import com.kmutt.sit.utils.JavaHelper;
import com.kmutt.sit.workflow.ClusterType;
import com.kmutt.sit.workflow.GeneralClusterWorkflow;
import com.kmutt.sit.workflow.WorkflowBuilder;

public class DaxGraph {

	private Logger logger = Logger.getLogger(DaxGraph.class);
	
	private ClusterType clusterType = ClusterType.NONE;	
	
	private Map<Integer, String> matrixIdMapping = new HashMap<>();
	
	private Map<String, List<DaxNode>> clusterMapping = new TreeMap<>();
	
	private List<List<DaxNode>> nodeLevel;
	private List<DaxNode> topologyListNormal;
	
	private DaxMatrixGraph daxMatrix;
	private DaxFileExplorer explorer;
	
	private GeneralClusterWorkflow workflow;
	
	public DaxGraph() {
		
	}
	
	public DaxGraph(DaxFileExplorer explorer) {
		this.explorer = explorer;
		assignMatrixId();
	}
	
	public DaxMatrixGraph getDaxMatrixGraph() {
		return this.daxMatrix;
	}
	
	public List<DaxNode> getClusteredTaskByClusterId(String clusterId){
		return this.clusterMapping.get(clusterId);
	}
	
	public void processDaxMatrixGraph() {
		this.daxMatrix = new DaxMatrixGraph(this.explorer.getDaxNodes());
		this.daxMatrix.intialDaxMatrixGraph();
		this.daxMatrix.printDaxMatrix();
		determineNodeLevel();
	}
	
	public void determineNodeLevel() {
		this.nodeLevel = new ArrayList<List<DaxNode>>();
		this.topologyListNormal = new ArrayList<DaxNode>();
		this.daxMatrix.determineNodeLevel();
		setNodeLevelList();
	}
	
	public void processClusterWorkflow() {
		this.workflow = WorkflowBuilder.getWorkflowBuilder(clusterType)
						.setNodeLevelList(nodeLevel)
						.setDaxNodes(this.explorer.getDaxNodes())
						.convertWorkflowByClusterType();		
		
		this.clusterMapping = this.getDaxCluster();
		
	}
	
	public GeneralClusterWorkflow getDagWorkflow(){			
		return this.workflow;
	}
	
	public void printWorkflowByGroupLevel() {
		this.workflow.printWorkflowByGroupLevel();
	}
	
	public void printDaxMatrixGraph() {
		this.daxMatrix.printDaxMatrix();
	}
	
	public void printNodeLevel() {
		this.daxMatrix.printNodeLevel();
	}
	
	private void setNodeLevelList() {
		
		int levelId = 1;
		
		for(List<Integer> iList : this.daxMatrix.getNodeLevel()) {
			
			ArrayList<DaxNode> level = new ArrayList<DaxNode>();
			
			for(Integer i : iList) {
				DaxNode node = this.explorer.getDaxNodes().get(matrixIdMapping.get(i));
				node.setLevelId(levelId);
				node.setClusterId(DaxNode.NULL_CLUSTER);
				level.add(node);
				this.topologyListNormal.add(node);
			}			
			
			this.nodeLevel.add(level);
			levelId++;
		}
	}
	
	public void printNodeLevelByMatrixId() {
		logger.debug("printNodeLevelByMatrixId(): MatrixId");
		for(List<DaxNode> nodeList : this.nodeLevel) {			
			List<Integer> iList = nodeList.stream().map(n -> n.getMatrixId()).collect(Collectors.toList());
			logger.info(Arrays.asList(iList).toString());
		}
	}
	
	public void printNodeLevelById() {
		logger.debug("printNodeLevelById(): Id");
		for(List<DaxNode> nodeList : this.nodeLevel) {				
			List<String> sList = nodeList.stream().map(n -> n.getId()).collect(Collectors.toList());
			logger.info(Arrays.asList(sList).toString());
		}
	}
	
	public void printNodeLevelByName() {
		logger.debug("printNodeLevelByName(): Name");
		for(List<DaxNode> nodeList : this.nodeLevel) {				
			List<String> sList = nodeList.stream().map(n -> n.getName()).collect(Collectors.toList());
			logger.info(Arrays.asList(sList).toString());
		}
	}
	
	public void printNodeLevelByClusterId() {
		logger.debug("printNodeLevelByClusterId(): Cluster Type: " + this.clusterType);
		for(List<DaxNode> nodeList : this.nodeLevel) {				
			List<String> sList = nodeList.stream().map(n -> n.getClusterId()).collect(Collectors.toList());
			logger.info(Arrays.asList(sList).toString());
		}
	}
	
	public void printOrginalDagNodes() {
		logger.info("Display Nodes with Its Details: ");
		
		Set<Entry<String, DaxNode>> s = this.explorer.getDaxNodes().entrySet();
		Iterator<Entry<String, DaxNode>> it = s.iterator();
				
		while (it.hasNext()) {
			Entry<String, DaxNode> entry = it.next();
			String key = (String) entry.getKey();
			DaxNode node = (DaxNode) entry.getValue();
			
			logger.info("No. " + node.getMatrixId()  + ": " + key + " [" + node.getDaxNodeType() + ", " + node.getDaxNodeComType() + "]");
			
			if(!node.getParents().isEmpty()) {
				String parentList = "Parents (" + node.getParents().size() + "): ";				
				parentList += JavaHelper.getStringOfNodeList(node.getParents());
				logger.info(parentList);
			}			
			
			if(!node.getChilds().isEmpty()) {
				String childList = "Childs (" + node.getChilds().size() + "): ";				
				childList += JavaHelper.getStringOfNodeList(node.getChilds());
				logger.info(childList);
			}
			
		}
	}
	
	public void printOriginalDagDetails() {
		logger.info("This graph consists of : ");
		
		logger.info(this.explorer.getDaxNodes().size() + " nodes [Head: " + this.explorer.getHeadNodeNumber() 
		+ ", Inter: " + this.explorer.getInterNodeNumber() + ", End: " + this.explorer.getEndNodeNumber() + "]");
		
		logger.info("[One-to-one: " + this.explorer.getOneToOneNodeNumber()
		+ ", One-to-many: " + this.explorer.getOneToManyNodeNumber()
		+ ", Many-to-one: " + this.explorer.getManyToOneNodeNumber()
		+ ", Many-to-many: " + this.explorer.getManyToManyNodeNumber()
		+ "]");
		
		Global.MODEL_RATIO = String.format("%d,%d,%d,%d", this.explorer.getOneToOneNodeNumber(), this.explorer.getOneToManyNodeNumber(),
				this.explorer.getManyToOneNodeNumber(), this.explorer.getManyToManyNodeNumber());
		
	}
	
	public void addNode(Map<String, DaxNode> nodeList, DaxNode node) {
		if (!nodeList.containsKey(node.getId())) {
			nodeList.put(node.getId(), node);
			logger.debug("New node added: " + node.getId());
		}
	}
	
	public DaxNode getAdagNode(String nodeId){
		if (this.explorer.getDaxNodes().containsKey(nodeId)) {
			return this.explorer.getDaxNodes().get(nodeId);
		} else {
			return null;
		}
	}	

	public List<List<DaxNode>> getNodeLevel() {
		return nodeLevel;
	}

	public Map<String, DaxNode> getDaxNodes() {
		return this.explorer.getDaxNodes();
	}

	public String getDaxNameSpace() {
		return this.explorer.getDaxNodes().entrySet().iterator().next().getValue().getNamespace();
	}

	public ClusterType getDaxClusterType() {
		return clusterType;
	}

	public void setDaxClusterType(ClusterType clustertype) {
		this.clusterType = clustertype;
	}

	public Map<String, List<DaxNode>> getDaxCluster() {
		return clusterMapping;
	}

	public void setDaxCluster(Map<String, List<DaxNode>> daxCluster) {
		this.clusterMapping = daxCluster;
	}

	public List<DaxNode> getTopologyListNormal() {
		return topologyListNormal;
	}
	
	private void assignMatrixId() {
		
		Set<Entry<String, DaxNode>> s = this.explorer.getDaxNodes().entrySet();
		Iterator<Entry<String, DaxNode>> it = s.iterator();
		
		int matrixId = 0;
		
		while (it.hasNext()) {
			Entry<String, DaxNode> entry = it.next();
			DaxNode node = (DaxNode) entry.getValue();
			
			node.setMatrixId(matrixId);			
			matrixIdMapping.put(matrixId, node.getId());
			
			matrixId++;
		}
	}
	
}
