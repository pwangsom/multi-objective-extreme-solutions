package com.dag.da.matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.kmutt.sit.utils.JavaHelper;

public class LevelClusterIntegerMatrix {

	private Logger logger = Logger.getLogger(IntegerMatrix.class);
	
	final private int SIZE = 15;
	
	private Integer[][] taskDistanceMatrix = new Integer[][] {
		 {0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 2, 0, 3},
		 {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 2, 3},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
 };
	

/*	private Integer[][] taskDistanceMatrix = new Integer[][] {
			{ 0, 0, 0, 0, 1, 2, 2, 2, 2, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 1, 2, 2, 2, 2, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 1, 2, 2, 2, 2, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 1, 2, 2, 2, 2, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 2, 3, 4 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 2, 3, 4 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 2, 3, 4 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 2, 3, 4 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 3 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
			};*/
			
	
	public void processLevel() {
		
		JavaHelper.printIntegerMatrix(taskDistanceMatrix, SIZE);

		int[] max = { 0 };
		max[0] = JavaHelper.getMaxValueIntegerMatrix(taskDistanceMatrix);
		int[] min = { 0 };

		logger.debug("");
		
		while(max[0] > 0) {
			
			int[] rowIdx = { 0 };

			logger.debug("Max value: " + max[0]);
			boolean notFound = true;
			
			Iterator<Integer[]> rows = Stream.of(taskDistanceMatrix).iterator();
			
			while(rows.hasNext() && notFound) {
				
				Integer[] row = rows.next();
				
				if (Arrays.stream(row).filter(i -> i.intValue() == max[0]).count() > 0) {
					notFound = false;

					List<Integer> result = new ArrayList<Integer>();
					result.add(rowIdx[0]);

					logger.debug("Row no: " + rowIdx[0]);
					logger.debug("row -> " + Arrays.asList(row));

					min[0] = Arrays.stream(row).filter(i -> i.intValue() > 0)
							.min(Comparator.comparing(Integer::intValue)).get();

					logger.debug("Min value: " + min[0]);

					for (int[] j = { min[0] }; j[0] <= max[0]; j[0]++) {

						int colMatrix = IntStream.range(0, row.length).filter(i -> j[0] == row[i]).findFirst()
								.orElse(-1);

						if (colMatrix > -1) {

							result.add(colMatrix);

							logger.debug("Col no: " + colMatrix);

							for (int[] w = { 0 }; w[0] < SIZE; w[0]++) {
								taskDistanceMatrix[w[0]][colMatrix] = 0;
								taskDistanceMatrix[colMatrix][w[0]] = 0;
							}

							JavaHelper.printIntegerMatrix(taskDistanceMatrix, SIZE);

						}
					}

					for (int[] w = { 0 }; w[0] < SIZE; w[0]++) {
						taskDistanceMatrix[rowIdx[0]][w[0]] = 0;
					}

					logger.debug("Row no: " + rowIdx[0]);
					logger.debug("");
					JavaHelper.printIntegerMatrix(taskDistanceMatrix, SIZE);
					logger.debug("");
					logger.debug("Result -> " + result);

				}				
				
				rowIdx[0]++;
			}
			
			max[0] = JavaHelper.getMaxValueIntegerMatrix(taskDistanceMatrix);

			logger.debug("Max value: " + max[0]);
		}
		
	}

	public void processLevelClustering() {

		JavaHelper.printIntegerMatrix(taskDistanceMatrix, 22);

		int[] max = { 0 };
		max[0] = JavaHelper.getMaxValueIntegerMatrix(taskDistanceMatrix);
		int[] min = { 0 };

		int[] rowIdx = { 0 };

		logger.debug("");

		while (max[0] > 0) {

			logger.debug("Max value: " + max[0]);

			Stream.of(taskDistanceMatrix).forEach(row -> {

				if (Arrays.stream(row).filter(i -> i.intValue() == max[0]).count() > 0) {

					List<Integer> result = new ArrayList<Integer>();
					result.add(rowIdx[0]);

					logger.debug("Row no: " + rowIdx[0]);
					logger.debug("row -> " + Arrays.asList(row));

					min[0] = Arrays.stream(row).filter(i -> i.intValue() > 0)
							.min(Comparator.comparing(Integer::intValue)).get();

					logger.debug("Min value: " + min[0]);

					for (int[] j = { min[0] }; j[0] <= max[0]; j[0]++) {

						int colMatrix = IntStream.range(0, row.length).filter(i -> j[0] == row[i]).findFirst()
								.orElse(-1);

						if (colMatrix > -1) {

							result.add(colMatrix);

							logger.debug("Col no: " + colMatrix);

							for (int[] w = { 0 }; w[0] < 22; w[0]++) {
								taskDistanceMatrix[w[0]][colMatrix] = 0;
							}

							JavaHelper.printIntegerMatrix(taskDistanceMatrix, 22);

						}
					}

					for (int[] w = { 0 }; w[0] < 22; w[0]++) {
						taskDistanceMatrix[rowIdx[0]][w[0]] = 0;
					}

					logger.debug("Row no: " + rowIdx[0]);
					logger.debug("");
					JavaHelper.printIntegerMatrix(taskDistanceMatrix, 22);
					logger.debug("");
					logger.debug("Result -> " + result);

				}
				;

				max[0] = JavaHelper.getMaxValueIntegerMatrix(taskDistanceMatrix);

				logger.debug("Max value: " + max[0]);

				rowIdx[0]++;

			});

		}

	}

}
