package com.kmutt.sit.coverageratio;

public class ThreeObjectives {
	
	private Integer id;
	
	private String name;
	
	private Double first;
	private Double second;
	private Double third;
	
	public ThreeObjectives() {
		
	}
	
	public ThreeObjectives(Double first, Double second, Double third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getFirst() {
		return first;
	}
	public void setFirst(Double first) {
		this.first = first;
	}
	public Double getSecond() {
		return second;
	}
	public void setSecond(Double second) {
		this.second = second;
	}
	public Double getThird() {
		return third;
	}
	public void setThird(Double third) {
		this.third = third;
	}
	
	public boolean isDominate(ThreeObjectives another) {
		boolean result = false;
		
		int score = 0;
		
		if(this.first < another.first) score++;
		if(this.second < another.second) score++;
		if(this.third < another.third) score++;
		
		if(score < 3 && score > 0) {
			if(this.first == another.first) score++;
			if(this.second == another.second) score++;
			if(this.third == another.third) score++;
		}
		
		if(score == 3) result = true;
		
		return result;
	}

}
