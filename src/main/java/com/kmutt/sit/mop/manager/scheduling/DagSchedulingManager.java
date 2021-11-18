package com.kmutt.sit.mop.manager.scheduling;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.uma.jmetal.solution.IntegerSolution;

import com.kmutt.sit.cloud.vm.model.AwsProviderModel;
import com.kmutt.sit.cloud.vm.model.GceProviderModel;
import com.kmutt.sit.cloud.vm.model.ProviderModelInterface;
import com.kmutt.sit.mop.algorithm.AlgorithmType;
import com.kmutt.sit.mop.parameter.Configuration;
import com.kmutt.sit.mop.pareto.collection.proprietary.DagSchedulingPopulationOfGenerationCollection;
import com.kmutt.sit.mop.problem.scheduling.ThirdObjectiveType;
import com.kmutt.sit.pegasus.dax.DaxFileExplorer;
import com.kmutt.sit.pegasus.dax.DaxGraph;
import com.kmutt.sit.pegasus.xsd.element.Adag;
import com.kmutt.sit.utils.JavaHelper;
import com.kmutt.sit.workflow.ClusterType;
import com.kmutt.sit.workflow.GeneralClusterWorkflow;

public class DagSchedulingManager {

	private Logger logger = Logger.getLogger(DagSchedulingManager.class);
	
	private DagSchedulingConfiguration config;
	
	private List<AlgorithmType> algorithmList;
	private List<ClusterType> clusterList;
	
	public DagSchedulingManager() {
		config = new DagSchedulingConfiguration();
		algorithmList = new ArrayList<AlgorithmType>();
		clusterList = new ArrayList<ClusterType>();
	}
	
	public void manage() {

		logger.debug("");
		logger.info("DAG-SCHEDULING Starting...");
		
		displayConfigurations();
		createSubfolderOutput();
				
		String fullPathFileName = JavaHelper.appendPathName(this.config.getInputPath(), this.config.getWorkflowName() + "_" 
								+ this.config.getWorkflowSize() + ".xml");
		

		DaxFileExplorer explorer = getDaxFileExplorer(fullPathFileName);
		
		if(!JavaHelper.isNull(explorer)) {
			
			if(algorithmList.isEmpty()) {
				algorithmList = Arrays.asList(AlgorithmType.NSGA_II, AlgorithmType.NSGA_III, AlgorithmType.ENSGA_III);
			}
			
			if(clusterList.isEmpty()) {
				clusterList = Arrays.asList(ClusterType.NONE, ClusterType.P2P, ClusterType.LEVEL, ClusterType.HORIZONTAL);
			}
			
			this.config.setAlgorithmTypeList(algorithmList);
			
			setClusterWorkflow(explorer);
			
			DagSchedulingExperiment experimentor = new DagSchedulingExperiment().setConfigurations(config);
			experimentor.runExperiment();
			
			analysis(experimentor.getPopulationOfGenerationCollection(), experimentor.getConfiguration());
		}
		
		logger.debug("");
		logger.info("DAG-SCHEDULING Finished...");
		logger.debug("");
	}
	
	private void analysis(DagSchedulingPopulationOfGenerationCollection<IntegerSolution> collection, DagSchedulingConfiguration config) {
		DagSchedulingAnalysis analyzer = new DagSchedulingAnalysis(collection, config);
		analyzer.createReferenceParetoFront();
		analyzer.measureIndicator();
	}
	
	public DagSchedulingManager setProgramKey(String programKey) {
		this.config.setProgramKey(programKey);
		
		return this;
	}
	
	public DagSchedulingManager setWorkingPath(String path) {
		this.config.setWorkingPath(path);
		this.config.setInputPath(JavaHelper.appendPathName(path, "input"));
		this.config.setOutputPath(JavaHelper.appendPathName(path, "output"));
		
		return this;
	}
	
	public DagSchedulingManager setWorkflowNameSize(String workflowName, int workflowSize) {
		this.config.setWorkflowName(workflowName);
		this.config.setWorkflowSize(workflowSize);
		
		return this;
	}
	
