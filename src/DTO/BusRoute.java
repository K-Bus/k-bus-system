package DTO;

public class BusRoute {

    private int busRouteId;
    private int depature;
    private int destination;
    private Seat seat;
    private String depatureTime;

    public BusRoute(int busRouteId, int depature, int destination, Seat seat, String depatureTime) {
        this.busRouteId = busRouteId;
        this.depature = depature;
        this.destination = destination;
        this.seat = seat;
        this.depatureTime = depatureTime;
    }
}
