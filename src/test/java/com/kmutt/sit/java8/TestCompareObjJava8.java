package com.kmutt.sit.java8;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.kmutt.sit.cloud.CloudVm;

public class TestCompareObjJava8 {
	
	private static Logger logger = Logger.getLogger(TestCompareObjJava8.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		List<Employee> emps = new ArrayList<Employee>();
		 
		emps.add(new Employee(1, "Lokesh", 36));
		emps.add(new Employee(2, "Alex", 46));
		emps.add(new Employee(3, "Brian", 52));
		 
		Comparator<Employee> comparator = Comparator.comparing(Employee::getAge);
		 
		// Get Min or Max Object
		Employee minObject = emps.stream().min(comparator).get();
		Employee maxObject = emps.stream().max(comparator).get();
		 
		logger.info("minObject = " + minObject);
		logger.info("maxObject = " + maxObject);
		
		
		List<CloudVm> vmList = new ArrayList<CloudVm>();
		
		CloudVm vm1 = new CloudVm();
		CloudVm vm2 = new CloudVm();
		CloudVm vm3 = new CloudVm();
		
		vm1.setNextAvailableTime(1.00);
		vm2.setNextAvailableTime(2.00);
		vm3.setNextAvailableTime(3.00);		
		
		vmList.add(vm1);
		vmList.add(vm2);
		vmList.add(vm3);

		Comparator<CloudVm> comparator1 = Comparator.comparing(CloudVm::getNextAvailableTime);

		CloudVm minObject1 = vmList.stream().min(comparator1).get();
		CloudVm maxObject1 = vmList.stream().max(comparator1).get();
		
		logger.info("minObject1 = " + minObject1.getNextAvailableTime());
		logger.info("maxObject1 = " + maxObject1.getNextAvailableTime());
		
	}

}
