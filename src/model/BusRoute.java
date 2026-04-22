package model;

import java.io.Serializable;
import java.util.Arrays;

public class BusRoute implements Serializable {
    private static final long serialVersionUID = 1L;

    private int busRouteId;
    private int departure;
    private int destination;
    private Seat[] seats;
    private String departureTime;

    public BusRoute() {}

    public BusRoute(int busRouteId, int departure, int destination,
                    Seat[] seats, String departureTime) {
        this.busRouteId = busRouteId;
        this.departure = departure;
        this.destination = destination;
        this.seats = seats;
        this.departureTime = departureTime;
    }

    public int getBusRouteId() { return busRouteId; }
    public void setBusRouteId(int busRouteId) { this.busRouteId = busRouteId; }

    public int getDeparture() { return departure; }
    public void setDeparture(int departure) { this.departure = departure; }

    public int getDestination() { return destination; }
    public void setDestination(int destination) { this.destination = destination; }

    public Seat[] getSeats() { return seats; }
    public void setSeats(Seat[] seats) { this.seats = seats; }

    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    @Override
    public String toString() {
        return "BusRoute{id=" + busRouteId
                + ", " + departure + "->" + destination
                + ", time=" + departureTime
                + ", seats=" + (seats == null ? 0 : seats.length) + "}";
    }
}
