package DTO;

public class Schedule {
	private int busId;
	private int departure;
	private String departureTime;
	private int destination;
	
	public Schedule(int departure, int destination, String departureTime, int busId) {
		super();
		this.busId = busId;
		this.departure = departure;
		this.departureTime = departureTime;
		this.destination = destination;
	}
	public int getBusRouteId() {
		return busId;
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
