package com.kmutt.sit.mop.runner.scheduling.planner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.util.Precision;

import com.kmutt.sit.mop.parameter.Configuration;
import com.kmutt.sit.pegasus.dax.DaxNode;
import com.kmutt.sit.pegasus.xsd.element.FilenameType;
import com.kmutt.sit.pegasus.xsd.element.LinkageType;
import com.kmutt.sit.utils.JavaHelper;
import com.kmutt.sit.workflow.component.CommunicationTask;

public class CommunicationPlanner {
		
	private Map<String, Integer> storageMap = new HashMap<String, Integer>();
	
	private Double networkDegradation = 0.0;
	
	public void setNetworkDegradation(Double networkDegradation) {
		this.networkDegradation = networkDegradation;
	}
	
	public void recordOutputFile(DaxNode computeTask, int sourceVmId) {
		
		if(!JavaHelper.isNull(computeTask.getUses()) && !computeTask.getUses().isEmpty()) {
			computeTask.getUses().stream().forEach(file -> {
				if(file.getLink() == LinkageType.OUTPUT || file.getLink() == LinkageType.INOUT) {
					this.storageMap.put(file.getFile(), sourceVmId);
				}
			});
		}
		
	}
	
	public List<CommunicationTask> accessInpuFile(DaxNode computeTask, int targetVmId){
		
		List<CommunicationTask> commuTaskList = new ArrayList<CommunicationTask>();
		
		if(!JavaHelper.isNull(computeTask.getUses()) && !computeTask.getUses().isEmpty()) {
			computeTask.getUses().stream().forEach(file -> {
				
				if(file.getLink() == LinkageType.INPUT || file.getLink() == LinkageType.INOUT) {

					if(this.storageMap.containsKey(file.getFile())) {
						commuTaskList.add(getCommunicationTask(file, this.storageMap.get(file.getFile()), targetVmId));
					}
					
				}
				
			});
		}
		
		return commuTaskList;
		
	}
	
	private CommunicationTask getCommunicationTask(FilenameType file, int sourceVmId, int targetVmId) {
		
		CommunicationTask commuTask = new CommunicationTask();
		
		commuTask.setFileName(file.getFile());
		
		Double fileSize = file.getSize().doubleValue() * Configuration.FILE_SIZE_MAGNITUDE * Configuration.FILE_SIZE_SCALE;
		
		commuTask.setFileSize(fileSize);
		commuTask.setSourceVmId(sourceVmId);
		commuTask.setTargetVmId(targetVmId);
		
		if(sourceVmId == targetVmId) {
			commuTask.setLocalFile(true);
			commuTask.setCommunicationTime(0.00);
		} else {
			commuTask.setLocalFile(false);

			
			Double commuTime = (fileSize / (Configuration.INTER_VM_THROUGHPUT - (Configuration.INTER_VM_THROUGHPUT * networkDegradation)));
			
			// Add overhead in communication time
			commuTime = commuTime + (commuTime * JavaHelper.getRandomOverheadCommutime());
			
			commuTask.setCommunicationTime(Precision.round(commuTime, Configuration.PRECISION_SCALE));
		}
		
		return commuTask;
	}

}
