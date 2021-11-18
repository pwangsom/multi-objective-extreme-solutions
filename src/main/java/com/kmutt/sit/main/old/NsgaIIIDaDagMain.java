package com.kmutt.sit.main.old;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.kmutt.sit.main.old.nsgaiii.runner.DaDagNsgaIIIRunner;
import com.kmutt.sit.pegasus.dax.DaxFileExplorer;
import com.kmutt.sit.pegasus.dax.DaxGraph;
import com.kmutt.sit.pegasus.xsd.element.Adag;
import com.kmutt.sit.workflow.ClusterType;
import com.kmutt.sit.workflow.GeneralClusterWorkflow;

public class NsgaIIIDaDagMain {
	
	private static Logger logger = Logger.getLogger(NsgaIIIDaDagMain.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		File file = new File("src/main/resources/input/pegasus/dataset1/Epigenomics_24.xml");
		
		GeneralClusterWorkflow workflow = processWorkflow(getDaxFileExplorer(file), ClusterType.P2P);
		
		executeNsgaIII(workflow);
	}
	
	private static void executeNsgaIII(GeneralClusterWorkflow workflow) {
		DaDagNsgaIIIRunner runner = new DaDagNsgaIIIRunner();
		
		runner.setupRunner(workflow);
		runner.executeRunner();
	}

	private static GeneralClusterWorkflow processWorkflow(DaxFileExplorer explorer, ClusterType clusterType) {
		
		DaxGraph graph = null;

		graph = new DaxGraph(explorer);
		graph.setDaxClusterType(clusterType);
		logger.debug("");
		logger.info("==================");
		logger.debug("");
		graph.printOrginalDagNodes();
		logger.debug("");
		logger.info("==================");
		logger.debug("");
		graph.printOriginalDagDetails();
		logger.debug("");
		logger.info("=========Process Dax Matrix=========");
		logger.debug("");
		graph.processDaxMatrixGraph();
		logger.debug("");
		graph.printNodeLevelByMatrixId();
		logger.debug("");
		graph.printNodeLevelById();
		logger.debug("");
		graph.printNodeLevelByName();
		logger.debug("");
		graph.processClusterWorkflow();
		logger.debug("");
		graph.printNodeLevelByClusterId();
		logger.debug("");
		
		return graph.getDagWorkflow();
	}
	
	private static DaxFileExplorer getDaxFileExplorer(File file){
		DaxFileExplorer explorer = null;		

		try {			
			JAXBContext jaxbContext = JAXBContext.newInstance(Adag.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Adag aDag = (Adag) jaxbUnmarshaller.unmarshal(file);
			
			explorer = new DaxFileExplorer(file, aDag);
			explorer.exploreAdagNodes();
			
		} catch (JAXBException e) {
			logger.error(e);
			e.printStackTrace();
		}
				
		return explorer;
	}
}
