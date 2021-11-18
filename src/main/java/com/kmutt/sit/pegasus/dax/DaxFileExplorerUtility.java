package com.kmutt.sit.pegasus.dax;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.kmutt.sit.pegasus.xsd.element.Adag;

public class DaxFileExplorerUtility {	

	private static Logger logger = Logger.getLogger(DaxFileExplorerUtility.class);
	
	public static DaxFileExplorer getDaxFileExplorer(String fullPathFileName) {
		
		logger.debug("");
		logger.info("Processing File: " + fullPathFileName);
		
		DaxFileExplorer explorer = null;
		
		File file = new File(fullPathFileName);
		
		if(file.exists()) {
			
			logger.debug("");
			logger.info("File exists...");
		
			try {			
				JAXBContext jaxbContext = JAXBContext.newInstance(Adag.class);

				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Adag aDag = (Adag) jaxbUnmarshaller.unmarshal(file);
				
				explorer = new DaxFileExplorer(file, aDag);
				explorer.exploreAdagNodes();

				logger.debug("");
				logger.info("File exporler finished...");
				
			} catch (JAXBException e) {
				logger.error(e, e);
			}
		} else {
			logger.debug("");
			logger.info("File not found!!");
			
		}
				
		return explorer;
		
	}

}
