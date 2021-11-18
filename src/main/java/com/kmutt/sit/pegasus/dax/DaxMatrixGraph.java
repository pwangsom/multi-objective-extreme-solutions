package com.kmutt.sit.pegasus.dax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.log4j.Logger;

import com.kmutt.sit.utils.JavaHelper;

public class DaxMatrixGraph {

	private Logger logger = Logger.getLogger(DaxMatrixGraph.class);

	final private int MATRIX_SIZE;
	private Map<String, DaxNode> daxNodes = new TreeMap<>();
	private List<List<Integer>> nodeLevel;
	private Integer[][] daxMatrix;
	
	public DaxMatrixGraph(Map<String, DaxNode> daxNodes) {
		this.daxNodes = daxNodes;
		this.MATRIX_SIZE = this.daxNodes.size();
		this.daxMatrix = new Integer[this.MATRIX_SIZE][this.MATRIX_SIZE];
		setZeroDaxMatrixGraph();
		this.nodeLevel = new ArrayList<List<Integer>>();
	}
	
	public List<List<Integer>> getNodeLevel(){
		return this.nodeLevel;
	}
	
	public void determineNodeLevel() {
		logger.debug("");
		logger.info("determineNodeLevel()");
		
		Integer[][] clone = this.daxMatrix;

		ArrayList<Integer> doneList = new ArrayList<Integer>();

		int levelId = 0;

		boolean isNotDone = true;

		while (isNotDone) {

			ArrayList<Integer> level = new ArrayList<Integer>();

			for (int i = 0; i < this.MATRIX_SIZE; i++) {
				
				if(!doneList.contains(i)) {
					
					// Scan for the row in matrix that has only value = 0,
					// which means the node in that row does not have parent,
					// or its parent already took to doneList
					
					if (!Arrays.asList(clone[i]).contains(1)) {
						doneList.add(i);
						level.add(i);
					}
				}
			}

			if (!level.isEmpty()) {
				levelId++;
				nodeLevel.add(level);

				String levelStr = "level: " + levelId + ": " + Arrays.asList(level).toString();

				logger.info(levelStr);	
				
				for (Integer m : level) {
					for (int j = 0; j < this.MATRIX_SIZE; j++) {
						clone[j][m] = 0;
					}					
				}			

			} else {
				isNotDone = false;
			}
		}
	}
	
	public void printDaxMatrixGraph() {
		for(int i = 0; i < this.MATRIX_SIZE; i++) {
			logger.info(Arrays.asList(this.daxMatrix[i]).toString());
		}
	}
	
	public void printNodeLevel() {
		
		int i = 0;
		
		for(List<Integer> level : nodeLevel) {
			
			i++;
			
			String levelStr = "Level " + i + ": " + Arrays.asList(level).toString();

			logger.info(levelStr);	
			
		}
	}	
	
	
	private void setZeroDaxMatrixGraph() {
		this.daxMatrix = 
			    IntStream.range(0, this.MATRIX_SIZE)
			             .mapToObj(x -> IntStream.range(0, this.MATRIX_SIZE)
			                                     .mapToObj(y -> Integer.valueOf(0))
			                                     .toArray(Integer[]::new))
			             .toArray(Integer[][]::new);
	}
	
	public void intialDaxMatrixGraph() {
		
		logger.info("intialDaxMatrixGraph()");
		logger.debug("This matrix shows that if cell = 1, it means node in row is a child of node in column");
		logger.debug("For example, in two nodes workflow, there are node1 in the first row and column and node2 in the second ones");
		logger.debug("         [node1, node2]");
		logger.debug("node1 -> [    -,     -]");
		logger.debug("node2 -> [    1,     -]");
		logger.debug("The above example shows that node2, node in row, is child of node1, node in column");
		logger.debug("");
		
		
		Set<Entry<String, DaxNode>> s = daxNodes.entrySet();
		Iterator<Entry<String, DaxNode>> it = s.iterator();
						
		while (it.hasNext()) {
			Entry<String, DaxNode> entry = it.next();
			DaxNode node = entry.getValue();
			
			if(!JavaHelper.isNull(node.getChilds())) {
				
				Set<Entry<String, DaxNode>> c = node.getChilds().entrySet();
				Iterator<Entry<String, DaxNode>> childs = c.iterator();
								
				while (childs.hasNext()) {
					Entry<String, DaxNode> child = childs.next();					
					this.daxMatrix[child.getValue().getMatrixId()][node.getMatrixId()] = 1;
					
				}
				
			}
			
		}
		
	}
	
	public void assignDaxMatrixId() {
		
		logger.info("assignDaxMatrixId()");
		
		Set<Entry<String, DaxNode>> s = daxNodes.entrySet();
		Iterator<Entry<String, DaxNode>> it = s.iterator();
		
		int i = 0;
		
		while (it.hasNext()) {
			Entry<String, DaxNode> entry = it.next();
			entry.getValue().setMatrixId(i);
			
			i++;
		}
	}
	
	public void printDaxMatrix() {
		for(int i = 0; i < this.MATRIX_SIZE; i++) {
			// logger.info(Arrays.asList(this.daxMatrix[i]).toString());
			logger.info(Arrays.asList(this.daxMatrix[i]).stream()
					.map(n -> n < 1 ? "-" : String.valueOf(n))
					.collect(Collectors.toList()).toString());
		}
	}

}
