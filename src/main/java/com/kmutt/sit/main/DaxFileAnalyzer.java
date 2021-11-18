package com.kmutt.sit.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.kmutt.sit.pegasus.dax.DaxFileExplorer;
import com.kmutt.sit.pegasus.dax.DaxFileExplorerUtility;
import com.kmutt.sit.utils.JavaHelper;

public class DaxFileAnalyzer {

	private static Logger logger = Logger.getLogger(DaxFileAnalyzer.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		if (args.length == 1) {

			DaxFileAnalyzer analyzer = new DaxFileAnalyzer();
			analyzer.readFiles(args[0].trim());

		} else {
			System.out.println("Please specify the target directory!");
		}

	}

	private void readFiles(String folderPath) {
		
		List<String> reportList = new ArrayList<String>();
				
		try {
			Files.list(Paths.get(folderPath)).forEach(file -> {
				if (file.getFileName().toString().equalsIgnoreCase("epigenomics_50.xml") || true) {
					
					String fullPathFileName = JavaHelper.appendPathName(file.getParent().toString(), file.getFileName().toString());
					
					logger.info(fullPathFileName);
					
					DaxFileExplorer explorer = DaxFileExplorerUtility.getDaxFileExplorer(fullPathFileName);
					explorer.showReport();
					
					reportList.add(explorer.toString());
				}
			});
			
			reportList.forEach(report -> {
				logger.info(report);
			});
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
	}

}
