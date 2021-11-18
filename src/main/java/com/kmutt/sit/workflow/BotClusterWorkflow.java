package com.kmutt.sit.workflow;

public class BotClusterWorkflow extends AbstractClusterWorkflow {
	
	public BotClusterWorkflow() {
		super(ClusterType.BoT);		
	}

	@Override
	public GeneralClusterWorkflow convertWorkflowByClusterType() {
		// TODO Auto-generated method stub
		return this;
	}

}
