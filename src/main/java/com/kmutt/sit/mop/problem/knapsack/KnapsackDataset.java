package com.kmutt.sit.mop.problem.knapsack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

public class KnapsackDataset {
	
	private Logger logger = Logger.getLogger(KnapsackDataset.class);
	
	private String fileNamePath;
	private int size = 1;
	private String instanceId = "00";
	private Map<Integer, KnapsackItem> itemsMapping = new HashMap<Integer, KnapsackItem>();
	private int sumWeight = 0;
	private int weightCapacity = 0;
	private int numberCapacity = 0;
	
	private int maxObjective1 = 0;
	private int maxObjective2 = 0;
	private int maxObjective3 = 0;
	
	private int extremeObjective1 = 0;
	private int extremeObjective2 = 0;
	private int extremeObjective3 = 0;	

	private int extremeWeight1 = 0;
	private int extremeWeight2 = 0;
	private int extremeWeight3 = 0;
	
	private Integer[] extremeIndexObj1;
	private Integer[] extremeIndexObj2;
	private Integer[] extremeIndexObj3;
	
	private Integer[] extremeOrder1;
	private Integer[] extremeOrder2;
	private Integer[] extremeOrder3;
		
	public KnapsackDataset(String fileNamePath, int size, String instanceId) {
		this.fileNamePath = fileNamePath;
		this.size = size;
		this.instanceId = instanceId;
		
		extremeIndexObj1 = new Integer[this.size];
		extremeIndexObj2 = new Integer[this.size];
		extremeIndexObj3 = new Integer[this.size];
		
		extremeOrder1 = new Integer[this.size];
		extremeOrder2 = new Integer[this.size];
		extremeOrder3 = new Integer[this.size];
		
		Arrays.fill(extremeIndexObj1, 0);
		Arrays.fill(extremeIndexObj2, 0);
		Arrays.fill(extremeIndexObj3, 0);
		
		Arrays.fill(extremeOrder1, 0);
		Arrays.fill(extremeOrder2, 0);
		Arrays.fill(extremeOrder3, 0);
	}
	
	public KnapsackItem getKnapSackItemById(Integer id) {
		return itemsMapping.get(id);
	}
	
	@SuppressWarnings("resource")
	public void loadDatasetFile() {
		try {

			File f = new File(fileNamePath);

			BufferedReader reader = new BufferedReader(new FileReader(f));

			String readLine = "";

			logger.info("Reading file using Buffered Reader");
			
			int mapKey = 0;

			while ((readLine = reader.readLine()) != null) {
				String[] dataLine = readLine.split(",");
				if(dataLine.length == 4) {
					mapKey++;
					addKnapsackItem(mapKey, Integer.parseInt(dataLine[0]), Integer.parseInt(dataLine[1]),
									Integer.parseInt(dataLine[2]), Integer.parseInt(dataLine[3]));
				} else {
					throw new IllegalArgumentException("Each data line should contain 4 values.");
				}
			}
			
			if(mapKey == size) {
				findingNumberAndWeightCapacity();
				findingExtremeWeightCapacity();
			} else {
				throw new IllegalArgumentException("Actual data size is not correct. (Size: " + size + ", Actual: " + mapKey + ")");				
			}

		} catch (IOException e) {
			logger.error(e, e);
		} catch (Exception e) {
			logger.error(e, e);			
		}
	}
	
