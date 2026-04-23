package DTO;

public class Schedule {
	private int busRouteId;
	private int departure;
	private int destination;
	private String departureTime;
	public Schedule(int busRouteId, int departure, int destination, String departureTime) {
		super();
		this.busRouteId = busRouteId;
		this.departure = departure;
		this.destination = destination;
		this.departureTime = departureTime;
	}
	public int getBusRouteId() {
		return busRouteId;
	}
	public int getDeparture() {
		return departure;
	}
	public int getDestination() {
		return destination;
	}
	public String getDepartureTime() {
		return departureTime;
	}
	
	
}
