package com.kmutt.sit.workflow;

public class WorkflowBuilder {
	
	public static GeneralClusterWorkflow getWorkflowBuilder(ClusterType type) {
		
		GeneralClusterWorkflow workflow;
		

		switch(type) {
		case BoT:
			workflow = new BotClusterWorkflow();
			break;
		case P2P:
			workflow = new P2pClusterWorkflow();
			break;
		case LEVEL:
			workflow = new LevelClusterWorkflow();
			break;
		case HORIZONTAL:
			workflow = new HorizontalClusterWorkflow();
			break;
		case SINGLE:
			workflow = new SingleInputClusterWorkflow();
			break;
		default:
			workflow = new NoneClusterWorkflow();
			break;
		}
		
		return workflow;
	}

}
