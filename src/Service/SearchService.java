package Service;

import DTO.BusRoute;
import DTO.Reservation;
import DTO.Schedule;
import DTO.Seat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchService {

	public List<Schedule> findScheduleListByDate(int departure, int destination, List<Schedule> scheduleList) {
		List<Schedule> result = new ArrayList<>();
	    for (Schedule schedule : scheduleList) {
	        if (schedule.getDeparture() == departure && schedule.getDestination() == destination) {
	            result.add(schedule);
	        }
	    }
	    return result;
	}
    
    public int findBusId(int departure, int destination, String date, List<Schedule> scheduleList) {
        for (Schedule schedule : scheduleList) {
            if (schedule.getDeparture() == departure
                    && schedule.getDestination() == destination
                    && schedule.getDepartureTime().equals(date)) {
                return schedule.getBusRouteId();
            }
        }
        
        return -1;
    }
   

    public Seat findSeats(String date, int busId, List<Seat> seatList) {
        for (Seat seat : seatList) {
            if (seat.getDate().equals(date) && seat.getBusId() == busId) {
                return seat;
            }
        }
        return null;
    }


}
