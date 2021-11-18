package com.kmutt.sit.java8;

import java.util.stream.IntStream;

public class NowJava8 {
    public static void main(String[] args) {

/*        List<Staff> staff = Arrays.asList(
                new Staff("mkyong", 30, new BigDecimal(10000)),
                new Staff("jack", 27, new BigDecimal(20000)),
                new Staff("lawrence", 33, new BigDecimal(30000))
        );

		// convert inside the map() method directly.
        List<StaffPublic> result = staff.stream().map(temp -> {
            StaffPublic obj = new StaffPublic();
            obj.setName(temp.getName());
            obj.setAge(temp.getAge());
            if ("mkyong".equals(temp.getName())) {
                obj.setExtra("this field is for mkyong only!");
            }
            return obj;
        }).collect(Collectors.toList());

        System.out.println(result);*/
    	
    	
		IntStream.range(0, 10).forEach(i -> {
			System.out.println("First Loop : i -> " + i);
		});

		System.out.println("====================");

		IntStream.range(1, 10).forEach(i -> {
			System.out.println("Second Loop: i -> " + i);
		});
    }
}
