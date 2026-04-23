package DTO;

import java.util.Arrays;

public class Seat {
    //false: 빈 좌석, true: 예약됨
    private boolean[] seatStatus;

    public Seat() {
        this.seatStatus = new boolean[30];
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

    @Override
    public String toString() {
        return "Seat{" +
                "seatStatus=" + Arrays.toString(seatStatus) +
                '}';
    }
}