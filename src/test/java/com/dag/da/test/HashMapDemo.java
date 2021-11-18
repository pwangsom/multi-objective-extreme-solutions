package com.dag.da.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HashMapDemo {
	@SuppressWarnings("unchecked")
	public static void main(String args[]) {

		// create two hash maps
		Map<Integer, List<String>> newmap1 = new TreeMap<Integer, List<String>>();
		Map<Integer, List<String>> newmap2 = new TreeMap<Integer, List<String>>();

		// populate 1st map
		newmap1.put(1, Arrays.asList("tutorials", "tu"));
		newmap1.put(2, Arrays.asList("is", "the"));
		newmap1.put(3, Arrays.asList("best", "of best"));

		// clone 1st map
		newmap2 = (Map<Integer, List<String>>) ((TreeMap<Integer, List<String>>) newmap1).clone();

		System.out.println("       1st Map: " + newmap1);
		System.out.println("Cloned 2nd Map: " + newmap2);
		
		newmap2.put(1, Arrays.asList("tutorials", "kmutt"));
		
		System.out.println("");
		System.out.println("       1st Map: " + newmap1);
		System.out.println("Cloned 2nd Map: " + newmap2);		

		newmap2.put(1, Arrays.asList("peerasak", "wangsom"));
		
		System.out.println("");
		System.out.println("       1st Map: " + newmap1);
		System.out.println("Cloned 2nd Map: " + newmap2);
		
		List<String> current = new ArrayList<String>(); 
		current.addAll(newmap2.get(1));
		current.add("tong");
		newmap2.put(1, current);
		
		System.out.println("");
		System.out.println("       1st Map: " + newmap1);
		System.out.println("Cloned 2nd Map: " + newmap2);
	}

}
