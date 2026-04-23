package DTO;

import java.util.List;

public class BusRoute {

    private List<Schedule> scheduleList;
    private Seat[] seat;

    public BusRoute(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
        seat = new Seat[scheduleList.size()];
        for (int i = 0; i < seat.length; i++) {
            seat[i] = new Seat();
        }
    }

    public List<Schedule> getScheduleList() {
        return scheduleList;
    }

    public Seat[] getSeat() {
        return seat;
    }

    public void setSeat(Seat[] seat) {
        this.seat = seat;
    }
}
