package model;

import java.io.Serializable;

public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String userId;
    private int busRouteId;
    private String date;
    private int seatNumber;

    public Reservation() {}

    public Reservation(int id, String userId, int busRouteId, String date, int seatNumber) {
        this.id = id;
        this.userId = userId;
        this.busRouteId = busRouteId;
        this.date = date;
        this.seatNumber = seatNumber;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public int getBusRouteId() { return busRouteId; }
    public void setBusRouteId(int busRouteId) { this.busRouteId = busRouteId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getSeatNumber() { return seatNumber; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }

    @Override
    public String toString() {
        return "Reservation{id=" + id
                + ", userId=" + userId
                + ", busRouteId=" + busRouteId
                + ", seatNo=" + seatNumber
                + ", date=" + date + "}";
    }
}
