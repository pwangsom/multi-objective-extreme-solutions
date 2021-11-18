package com.dag.da.test;

import org.apache.log4j.Logger;

import com.kmutt.sit.mop.problem.knapsack.KnapsackDataset;

public class KnapsackDatasetTest {	

	private static Logger logger = Logger.getLogger(KnapsackDatasetTest.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String fileNamePath = "D:\\Users\\Peerasak\\git\\daDAG\\knapsack-problem\\experiment0\\input\\01mokp_250_01.dat";
		
		KnapsackDataset mokpDataset = new KnapsackDataset(fileNamePath, 250, "01");
		mokpDataset.loadDatasetFile();
		logger.info(mokpDataset.toString());

	}

}