	private void findingExtremeWeightCapacity() {
		
		List<KnapsackItem> itemList = new ArrayList<KnapsackItem>(itemsMapping.values());
		
	    Comparator<KnapsackItem> comparator1 = Comparator.comparingInt(KnapsackItem::getObjective1).reversed();
	    comparator1 = comparator1.thenComparing(Comparator.comparingInt(KnapsackItem::getWeight));
	    
	    Comparator<KnapsackItem> comparator2 = Comparator.comparingInt(KnapsackItem::getObjective2).reversed();
	    comparator2 = comparator2.thenComparing(Comparator.comparingInt(KnapsackItem::getWeight));
	    
	    Comparator<KnapsackItem> comparator3 = Comparator.comparingInt(KnapsackItem::getObjective3).reversed();
	    comparator3 = comparator3.thenComparing(Comparator.comparingInt(KnapsackItem::getWeight));
		
		List<KnapsackItem> extremeOrderItem1 = itemList.stream().sorted(comparator1).collect(Collectors.toList());
		List<KnapsackItem> extremeOrderItem2 = itemList.stream().sorted(comparator2).collect(Collectors.toList());
		List<KnapsackItem> extremeOrderItem3 = itemList.stream().sorted(comparator3).collect(Collectors.toList());
		
		logger.debug("");
		logger.debug("Orginal Obj1: " + itemList.stream().map(i -> i.getObjective1()).collect(Collectors.toList()));
		logger.debug("Sorted  Obj1: " + extremeOrderItem1.stream().map(i -> i.getObjective1()).collect(Collectors.toList()));
		logger.debug("");
		logger.debug("Orginal Obj2: " + itemList.stream().map(i -> i.getObjective2()).collect(Collectors.toList()));
		logger.debug("Sorted  Obj2: " + extremeOrderItem2.stream().map(i -> i.getObjective2()).collect(Collectors.toList()));
		logger.debug("");
		logger.debug("Orginal Obj3: " + itemList.stream().map(i -> i.getObjective3()).collect(Collectors.toList()));
		logger.debug("Sorted  Obj3: " + extremeOrderItem3.stream().map(i -> i.getObjective3()).collect(Collectors.toList()));
		logger.debug("");
		
		for(int i = 0; i < itemList.size(); i++) {
			
			extremeOrder1[i] = extremeOrderItem1.get(i).getItemIndex();
			extremeOrder2[i] = extremeOrderItem2.get(i).getItemIndex();
			extremeOrder3[i] = extremeOrderItem3.get(i).getItemIndex();
			
			if(extremeWeight1 + extremeOrderItem1.get(i).getWeight() <= weightCapacity) {
				extremeWeight1 += extremeOrderItem1.get(i).getWeight();
				extremeObjective1 += extremeOrderItem1.get(i).getObjective1();
				extremeIndexObj1[extremeOrderItem1.get(i).getItemIndex()] = 1;
			}
			
			if(extremeWeight2 + extremeOrderItem2.get(i).getWeight() <= weightCapacity) {
				extremeWeight2 += extremeOrderItem2.get(i).getWeight();
				extremeObjective2 += extremeOrderItem2.get(i).getObjective2();
				extremeIndexObj2[extremeOrderItem2.get(i).getItemIndex()] = 1;
			}
			
			if(extremeWeight3 + extremeOrderItem3.get(i).getWeight() <= weightCapacity) {
				extremeWeight3 += extremeOrderItem3.get(i).getWeight();
				extremeObjective3 += extremeOrderItem3.get(i).getObjective3();
				extremeIndexObj3[extremeOrderItem3.get(i).getItemIndex()] = 1;
			}

		}
		
		logger.debug("");
		logger.debug(Arrays.asList(extremeIndexObj1));
		logger.debug("Extreme 1 -> weight: " + extremeWeight1 + ", objective: " + extremeObjective1);
		logger.debug("");
		logger.debug(Arrays.asList(extremeIndexObj2));
		logger.debug("Extreme 2 -> weight: " + extremeWeight2 + ", objective: " + extremeObjective2);
		logger.debug("");
		logger.debug(Arrays.asList(extremeIndexObj3));
		logger.debug("Extreme 3 -> weight: " + extremeWeight3 + ", objective: " + extremeObjective3);
		logger.debug("");
	}
	
	private void findingNumberAndWeightCapacity() {
		weightCapacity = sumWeight/2;
		numberCapacity = weightCapacity/(sumWeight/size);
	}
	
	private void addKnapsackItem(int mapKey, int obj1, int obj2, int obj3, int weight) {
		itemsMapping.put(mapKey, new KnapsackItem(mapKey-1, obj1, obj2, obj3, weight));
		
		maxObjective1 += obj1;
		maxObjective2 += obj2;
		maxObjective3 += obj3;
		sumWeight += weight;
	}
	
