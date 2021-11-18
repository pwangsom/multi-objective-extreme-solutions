package com.dag.da.matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

public class BooleanMatrix {
	
	private Logger logger = Logger.getLogger(BooleanMatrix.class);
	
	final private int MAT_SIZE = 7;
	
	private Boolean[][] mat = new Boolean[][] {
		{false, false, false, false, false, false, false},
		{false, false, false, false, false, false, false},
		{false, true, false, false, false, false, false},
		{true, false, true, false, false, false, false},
		{true, true, true, true, false, true, false},
		{false, true, false, false, false, false, false},
		{true, false, false, false, true, true, false}		
	};
	

	private List<List<Integer>> nodeLevel;
	
	public BooleanMatrix() {
		nodeLevel = new ArrayList<List<Integer>>();
	}
	
	public void determineMatrixLevel() {

		logger.info("determineMatrixLevel()");

		ArrayList<Integer> doneList = new ArrayList<Integer>();

		int levelId = 0;

		boolean isNotDone = true;

		while (isNotDone) {

			ArrayList<Integer> level = new ArrayList<Integer>();

			for (int i = 0; i < this.MAT_SIZE; i++) {
				
				if(!doneList.contains(i)) {
					if (!Arrays.asList(this.mat[i]).contains(true)) {
						doneList.add(i);
						level.add(i);
					}
				}
			}

			if (!level.isEmpty()) {
				levelId++;
				nodeLevel.add(level);

				String levelStr = "level: " + levelId + ": " + Arrays.asList(level).toString();
				
				for (Integer m : level) {
					for (int j = 0; j < this.MAT_SIZE; j++) {
						this.mat[j][m] = false;
					}					
				}

				logger.info(levelStr);				

				printBooleanMatrix();
			} else {
				isNotDone = false;
			}

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
	
/*		
	int[] getColumn(int[][] matrix, int column) {
	    return IntStream.range(0, matrix.length)
	        .map(i -> matrix[i][column]).toArray();
	}
	
	And if you want to cope with rows that are too short:

	int[] getColumn(int[][] matrix, int column, int defaultVal) {
	    return IntStream.range(0, matrix.length)
	        .map(i -> matrix[i].length < column ? defaultVal : matrix[i][column])
	        .toArray();
	}
*/
	
	public void printBooleanMatrix() {
		for(int i = 0; i < this.MAT_SIZE; i++) {
			
			String row = "";
			
			for(int j = 0; j < this.MAT_SIZE; j++) {
				row += this.mat[i][j] + ",\t";
			}
			
			logger.info(row);
		}
	}

}
