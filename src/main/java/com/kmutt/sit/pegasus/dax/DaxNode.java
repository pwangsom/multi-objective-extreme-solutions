package com.kmutt.sit.pegasus.dax;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.kmutt.sit.pegasus.xsd.element.Adag;
import com.kmutt.sit.pegasus.xsd.element.FilenameType;
import com.kmutt.sit.utils.JavaHelper;

public class DaxNode extends Adag.Job {
	
	private Logger logger = Logger.getLogger(DaxNode.class);	

	final public static String NULL_CLUSTER = "000-000";
	final public static String TEMP_CLUSTER = "999-999";
	
	private Map<String, DaxNode> parents = new TreeMap<>();
	private Map<String, DaxNode> childs = new TreeMap<>();
	
	private int levelId;
	private int matrixId = -1;
	private int vmId = -1;
	private String clusterId = DaxNode.NULL_CLUSTER;
	
	private DaxNodeType daxNodeType;
	private DaxNodeComType daxNodeComType;
	
	private long executeTime;

	private Double actualRuntime;
	private Double submittedTime;
	private Double lastParentFinishTime;
	private Double canStartTime;
	
	// waiting time due to wait for other tasks in the same cluster running
	private Double startWaitingTime;
	private Double waitingTime;
	private Double finishedTime;
	
	private BigDecimal jobRuntime;
	
	private boolean isNotFinish = true;

	public DaxNode() {
		
	}
	
	public Double getActualRuntime() {
		return actualRuntime;
	}

	public void setActualRuntime(Double actualRuntime) {
		this.actualRuntime = actualRuntime;
	}

	public Double getSubmittedTime() {
		return submittedTime;
	}

	public void setSubmittedTime(Double submittedTime) {
		this.submittedTime = submittedTime;
	}

	public Double getCanStartTime() {
		return canStartTime;
	}

	public void setCanStartTime(Double canStartTime) {
		this.canStartTime = canStartTime;
	}

	public Double getFinishedTime() {
		return finishedTime;
	}

	public void setFinishedTime(Double finishedTime) {
		this.finishedTime = finishedTime;
	}
	
	public Double getLastParentFinishTime() {
		return lastParentFinishTime;
	}

	public void setLastParentFinishTime(Double lastParentFinishTime) {
		this.lastParentFinishTime = lastParentFinishTime;
	}

	public Double getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime() {
		if(this.lastParentFinishTime > this.submittedTime) {
			this.startWaitingTime = this.lastParentFinishTime;
			this.waitingTime = this.canStartTime - this.startWaitingTime;
		} else {
			this.startWaitingTime = this.lastParentFinishTime;
			this.waitingTime = this.canStartTime - this.startWaitingTime;
		}
	}

	public BigDecimal getJobRuntime() {
		return jobRuntime;
	}

	public void setJobRuntime(BigDecimal jobRuntime) {
		this.jobRuntime = jobRuntime;
	}

	public Double getStartWaitingTime() {
		return startWaitingTime;
	}

	public long getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(long executeTime) {
		this.executeTime = executeTime;
	}

