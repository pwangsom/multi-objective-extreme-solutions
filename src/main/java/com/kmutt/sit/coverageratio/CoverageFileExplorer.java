package com.kmutt.sit.coverageratio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.kmutt.sit.utils.JavaHelper;

public class CoverageFileExplorer {
	
	private static Logger logger = Logger.getLogger(CoverageFileExplorer.class);

	private static Integer threeObjectivesId = 0;
	
	public CoverageFileExplorer() {
		
	}
	
	public static List<ThreeObjectives> getThreeObjectivesList(Path file, String objectiveName) {
		
		List<ThreeObjectives> list = new ArrayList<ThreeObjectives>();
		
		//logger.debug("Exploring: " + objectiveName);
		
		try (Stream<String> stream = Files.lines(file)) {

			stream.forEach(line -> {
				if(!JavaHelper.isNull(line)) {
					String[] fields = line.split(",");
					
					ThreeObjectives solution = new ThreeObjectives();
					solution.setId(threeObjectivesId);
					solution.setName(objectiveName);
					solution.setFirst(Double.valueOf(fields[0]));
					solution.setSecond(Double.valueOf(fields[1]));
					solution.setThird(Double.valueOf(fields[2]));
					
					list.add(solution);
					
					/*logger.debug("FileName: " + objectiveName + ", ID: " + solution.getId() + ", [" + solution.getFirst() 
					+ ", " + solution.getSecond() + ", " + solution.getThird() + "]");*/
					
					threeObjectivesId++;
				}
			});

		} catch (IOException e) {
			logger.error(e);
		}
		
		return list;
	}

}
