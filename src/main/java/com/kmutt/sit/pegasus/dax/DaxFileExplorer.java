package com.kmutt.sit.pegasus.dax;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.math3.util.Precision;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.kmutt.sit.mop.parameter.Configuration;
import com.kmutt.sit.pegasus.xsd.element.Adag;
import com.kmutt.sit.utils.JavaHelper;

public class DaxFileExplorer {
	
	private Logger logger = Logger.getLogger(DaxFileExplorer.class);
	
	private Map<String, DaxNode> daxNodes = new TreeMap<>();
	private List<String> fileList = new ArrayList<String>();
	
	private int headOneOneNodeNumber = 0;
	private int headOneManyNodeNumber = 0;

	private int interOneOneNodeNumber = 0;
	private int interOneManyNodeNumber = 0;
	private int interManyOneNodeNumber = 0;
	private int interManyManyNodeNumber = 0;

	private int endOneOneNodeNumber = 0;
	private int endManyOneNodeNumber = 0;
	
	private File xmlFile;
	private Adag dax;
	private int noOfEdges = 0;
	private int noOfFiles = 0;
	
	public DaxFileExplorer() {
		
	}
	
	public DaxFileExplorer(File file, Adag dax) {
		this.xmlFile = file;
		this.dax = dax;
	}
	
	public void showReport() {		
		logger.info(toString());
	}
	
	public String toString() {
		String report = String.format("[file:%s, nodes:%d, edges:%d, files:%d]",
						xmlFile.getName(), daxNodes.size(), noOfEdges, noOfFiles);
		return report;
	}
	
	public Map<String, DaxNode> getDaxNodes(){
		return this.daxNodes;
	}
	
	public void exploreAdagNodes() {
		if (!JavaHelper.isNull(dax)) {

			logger.info(dax);

			int i = 0;

			for (Adag.Job job : dax.getJob()) {
				DaxNode node = new DaxNode();
				node.inheritProperties(job);
				
				noOfFiles += node.getUses().size();

				i++;
				logger.debug(i + " " + node.toString());

				addNode(this.daxNodes, node);
			}
			
			determineNodeRuntime();
			exploreAdagDependencies();
			determineNodeType();
			
			this.daxNodes.forEach((k, v) -> {
				
				if(v.getRuntime().compareTo(v.getJobRuntime()) != 0) {
					logger.info(String.format("ID: %s, JobRuntime: %.2f, NodeRuntime: %.2f", v.getId(), v.getJobRuntime(), v.getRuntime()));
				}
				
			});
			
			logger.info("");

		} else {
			logger.info("Adag is empty!!");
		}
		
	}

	private void exploreAdagDependencies() {
		if (!JavaHelper.isNull(dax)) {

			logger.debug(dax);
			
			for (Adag.Child child : dax.getChild()) {
				noOfEdges += child.getParent().size();
				for (Adag.Child.Parent parent : child.getParent()) {					
					daxNodes.get(child.getRef().getId()).addParent(daxNodes.get(parent.getRef().getId()));
					daxNodes.get(parent.getRef().getId()).addChild(daxNodes.get(child.getRef().getId()));
				}
			}

		} else {
			logger.debug("Adag is empty!!");
		}
	}
	
	private void determineNodeRuntime() {
		
		if (!JavaHelper.isNull(xmlFile)) {
			
			try {
				
	            SAXBuilder builder = new SAXBuilder();
	            //parse using builder to get DOM representation of the XML file
	            Document dom = builder.build(xmlFile);
	            Element root = dom.getRootElement();
	            List<Element> list = root.getChildren();
	            
	            for (Element node : list) {
	                switch (node.getName().toLowerCase()) {
	                
                    case "job":
                    	String nodeId = node.getAttributeValue("id");
                    	long executeTime = 0;
                        double runtime = 0.00;
                        
                        if (node.getAttributeValue("runtime") != null) {
                            String nodeTime = node.getAttributeValue("runtime");
                            runtime = Double.parseDouble(nodeTime);
                            executeTime = (long) (1000 * runtime);
                            if (executeTime < 100) {
                            	runtime = 0.1;
                            	executeTime = 100;
                            }
                            executeTime = (long) runtime;
                        } else {
                            runtime = 0.1;
                            executeTime = 100;
                            logger.info("Cannot find runtime for " + nodeId + ",set it to be 0.1");
                        } 
                        
                		if (daxNodes.containsKey(nodeId)) {
                			
                			daxNodes.get(nodeId).setRuntime(BigDecimal.valueOf(runtime));
                			daxNodes.get(nodeId).setExecuteTime(executeTime);
                			
                			runtime *= Configuration.RUNTIME_SCALE;
                			runtime = runtime + (runtime * JavaHelper.getRandomOverheadRuntime());
                			
                			daxNodes.get(nodeId).setActualRuntime(
                						Precision.round(runtime, Configuration.PRECISION_SCALE)
                					);
                		}
                        
                    	break;
	                }	            	
	            }
				
				
			}  catch (JDOMException jde) {
	            logger.error("JDOM Exception;Please make sure your dax file is valid");

	        } catch (IOException ioe) {
	        	logger.error("IO Exception;Please make sure dax.path is correctly set in your config file");

	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("Parsing Exception");
	        }

		} else {
			logger.info("XML file is empty!!");
		}		
    }
	