	public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	public int getLevelId() {
		return levelId;
	}

	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}

	public int getMatrixId() {
		return matrixId;
	}

	public void setMatrixId(int matrixId) {
		this.matrixId = matrixId;
	}

	public int getVmId() {
		return vmId;
	}

	public void setVmId(int vmId) {
		this.vmId = vmId;
	}

	public boolean isNotFinish() {
		return isNotFinish;
	}

	public void setNotFinish(boolean isNotFinish) {
		this.isNotFinish = isNotFinish;
	}

	public Double getRuntimeDouble() {
		return this.runtime.doubleValue();
	}

	public void addParent(DaxNode parent) {
		if (parents.containsKey(parent.getId())) {
			// logger.debug("Parent already exists: " + parent.getId());
		} else {
			parents.put(parent.getId(), parent);
			logger.debug("New parent added: " + parent.getId());
		}
	}
	
	public Map<String, DaxNode> getParents() {
		return parents;
	}
	
	public boolean isParent(String parentNodeId) {
		return parents.containsKey(parentNodeId);
	}

	public void addChild(DaxNode child) {
		if (parents.containsKey(child.getId())) {
			// logger.debug("Child already exists: " + child.getId());
		} else {
			childs.put(child.getId(), child);
			logger.debug("New child added: " + child.getId());
		}
	}

	public boolean isChild(String parentNodeId) {
		return childs.containsKey(parentNodeId);
	}
	
	public Map<String, DaxNode> getChilds() {
		return childs;
	}

	public DaxNodeType getDaxNodeType() {
		return daxNodeType;
	}

	public void setDaxNodeType(DaxNodeType daxNodeType) {
		this.daxNodeType = daxNodeType;
	}
	
	public DaxNodeComType getDaxNodeComType() {
		return daxNodeComType;
	}

	public void setDaxNodeComType(DaxNodeComType daxNodeComType) {
		this.daxNodeComType = daxNodeComType;
	}	

	public void displayFileList() {	
		if (!JavaHelper.isNull(this.getUses())) {
			for (FilenameType file : this.getUses()) {
				logger.info(file.toString());
			}

		} else {
			logger.info("FileList is empty!!");
		}
	}
	
	public void displayNodeDetails() {
		
		logger.info("======Dax Node Details=======");
		logger.info("Node ID       : " + this.id);
		logger.info("Node Name     : " + this.name);
		logger.info("Node Namespace: " + this.namespace);
		logger.info("Version       : " + this.version);
		logger.info("Runtime       : " + this.getRuntimeDouble());
		logger.info("Filelists     : " + this.getUses().size());
		logger.info("Parents       : " + this.getParents().size());
		logger.info("Childs        : " + this.getChilds().size());
		logger.info("Matrix ID     : " + this.matrixId);
		logger.info("Level ID      : " + this.levelId);
		logger.info("Cluster ID    : " + this.clusterId);
		logger.info("VM ID         : " + this.vmId);		
	}
	
    @Override
    public String toString() {
    	String str = String.format("{[ID: %s], [Namespace: %s], [Name: %s], [Version, %s], [Runtime: %.2f], [JobRuntime: %.2f], [Files: %d], [Parents: %d], [Children: %d]}", 
    				 this.id, this.namespace, this.name, this.version, this.runtime, this.jobRuntime, this.getUses().size(), this.getParents().size(), this.getChilds().size());
    	
    	
        return str;
    } 
    
/*    @Override
    public String toString() { 
        return this.id;
    } */
	
// Adag v21 and v211	
/*  protected Adag.Job.Argument argument;
    protected List<Adag.Job.Profile> profile;
    protected Adag.Job.Stdin stdin;
    protected Adag.Job.Stdout stdout;
    protected Adag.Job.Stderr stderr;
    protected List<FilenameType> uses;
    protected String namespace;
    protected String name;
    protected String version;
    protected String dvNamespace;
    protected String dvName;
    protected String dvVersion;
    protected String id;
    protected BigInteger level;
    protected List<String> compound;
*/
	public void inheritProperties(Adag.Job job) {
		this.argument = job.getArgument();
		this.profile = job.getProfile();
		this.stdin = job.getStdin();
		this.stdout = job.getStdout();
		this.stderr = job.getStderr();
		this.uses = job.getUses();
		this.namespace = job.getNamespace();
		this.name = job.getName();
		this.version = job.getVersion();
		this.dvNamespace = job.getDvNamespace();
		this.dvName = job.getDvName();
		this.dvVersion = job.getDvVersion();
		this.id = job.getId();
		this.level = job.getLevel();
		this.compound = job.getCompound();
		this.jobRuntime = job.getRuntime();
	}

	
// Adag v212
/*	
    protected List<Adag.Job.Uses> uses;
    protected String id;
    protected String namespace;
    protected String name;
    protected BigDecimal version;
    protected BigDecimal runtime;

	public void inheritProperties(Adag.Job job) {
		this.uses = job.getUses();
		this.id = job.getId();
		this.namespace = job.getNamespace();
		this.name = job.getName();
		this.version = job.getVersion();
		this.runtime = job.getRuntime();
	}

*/
}
