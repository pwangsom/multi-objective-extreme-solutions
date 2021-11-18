package com.kmutt.sit.cloud;

import java.util.ArrayList;
import java.util.List;

public class CloudEventObserver {
	
	private static CloudEventObserver instance = new CloudEventObserver();
	
	private List<CloudEvent> eventList = new ArrayList<CloudEvent>();
	
	private int eventId = 0;
	
	private CloudEventObserver() {}
	
	public static CloudEventObserver getCloudEventObserver() {
		return instance;
	}

	public void addCloudEvent(CloudEvent event) {
		// TODO Auto-generated method stub
		this.eventId++;
		event.setEventId(eventId);
		this.eventList.add(event);
	}

	public List<CloudEvent> getCloudEventList() {
		// TODO Auto-generated method stub
		return this.eventList;
	}
}
