package com.dag.da.test;

import java.util.stream.IntStream;

import org.apache.log4j.Logger;

public class TestBooleanMatrix {	

	private static Logger logger = Logger.getLogger(TestBooleanMatrix.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub

/*		BooleanMatrix bMat = new BooleanMatrix();
		logger.info("Original Matrix");
		bMat.printBooleanMatrix();
		bMat.determineMatrixLevel();
		bMat.printNodeLevel();

		logger.debug("");
		logger.info("==================");
		logger.debug("");*/
		
/*		IntegerMatrix iMat = new IntegerMatrix();
		logger.info("Original Matrix");
		iMat.printBooleanMatrix();
		iMat.determineMatrixLevel();
		iMat.printNodeLevel();*/
		
/*		LevelClusterIntegerMatrix mat = new LevelClusterIntegerMatrix();
		mat.processLevel();*/
		
/*		List<List<String>> listLevel = new ArrayList<List<String>>();
		
		List<String> listStr = new ArrayList<String>();
		
		listStr.add("Peerasak");
		listStr.add("Tong");
		listStr.add("Khunnphat");
		listStr.add("Khunn");
		listStr.add("Pholaphat");
		listStr.add("Khem");
		listStr.add("Noi");
		
		IntStream.range(0,4).forEach(i -> {
			listLevel.add(new ArrayList<String>());
		});
		
		int i = 0;
		
		logger.debug(listLevel.size() + " -> " + listLevel);
		
		listLevel.get(i).add(listStr.get(0));
		
		logger.debug(listLevel.size() + " -> " + listLevel);
		
		listLevel.get(i).add(listStr.get(1));

		logger.debug(listLevel.size() + " -> " + listLevel);	
		
		i = 2;
		
		listLevel.get(i).add(listStr.get(2));

		logger.debug(listLevel.size() + " -> " + listLevel);
		
		listLevel.get(i).add(listStr.get(3));

		logger.debug(listLevel.size() + " -> " + listLevel);
		
		i = 3;
		
		listLevel.get(i).add(listStr.get(4));

		logger.debug(listLevel.size() + " -> " + listLevel);
		
		listLevel.get(i).add(listStr.get(5));

		logger.debug(listLevel.size() + " -> " + listLevel);
		
		i = 1;
		
		listLevel.get(i).add(listStr.get(6));

		logger.debug(listLevel.size() + " -> " + listLevel);
		
		listLevel.stream().forEach(level -> {
			logger.debug(level);
		});*/
		
		
/*		Integer[][] twoFive = JavaHelper.createIntegerMatrix(2, 5, 0);
		
		JavaHelper.printIntegerMatrix(twoFive);*/
		
/*		int totalSize = 36;
		int[] round = {1};
		
		if(totalSize > 10) {
			if(totalSize > 100) {
				round[0] = countDigit(totalSize/10);
			} else {
				round[0] = countDigit(totalSize);
			}
			round[0] = (int) Math.pow(10, round[0]-1);
		}
		
		logger.info("Round: " + round[0]);
		
		IntStream.range(0, totalSize).forEach(i -> {
			if((i % round[0]) == 0) {
				logger.info("Processing: " + i + " of " + totalSize);
			}
		});
		

		logger.info("Processing: " + totalSize + " of " + totalSize);
		logger.info("");*/
		
		
		logger.info(Math.pow(1024, 1));
	}
	
//	private static int countDigit(int n) {
//		return (int) Math.ceil(Math.log10(n));
//	}

}
