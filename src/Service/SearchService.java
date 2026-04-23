package Service;

import DTO.BusRoute;
import DTO.Schedule;
import DTO.Seat;

import java.util.ArrayList;
import java.util.List;

public class SearchService {

    public List<String> searchBusRoute(int departure, int destination, List<Schedule> scheduleList) {
        List<String> findBusRoute = new ArrayList<String>();

        for (int i = 0; i < scheduleList.size(); i++) {
            if (departure == scheduleList.get(i).getDeparture() && destination == scheduleList.get(i).getDestination()) {
                findBusRoute.add(scheduleList.get(i).getDepartureTime() + ":00");
            }
        }

        return findBusRoute;
    }

    public Seat searchBusRouteSeat(int busRouteId, BusRoute busRoute) {

        Seat[] seat = busRoute.getSeat();
        return seat[busRouteId];
    }

    public int findBusRouteId(int departure, int destination, String select, BusRoute busRoute) {

        Seat[] seat = busRoute.getSeat();
        for (int i = 0; i < busRoute.getScheduleList().size(); i++) {
            if (departure == busRoute.getScheduleList().get(i).getDeparture()
                    && destination == busRoute.getScheduleList().get(i).getDestination()
                    && select.equals(busRoute.getScheduleList().get(i).getDepartureTime())) {
                return i;
            }
        }
        return 0;
    }
}
