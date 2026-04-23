package DTO;

public class Reservation {
	private String userId;
	private int busRouteId;
	
	public Reservation() {}
	
	public Reservation(String userId, int busRouteId) {
        this.userId = userId;
        this.busRouteId = busRouteId;
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
}
