package DTO;

import java.io.Serializable;
import java.util.Arrays;

public class Seat implements Serializable {
    private boolean[] seatStatus;
    private String date;
    private int busId;
    
	public Seat(String date, int busId) {
        this.date = date;
        this.busId = busId;
        this.seatStatus = new boolean[30];
    }

	public String getDate() {
		return date;
	}

	public int getBusId() {
		return busId;
	}

    public boolean isBooked(int seatNumber) {
        return seatStatus[seatNumber];
    }

    public void setBooked(int seatNumber, boolean status) {
        this.seatStatus[seatNumber] = true;
    }
    
    public void cancelBooked(int seatNumber) {
        this.seatStatus[seatNumber] = false;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatStatus=" + Arrays.toString(seatStatus) +
                '}';
    }
}