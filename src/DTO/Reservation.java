package DTO;

public class Reservation {
	private int id;
	private String userId;
	private int busRouteId;
	private String date;
	
	public Reservation() {}
	
	private Reservation(int id, String userId, int busRouteId, String date) {
		this.id = id;
        this.userId = userId;
        this.busRouteId = busRouteId;
        this.date = date;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getBusRouteId() {
		return busRouteId;
	}

	public void setBusRouteId(int busRouteId) {
		this.busRouteId = busRouteId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
