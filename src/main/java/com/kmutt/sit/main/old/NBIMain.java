package com.kmutt.sit.main.old;

import java.util.List;

import org.apache.commons.math3.util.Precision;
import org.apache.log4j.Logger;

import com.kmutt.sit.utils.NormalBoundaryIntersectionGenerator;

public class NBIMain {
	
	private static Logger logger = Logger.getLogger(NBIMain.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		NormalBoundaryIntersectionGenerator generator = new NormalBoundaryIntersectionGenerator(3, 12, 0);
		
		List<double[]> weights = generator.generate();
		
		for(int i = 0; i < weights.size(); i++) {
			System.out.println(Precision.round(weights.get(i)[0], 6) + " "
							+ Precision.round(weights.get(i)[1], 6) + " "
							+ Precision.round(weights.get(i)[2], 6)
							);
		}
	}

}
