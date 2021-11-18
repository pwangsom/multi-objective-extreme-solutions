package com.kmutt.sit.main.old;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.kmutt.sit.mop.parameter.Global;
import com.kmutt.sit.pegasus.dax.DaxFileExplorer;
import com.kmutt.sit.pegasus.dax.DaxGraph;
import com.kmutt.sit.pegasus.xsd.element.Adag;
import com.kmutt.sit.workflow.ClusterType;

public class DaDagManager {

	private Logger logger = Logger.getLogger(DaDagManager.class);
	
	private File inputFile;
	
	public DaDagManager(File fullPathFileName) {
		this.inputFile = fullPathFileName;
	}
	
	public String analyseDaxGraph() {
		
		DaxGraph graph = new DaxGraph(getDaxFileExplorer(this.inputFile));
		graph.setDaxClusterType(ClusterType.P2P);
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
		
		Integer clusteredNodes = graph.getDaxCluster().entrySet().stream().map(map -> map.getValue())
									.flatMap(List::stream).collect(Collectors.toList()).size();
		
		String result = this.inputFile.getName() + "," + Global.MODEL_RATIO + "," + clusteredNodes;

		String output = "========Ratio======\n";
		output += result + "\n";
		
		logger.info(output);
		
		return result;
	}
	
	private DaxFileExplorer getDaxFileExplorer(File file){
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