	private void determineNodeType() {
		Set<Entry<String, DaxNode>> s = daxNodes.entrySet();
		Iterator<Entry<String, DaxNode>> it = s.iterator();
		
		while (it.hasNext()) {
			Entry<String, DaxNode> entry = it.next();
			DaxNode node = (DaxNode) entry.getValue();
			
			if (node.getChilds().isEmpty() && !node.getParents().isEmpty()) {
				node.setDaxNodeType(DaxNodeType.END);
				if(node.getParents().size() == 1) {
					node.setDaxNodeComType(DaxNodeComType.ONE_TO_ONE);
					endOneOneNodeNumber++;
				} else {
					node.setDaxNodeComType(DaxNodeComType.MANY_TO_ONE);
					endManyOneNodeNumber++;
				}
			} else if (node.getParents().isEmpty() && !node.getChilds().isEmpty()) {
				node.setDaxNodeType(DaxNodeType.HEAD);
				if(node.getChilds().size() == 1) {
					node.setDaxNodeComType(DaxNodeComType.ONE_TO_ONE);
					headOneOneNodeNumber++;
				} else {
					node.setDaxNodeComType(DaxNodeComType.ONE_TO_MANY);		
					headOneManyNodeNumber++;
				}
			} else {
				node.setDaxNodeType(DaxNodeType.INTER);
				if(node.getParents().size() == 1) {
					if(node.getChilds().size() == 1) {
						node.setDaxNodeComType(DaxNodeComType.ONE_TO_ONE);
						interOneOneNodeNumber++;
					} else {
						node.setDaxNodeComType(DaxNodeComType.ONE_TO_MANY);	
						interOneManyNodeNumber++;
					}
				} else {
					if(node.getChilds().size() == 1) {
						node.setDaxNodeComType(DaxNodeComType.MANY_TO_ONE);
						interManyOneNodeNumber++;
					} else {
						node.setDaxNodeComType(DaxNodeComType.MANY_TO_MANY);
						interManyManyNodeNumber++;
					}					
				}
			}
			
			logger.debug("Determine Node: " + node.getId() + ", Parents: " + node.getParents().size() + ", Childs: " + node.getChilds().size());
			logger.debug(String.format("[%d, %d, %d, %d, %d, %d, %d, %d]", this.headOneOneNodeNumber, this.headOneManyNodeNumber,
					this.interOneOneNodeNumber, this.interOneManyNodeNumber, this.interManyOneNodeNumber, this.interManyManyNodeNumber,
					this.endOneOneNodeNumber, this.endManyOneNodeNumber));		
			
		}
	}
	
	public void addNode(Map<String, DaxNode> nodeList, DaxNode node) {
		if (!nodeList.containsKey(node.getId())) {
			nodeList.put(node.getId(), node);
			logger.debug("New node added: " + node.getId());
		}
	}
	
	public int getHeadNodeNumber() {
		return headOneOneNodeNumber + headOneManyNodeNumber;
	}
	
	public int getEndNodeNumber() {
		return endOneOneNodeNumber + endManyOneNodeNumber;
	}

	public int getInterNodeNumber() {
		return interOneOneNodeNumber 
			 + interOneManyNodeNumber
			 + interManyOneNodeNumber
			 + interManyManyNodeNumber;
	}
	
	public int getOneToOneNodeNumber() {
		return headOneOneNodeNumber + interOneOneNodeNumber + endOneOneNodeNumber;
	}
	
	public int getOneToManyNodeNumber() {
		return headOneManyNodeNumber + interOneManyNodeNumber;
	}
	
	public int getManyToOneNodeNumber() {
		return interManyOneNodeNumber + endManyOneNodeNumber;
	}
	
	public int getManyToManyNodeNumber() {
		return interManyManyNodeNumber;
	}

	public int getHeadOneOneNodeNumber() {
		return headOneOneNodeNumber;
	}

	public int getHeadOneManyNodeNumber() {
		return headOneManyNodeNumber;
	}

	public int getInterOneOneNodeNumber() {
		return interOneOneNodeNumber;
	}

	public int getInterOneManyNodeNumber() {
		return interOneManyNodeNumber;
	}

	public int getInterManyOneNodeNumber() {
		return interManyOneNodeNumber;
	}

	public int getInterManyManyNodeNumber() {
		return interManyManyNodeNumber;
	}

	public int getEndOneOneNodeNumber() {
		return endOneOneNodeNumber;
	}

	public int getEndManyOneNodeNumber() {
		return endManyOneNodeNumber;
	}
}
