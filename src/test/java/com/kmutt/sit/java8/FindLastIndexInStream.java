package com.kmutt.sit.java8;

import java.util.ArrayList;
import java.util.List;

import com.kmutt.sit.workflow.component.TimeSlot;

public class FindLastIndexInStream {
	
	private static List<TimeSlot> timeList = new ArrayList<TimeSlot>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		TimeSlot t1 = new TimeSlot();
		// t1.setEmptyTimeSlot(false);
		timeList.add(t1);

		TimeSlot t2 = new TimeSlot();
		// t2.setEmptyTimeSlot(false);
		timeList.add(t2);
		
		TimeSlot t3 = new TimeSlot();
		// t3.setEmptyTimeSlot(false);
		timeList.add(t3);		

		TimeSlot t4 = new TimeSlot();
		timeList.add(t4);

		TimeSlot t5 = new TimeSlot();
		// t5.setEmptyTimeSlot(false);
		timeList.add(t5);
		
		
		int index = getLastIndexOfNotNullTimeSlot();
		
		System.out.println("index: " + index);
		
		t1.setEmptyTimeSlot(false);
		timeList.set(3, t1);

		index = getLastIndexOfNotNullTimeSlot();
		
		System.out.println("index: " + index);
	}
	
	private static int getLastIndexOfNotNullTimeSlot() {
		
		// return -1 if there is no not null time slot it list
		int index = -1;
		
		if(timeList.stream().filter(f -> f.isEmptyTimeSlot() == false).count() > 0) {
			for(int i = timeList.size()-1; i >= 0; i--) {				
				if(timeList.get(i).isEmptyTimeSlot() == false) {
					index = i;
					i = -1;					
				}
			}
			
		}
		return index;
	}

}
