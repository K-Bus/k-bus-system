package Service;

import DTO.Schedule;
import DTO.Seat;

import java.util.ArrayList;
import java.util.List;

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
    
    public int findBusId(int departure, int destination, String departureTime, List<Schedule> scheduleList) {
        for (Schedule schedule : scheduleList) {
            if (schedule.getDeparture() == departure
                    && schedule.getDestination() == destination
                    && schedule.getDepartureTime().equals(departureTime)) {
                return schedule.getBusId();
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
