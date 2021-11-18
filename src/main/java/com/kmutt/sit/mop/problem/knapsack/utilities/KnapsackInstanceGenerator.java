package com.kmutt.sit.mop.problem.knapsack.utilities;

import java.io.BufferedWriter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.uma.jmetal.util.fileoutput.FileOutputContext;

import com.kmutt.sit.mop.output.DaDagFileOutputContext;
import com.kmutt.sit.utils.JavaHelper;

public class KnapsackInstanceGenerator {	

	private static Logger logger = Logger.getLogger(KnapsackInstanceGenerator.class);
	
	private static String filePath = "D:\\Users\\Peerasak\\git\\daDAG\\knapsack-problem\\experiment0\\input";
	private static int noOfObjective = 3;
	private static int minValue = 10;
	private static int maxValue = 100;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int size = 500;
		int noOfInstance = 20;
		
		for(int i = 1; i <= noOfInstance; i++) {
			Integer[][] dataset = generateThreeObjectiveDataset(size);
			writeKnapsackDataset(i, size, dataset);
		}

	}
	
	private static Integer[][] generateThreeObjectiveDataset(int size){
		
		Integer[][] dataset = new Integer[size][noOfObjective + 1];
		
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < noOfObjective + 1; j++) {
				dataset[i][j] = JavaHelper.getRandomNumberInRange(minValue, maxValue);
			}
		}
		
		for(int i = 0; i < size; i++) {
			logger.info(dataset[i][0] + "," + dataset[i][1] + "," + dataset[i][2] + "," + dataset[i][3]);
		}
		
		return dataset;
	}
	
	private static void writeKnapsackDataset(int instanceId, int size, Integer[][] dataset) {
		
		String fileName = JavaHelper.appendPathName(filePath, String.format("%02dmokp2_", instanceId) + size + "_" + String.format("%02d", instanceId) + ".dat");
		
		FileOutputContext context = new DaDagFileOutputContext(fileName, ",");
		BufferedWriter bufferedWriter = context.getFileWriter();
		
		try {			
			
			for(int i = 0; i < size; i++) {
				for(int j = 0; j < noOfObjective + 1; j++) {
					bufferedWriter.write(dataset[i][j] + context.getSeparator());
				}
				bufferedWriter.newLine();
			}

			bufferedWriter.close();
			
		} catch (IOException e) {
			logger.error(e, e);
		}
	}

}
