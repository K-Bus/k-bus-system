package DTO;

import java.util.List;

public class BusRoute {

    private final List<Schedule> scheduleList;
    private Seat[] seat;

    public BusRoute(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
        seat = new Seat[scheduleList.size()];
        for (int i = 0; i < seat.length; i++) {
            seat[i] = new Seat("2026-04-23", 1);
        }
    }

    public Seat[] getSeat() {
        return seat;
    }

    public List<Schedule> getScheduleList() {
        return scheduleList;
    }
}