	@Override
	public String toString() {
		String str = String.format("Size: %d, InstanceID: %s, SumWeight: %d, WeightCap: %d,"
								 + " ItemsCap: %d, MaxObj1: %d, MaxObj2: %d, MaxObj3: %d",
								 + size, instanceId, sumWeight, weightCapacity, numberCapacity,
								 + maxObjective1, maxObjective2, maxObjective3);
		
		return str;
	}
	
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public Map<Integer, KnapsackItem> getItemsMapping() {
		return itemsMapping;
	}

	public void setItemsMapping(Map<Integer, KnapsackItem> itemsMapping) {
		this.itemsMapping = itemsMapping;
	}

	public int getSumWeight() {
		return sumWeight;
	}
	public void setSumWeight(int sumWeight) {
		this.sumWeight = sumWeight;
	}
	public int getWeightCapacity() {
		return weightCapacity;
	}
	public void setWeightCapacity(int weightCapacity) {
		this.weightCapacity = weightCapacity;
	}
	public int getNumberCapacity() {
		return numberCapacity;
	}
	public void setNumberCapacity(int numberCapacity) {
		this.numberCapacity = numberCapacity;
	}
	public int getMaxObjective1() {
		return maxObjective1;
	}
	public void setMaxObjective1(int maxObjective1) {
		this.maxObjective1 = maxObjective1;
	}
	public int getMaxObjective2() {
		return maxObjective2;
	}
	public void setMaxObjective2(int maxObjective2) {
		this.maxObjective2 = maxObjective2;
	}
	public int getMaxObjective3() {
		return maxObjective3;
	}
	public void setMaxObjective3(int maxObjective3) {
		this.maxObjective3 = maxObjective3;
	}

	public int getExtremeObjective1() {
		return extremeObjective1;
	}

	public void setExtremeObjective1(int extremeObjective1) {
		this.extremeObjective1 = extremeObjective1;
	}

	public int getExtremeObjective2() {
		return extremeObjective2;
	}

	public void setExtremeObjective2(int extremeObjective2) {
		this.extremeObjective2 = extremeObjective2;
	}

	public int getExtremeObjective3() {
		return extremeObjective3;
	}

	public void setExtremeObjective3(int extremeObjective3) {
		this.extremeObjective3 = extremeObjective3;
	}

	public int getExtremeWeight1() {
		return extremeWeight1;
	}

	public void setExtremeWeight1(int extremeWeight1) {
		this.extremeWeight1 = extremeWeight1;
	}

	public int getExtremeWeight2() {
		return extremeWeight2;
	}

	public void setExtremeWeight2(int extremeWeight2) {
		this.extremeWeight2 = extremeWeight2;
	}

	public int getExtremeWeight3() {
		return extremeWeight3;
	}

	public void setExtremeWeight3(int extremeWeight3) {
		this.extremeWeight3 = extremeWeight3;
	}

	public Integer[] getExtremeIndexObj1() {
		return extremeIndexObj1;
	}

	public void setExtremeIndexObj1(Integer[] extremeIndexObj1) {
		this.extremeIndexObj1 = extremeIndexObj1;
	}

	public Integer[] getExtremeIndexObj2() {
		return extremeIndexObj2;
	}

	public void setExtremeIndexObj2(Integer[] extremeIndexObj2) {
		this.extremeIndexObj2 = extremeIndexObj2;
	}

	public Integer[] getExtremeIndexObj3() {
		return extremeIndexObj3;
	}

	public void setExtremeIndexObj3(Integer[] extremeIndexObj3) {
		this.extremeIndexObj3 = extremeIndexObj3;
	}

	public Integer[] getExtremeOrder1() {
		return extremeOrder1;
	}

	public void setExtremeOrder1(Integer[] extremeOrder1) {
		this.extremeOrder1 = extremeOrder1;
	}

	public Integer[] getExtremeOrder2() {
		return extremeOrder2;
	}

	public void setExtremeOrder2(Integer[] extremeOrder2) {
		this.extremeOrder2 = extremeOrder2;
	}

	public Integer[] getExtremeOrder3() {
		return extremeOrder3;
	}

	public void setExtremeOrder3(Integer[] extremeOrder3) {
		this.extremeOrder3 = extremeOrder3;
	}

}
