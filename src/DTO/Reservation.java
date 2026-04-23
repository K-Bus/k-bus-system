package DTO;

public class Reservation {
	private String userId;
	private String date;
	private int busRouteId;
	private int seatNumber;
	
	public Reservation() {}
	
	public Reservation(String userId, String date, int busRouteId, int seatNumber) {
        this.userId = userId;
        this.date = date;
        this.busRouteId = busRouteId;
        this.seatNumber = seatNumber;
    }

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getBusRouteId() {
		return busRouteId;
	}

	public void setBusRouteId(int busRouteId) {
		this.busRouteId = busRouteId;
	}
	
	public int getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}

}
