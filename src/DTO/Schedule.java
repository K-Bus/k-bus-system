package DTO;

public class Schedule {

    private int departure;
    private int destination;
    private String departureTime;
    private int busRouteId;

	public Schedule(int departure, int destination, String departureTime, int busRouteId) {
		super();
        this.departure = departure;
        this.destination = destination;
		this.departureTime = departureTime;
        this.busRouteId = busRouteId;

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

    @Override
    public String toString() {
        return "Schedule{" +
                "departure=" + departure +
                ", destination=" + destination +
                ", departureTime='" + departureTime + '\'' +
                ", busRouteId=" + busRouteId +
                '}';
    }
}