	public DagSchedulingManager setThirdObjective(String name) {
		
		if(name.equalsIgnoreCase(ThirdObjectiveType.COMMU.getName())) {
			this.config.setThirdObjectiveType(ThirdObjectiveType.COMMU);
		} else if(name.equalsIgnoreCase(ThirdObjectiveType.VM.getName())) {
			this.config.setThirdObjectiveType(ThirdObjectiveType.VM);			
		}
		
		return this;
	}

	public DagSchedulingManager setMaxRun(int maxRun) {
		this.config.setMaxRun(maxRun);
		
		return this;
	}
	
	public DagSchedulingManager setMaxIteration(int maxIteration) {
		this.config.setAllMaxIteration(maxIteration);
		
		return this;
	}
	
	public DagSchedulingManager setIntervalIteration(int interval) {
		this.config.setIntervalIteration(interval);
		
		return this;
	}
	
	public DagSchedulingManager setAlgorithmList(String[] algorithms) {
		Arrays.asList(algorithms).stream().forEach(algorithm -> {
			
			switch (algorithm.trim().toLowerCase()) {
			case "nsgaii":
				algorithmList.add(AlgorithmType.NSGA_II);
				break;
			case "nsgaiii":
				algorithmList.add(AlgorithmType.NSGA_III);
				break;
			case "ensgaiii":
				algorithmList.add(AlgorithmType.ENSGA_III);
				break;
			case "moeadd":
				algorithmList.add(AlgorithmType.MOEADD);
				break;
			default:
				break;
			}			
		});
		
		return this;
	}
	
	public DagSchedulingManager setClusterList(String[] clusters) {		
		Arrays.asList(clusters).stream().forEach(cluster -> {
			
			switch (cluster.trim().toLowerCase()) {
			case "none":
				clusterList.add(ClusterType.NONE);
				break;
			case "p2p":
				clusterList.add(ClusterType.P2P);
				break;
			case "level":
				clusterList.add(ClusterType.LEVEL);
				break;
			case "hori":
				clusterList.add(ClusterType.HORIZONTAL);
				break;
			case "single":
				clusterList.add(ClusterType.SINGLE);
				break;
			default:
				break;
			}			
		});
		
		return this;
	}
	
	public DagSchedulingManager setVmModel(String vmModel, Double vmBootTime, Double vmDegrade, Double networkDegrade) {
		
		ProviderModelInterface model = null;
		
		switch (vmModel.trim().toLowerCase()) {
		case "aws":
			model = new AwsProviderModel(vmBootTime, vmDegrade);
			break;
		case "gce":
			model = new GceProviderModel(vmBootTime, vmDegrade);
			break;
		default:
			model = new AwsProviderModel(vmBootTime, vmDegrade);
			break;
		}

		this.config.setProviderModel(model);
		this.config.setNetworkDegradation(networkDegrade);
		
		return this;
	}
	
	public DagSchedulingManager setSkipUncompliedP2pWorkflow(boolean skipUnP2p) {
		this.config.setSkipUncompliedP2pWorkflow(skipUnP2p);
		
		return this;
	}
	
	public DagSchedulingManager setMagnitudeParameters(Double filesizeScale, Double runtimeScale) {
		Configuration.FILE_SIZE_SCALE = filesizeScale;
		Configuration.RUNTIME_SCALE = runtimeScale;
		
		return this;
	}
	
