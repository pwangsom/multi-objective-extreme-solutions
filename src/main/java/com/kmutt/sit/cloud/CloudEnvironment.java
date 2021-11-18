package com.kmutt.sit.cloud;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CloudEnvironment {

	final private int noOfVm;
	private List<CloudVm> vmList;
	
	public CloudEnvironment(int vms) {
		this.noOfVm = vms;
	}
	
	public void startVms() {
		setVmList();		
	}
	
	public void stopVms(Double requestTime) {
		vmList.stream().forEach(vm -> {
			vm.stop(requestTime);
		});
	}
	
	public List<CloudVm> getAvailableVmList(double requestTime){		
		List<CloudVm> result = vmList.stream().filter(vm -> vm.isAvailable(requestTime) == true).collect(Collectors.toList());
		
		return result;		
	}
	
	public int getNoOfVm() {
		return noOfVm;
	}
		
	public Double getNextAvailableTime() {		
		Comparator<CloudVm> comparator = Comparator.comparing(CloudVm::getNextAvailableTime);
		CloudVm nextVm = vmList.stream().min(comparator).get();

		return nextVm.getNextAvailableTime();
	}
	
	public Double getLastNextAvailableTime() {		
		Comparator<CloudVm> comparator = Comparator.comparing(CloudVm::getNextAvailableTime);
		CloudVm nextVm = vmList.stream().max(comparator).get();

		return nextVm.getNextAvailableTime();
	}
	
	public List<CloudEvent> getCloudEventListSortedByTimeThenEventId(){
		Comparator<CloudEvent> comparator = Comparator.comparing(CloudEvent::getStartEvent);
		comparator = comparator.thenComparing(Comparator.comparing(CloudEvent::getEventId));
		
		return CloudEventObserver.getCloudEventObserver().getCloudEventList()
				.stream().sorted(comparator).collect(Collectors.toList());
	}
	
	
	public Double getCompletionTime() {
		CloudEvent last = CloudEventObserver.getCloudEventObserver().getCloudEventList()
				.stream().max(Comparator.comparing(CloudEvent::getFinishEvent)).get();
		
		return last.getFinishEvent();
	}
	
	public Double getVmRunningTime() {
		return CloudEventObserver.getCloudEventObserver().getCloudEventList()
				.stream().filter(ev -> ev.getType() == CloudEvent.EventType.PROCESS_TASK)
				.collect(Collectors.toList())
				.stream().map(ev -> ev.getEventTime())
				// .collect(Collectors.toList()).stream()
				.mapToDouble(Double::doubleValue).sum();
	}
	
	public Double getTaskWaitingTime() {
		return CloudEventObserver.getCloudEventObserver().getCloudEventList()
			.stream().filter(ev -> ev.getType() == CloudEvent.EventType.TASK_WAITING)
			.collect(Collectors.toList())
			.stream().map(ev -> ev.getEventTime())
			// .collect(Collectors.toList()).stream()
			.mapToDouble(Double::doubleValue).sum();
	}
	
	public Double getVmIdleTime() {
		return CloudEventObserver.getCloudEventObserver().getCloudEventList()
			.stream().filter(ev -> ev.getType() == CloudEvent.EventType.VM_IDLE)
			.collect(Collectors.toList())
			.stream().map(ev -> ev.getEventTime())
			// .collect(Collectors.toList()).stream()
			.mapToDouble(Double::doubleValue).sum();
	}
	
	public List<CloudVmAction> getCloudVmActionListSortedByActionTime(){
		Comparator<CloudVmAction> comparator = Comparator.comparing(CloudVmAction::getActionTime);
		comparator = comparator.thenComparing(Comparator.comparing(CloudVmAction::getVmId));
		
		List<CloudVmAction> result = CloudVmActionObserver.getCloudVmActionObserver().getCloudVmStartActionList();
		result.addAll(CloudVmActionObserver.getCloudVmActionObserver().getCloudVmStopActionList());
		
		return result.stream().sorted(comparator).collect(Collectors.toList());
	}
	
	public List<CloudVmAction> getCloudVmActionList(){		
		List<CloudVmAction> result = CloudVmActionObserver.getCloudVmActionObserver().getCloudVmStartActionList();
		result.addAll(CloudVmActionObserver.getCloudVmActionObserver().getCloudVmStopActionList());
		
		return result;
	}
	
	public List<CloudVm> getVmList(){
		return this.vmList;
	}
	
	private void setVmList() {		
		vmList = new ArrayList<CloudVm>();
		
		for(int i = 0; i < this.noOfVm; i++) {
			CloudVm vm = new CloudVm();
			vm.setVmId(i);
			vm.setNextAvailableTime(0.00);
			vm.start(0.00);
			vmList.add(vm);
		}
	}	
	
	/*==============================================================================================*/

	
/*	
 * public List<CloudEvent> getListofAllCloudEventSortedByTime(){
		Comparator<CloudEvent> comparator = Comparator.comparing(CloudEvent::getStartEvent);
		comparator = comparator.thenComparing(Comparator.comparing(CloudEvent::getEventId));
		
		List<CloudEvent> result = this.vmList.stream().flatMap(vm -> vm.getEventList().stream())
				.collect(Collectors.toList());
		
		return result.stream().sorted(comparator).collect(Collectors.toList());
	}

	
	public Double getCompletionTime() {
		List<CloudEvent> list = this.vmList.stream().flatMap(vm -> vm.getEventList().stream())
				.collect(Collectors.toList());
		CloudEvent last = list.stream().max(Comparator.comparing(CloudEvent::getFinishEvent)).get();
		return last.getFinishEvent();
	}
	
	public Double getVmRunningTime() {
		return this.vmList.stream().flatMap(vm -> vm.getEventList().stream()
				.filter(ev -> ev.getType() == CloudEvent.EventType.PROCESS_TASK))
				.collect(Collectors.toList())
				.stream().map(ev -> ev.getEventTime())
				.collect(Collectors.toList()).stream()
				.mapToDouble(Double::doubleValue).sum();
	}
	
	public Double getVmIdleTime() {
		return this.vmList.stream().flatMap(vm -> vm.getEventList().stream()
				.filter(ev -> ev.getType() == CloudEvent.EventType.IDLE_TASK))
				.collect(Collectors.toList())
				.stream().map(ev -> ev.getEventTime())
				.collect(Collectors.toList()).stream()
				.mapToDouble(Double::doubleValue).sum();
	}
*/
	
	
}
