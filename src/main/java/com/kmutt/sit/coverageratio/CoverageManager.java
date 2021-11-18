package com.kmutt.sit.coverageratio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public class CoverageManager {

	private Logger logger = Logger.getLogger(CoverageManager.class);	
	
	private Map<String, CoverageObjectiveMatching> matchingMap = new TreeMap<String, CoverageObjectiveMatching>();
	
	public CoverageManager() {
	}

	public void accessFiles(String path) {
		try {
			Files.list(Paths.get(path)).forEach(file -> {
				
				if(Files.isRegularFile(file)){
					processFile(file);		
				}		
				
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
	}
	
	public void assessCoverageIndex() {
		matchingMap.entrySet().stream().map(map -> map.getValue())
		.forEach(match -> {
			if(match.isMatched()) {
				match.measureCoverageIndex();
			}
		});
	}
	
	public void printCoverageIndex() {
		logger.debug("");
		logger.info("++++ Coverage Index +++++");
		
		matchingMap.entrySet().stream().map(map -> map.getValue())
		.forEach(match -> {
			System.out.println(match.toString());
		});

	}
	
	private void processFile(Path file) {
		
		String fileName = file.getFileName().toString();
		logger.info(fileName);
		
		String[] splitFileName = fileName.split("\\.");
		
		String workflowName = splitFileName[0];
		
		CoverageObjectiveMatching match;
		
		if(matchingMap.containsKey(workflowName)) {
			match = matchingMap.get(workflowName);
		} else {
			match = new CoverageObjectiveMatching();
			match.setMatchingName(workflowName);
		}
		
		List<ThreeObjectives> solutionList = CoverageFileExplorer.getThreeObjectivesList(file, fileName);
		
		if(fileName.toUpperCase().contains("NONE")) {
			match.setFirstPareto(solutionList);
		} else {
			match.setSecondPareto(solutionList);
		}
		
		if(!matchingMap.containsKey(workflowName)) {
			matchingMap.put(workflowName, match);
		}
	}
}