	private void setClusterWorkflow(DaxFileExplorer explorer) {
		
		List<String> p2pWorkflowList = Arrays.asList("epigenomics", "ligo", "montage");
		
		clusterList.stream().forEach(clusterType -> {
			
			switch (clusterType) {
			case NONE:
				// this.config.setNoneClusterWorkflow(getClusterWorkflow(explorer, ClusterType.NONE));
				this.config.addClusterWorkflow(ClusterType.NONE, getClusterWorkflow(explorer, ClusterType.NONE));
				this.config.getClusterTypeList().add(ClusterType.NONE);
				break;
			case P2P:
				if(!this.config.isSkipUncompliedP2pWorkflow()) {
					// this.config.setP2pClusterWorkflow(getClusterWorkflow(explorer, ClusterType.P2P));
					this.config.addClusterWorkflow(ClusterType.P2P, getClusterWorkflow(explorer, ClusterType.P2P));
					this.config.setThereP2pCluster(true);
					this.config.getClusterTypeList().add(ClusterType.P2P);
				} else if (p2pWorkflowList.stream().anyMatch(str -> str.trim().equalsIgnoreCase(this.config.getWorkflowName()))) {
					// this.config.setP2pClusterWorkflow(getClusterWorkflow(explorer, ClusterType.P2P));
					this.config.addClusterWorkflow(ClusterType.P2P, getClusterWorkflow(explorer, ClusterType.P2P));
					this.config.setThereP2pCluster(true);
					this.config.getClusterTypeList().add(ClusterType.P2P);
				}
				break;
			case LEVEL:
				// this.config.setP2pLevelClusterWorkflow(getClusterWorkflow(explorer, ClusterType.LEVEL));
				this.config.addClusterWorkflow(ClusterType.LEVEL, getClusterWorkflow(explorer, ClusterType.LEVEL));
				this.config.getClusterTypeList().add(ClusterType.LEVEL);
				break;
			case HORIZONTAL:
				// this.config.setHorizontalClusterWorkflow(getClusterWorkflow(explorer, ClusterType.HORIZONTAL));
				this.config.addClusterWorkflow(ClusterType.HORIZONTAL, getClusterWorkflow(explorer, ClusterType.HORIZONTAL));
				this.config.getClusterTypeList().add(ClusterType.HORIZONTAL);				
				break;
			case SINGLE:
				this.config.addClusterWorkflow(ClusterType.SINGLE, getClusterWorkflow(explorer, ClusterType.SINGLE));
				this.config.getClusterTypeList().add(ClusterType.SINGLE);				
				break;

			default:
				break;
			}
			
		});		
		
	}
	
	private GeneralClusterWorkflow getClusterWorkflow(DaxFileExplorer explorer, ClusterType clusterType) {
		
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
		graph.printWorkflowByGroupLevel();
		logger.debug("");
		
		return graph.getDagWorkflow();
	}
	
	private DaxFileExplorer getDaxFileExplorer(String fullPathFileName){
		
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
	
	private void displayConfigurations() {
		
		logger.debug("");
		logger.info("=============Configuration=============");
		logger.debug("");

		logger.info("Program Key    : " + this.config.getProgramKey());
		logger.info("Working Path   : " + this.config.getWorkingPath());
		logger.info("Input Path     : " + this.config.getInputPath());
		logger.info("Output Path    : " + JavaHelper.appendPathName(this.config.getOutputPath(), this.config.getProgramKey()));
		logger.info("Workflow Name  : " + this.config.getWorkflowName());
		logger.info("Workflow Size  : " + this.config.getWorkflowSize());
		logger.info("Third Objective: " + this.config.getThirdObjectiveName());
		logger.info("Skip UnP2P     : " + this.config.isSkipUncompliedP2pWorkflow());
		logger.info("Max Run        : " + this.config.getMaxRun());
		logger.info("Max Iteration  : " + this.config.getAllMaxIteration());
		logger.info("Interval       : " + this.config.getIntervalIteration());
		logger.info("Cloud Provider : " + this.config.getProviderModel().getProviderModelType().getName());
		logger.info("VM Boot Time   : " + this.config.getProviderModel().getVmBootTime());
		logger.info("VM Degradation : " + this.config.getProviderModel().getVmDegradation());
		logger.info("Network        : " + this.config.getNetworkDegradation());
		logger.info("File size scale: " + Configuration.FILE_SIZE_SCALE);
		logger.info("Runtime scale  : " + Configuration.RUNTIME_SCALE);

		logger.debug("");
		
	}

	private void createSubfolderOutput() {
	    File directory = new File(JavaHelper.appendPathName(this.config.getOutputPath(), this.config.getProgramKey()));
	    
	    if (!directory.exists()){
	        directory.mkdir();
	    }	    
	}
}
