package DTO;

import java.io.Serializable;

public class Seat implements Serializable {
    //false: 빈 좌석, true: 예약됨
    private boolean[] seatStatus;
    private String date;
    private int busRouteId;

    public Seat(String date, int busRouteId) {
        this.seatStatus = new boolean[30];
        this.date = date;
    }

    public boolean[] getSeatStatus() {
        return seatStatus;
    }

    // 특정 좌석의 예약 여부 확인
    public boolean isBooked(int seatNumber) {
        return seatStatus[seatNumber - 1];
    }

    public void setBooked(int seatNumber, boolean status) {
        this.seatStatus[seatNumber - 1] = status;
    }
}