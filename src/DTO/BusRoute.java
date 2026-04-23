package DTO;

public class BusRoute {

    private int busRouteId;
    private int depature;
    private int destination;
    private Seat seat;
    private String depatureTime;

<<<<<<< Updated upstream
    public BusRoute(int busRouteId, int depature, int destination, Seat seat, String depatureTime) {
        this.busRouteId = busRouteId;
        this.depature = depature;
        this.destination = destination;
        this.seat = seat;
        this.depatureTime = depatureTime;
=======
    public BusRoute(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
        seat = new Seat[scheduleList.size()];
        for (int i = 0; i < seat.length; i++) {
            seat[i] = new Seat("2026-04-23", 1);
        }
>>>>>>> Stashed changes
    }

    public int getBusRouteId() {
        return busRouteId;
    }

    public void setBusRouteId(int busRouteId) {
        this.busRouteId = busRouteId;
    }

    public int getDepature() {
        return depature;
    }

    public void setDepature(int depature) {
        this.depature = depature;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public String getDepatureTime() {
        return depatureTime;
    }

    public void setDepatureTime(String depatureTime) {
        this.depatureTime = depatureTime;
    }

    @Override
    public String toString() {
        return "BusRoute{" +
                "busRouteId=" + busRouteId +
                ", depature=" + depature +
                ", destination=" + destination +
                ", seat=" + seat +
                ", depatureTime='" + depatureTime + '\'' +
                '}';
    }
}
