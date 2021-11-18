package com.kmutt.sit.main.old;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.kmutt.sit.cloud.CloudScheduling;
import com.kmutt.sit.pegasus.dax.DaxGraph;
import com.kmutt.sit.pegasus.dax.DaxNode;
import com.kmutt.sit.pegasus.xsd.element.Adag;
import com.kmutt.sit.workflow.ClusterType;

public class JAXBExample {

	private static Logger logger = Logger.getLogger(JAXBExample.class);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// File file = new File("src/main/resources/input/pegasus/dataset1/Montage_25.xml");
		// File file = new File("src/main/resources/input/pegasus/dataset1/Sipht_30.xml");
		File file = new File("src/main/resources/input/pegasus/dataset1/Epigenomics_24.xml");
		// File file = new File("src/main/resources/input/pegasus/dataset1/CyberShake_30.xml");
		// File file = new File("src/main/resources/input/pegasus/dataset1/Inspiral_1000.xml");
				
		
		processWorkflow(file, ClusterType.HORIZONTAL, 2, CloudScheduling.SchedulingType.FIFO);
	}
	
	private static void scheduleWorkflow(DaxGraph workflow, int noOfVms, CloudScheduling.SchedulingType schedulingType) {
		logger.info("=========Workflow Scheduling=========");
		
		CloudScheduling scheduler = new CloudScheduling(schedulingType);
		scheduler.intialCloudEnvironment(noOfVms);
		scheduler.setWorkflow(workflow);
		scheduler.scheduleWorkflow();
		logger.debug("");
		scheduler.printSchedulingReport();
		logger.debug("");
		scheduler.printCloudEvent();
		logger.debug("");
		scheduler.printCloudVmAction();
	}
	
	private static void processWorkflow(File file, ClusterType clusterType, int noOfVms, CloudScheduling.SchedulingType schedulingType) {

		try {			
			// String path = new File("src/main/resources/input/pegasus/CyberShakes_50.xml").getAbsolutePath();
			// logger.info(path);
			JAXBContext jaxbContext = JAXBContext.newInstance(Adag.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Adag aDag = (Adag) jaxbUnmarshaller.unmarshal(file);
			
/*			DaxGraph aGraph = new DaxGraph(file);
			aGraph.setDax(aDag);
			aGraph.setDaxClusterType(clusterType);
			aGraph.exploreAdagNodes();
			logger.debug("");
			logger.info("==================");
			logger.debug("");
			aGraph.printOrginalDagNodes();
			logger.debug("");
			logger.info("==================");
			logger.debug("");
			aGraph.printOriginalDagDetails();
			logger.debug("");
			logger.info("=========Process Dax Matrix=========");
			logger.debug("");
			logger.debug("");
			aGraph.intialDaxMatrixGraph();
			logger.info("Original Dax Matrix");
			aGraph.printDaxMatrixGraph();
			aGraph.determineNodeLevel();
			logger.debug("");
			aGraph.printNodeLevelByMatrixId();
			logger.debug("");
			aGraph.printNodeLevelById();
			logger.debug("");
			aGraph.printNodeLevelByName();
			logger.debug("");
			aGraph.processDaxGraphClustering();
			logger.debug("");
			aGraph.printNodeLevelByClusterId();
			logger.debug("");

			logger.info("=========Sample Dax Node=========");
			logger.debug("");
			DaxNode node = aGraph.getAdagNode("ID00002");
			node.displayNodeDetails();
			node.displayFileList();			

			scheduleWorkflow(aGraph, noOfVms, schedulingType);*/
			
		} catch (JAXBException e) {
			logger.error(e);
			e.printStackTrace();
		}
	}

}
