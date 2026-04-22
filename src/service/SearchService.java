package service;

import model.BusRoute;
import model.Seat;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchService {

    private final List<BusRoute> routes;
    private final Map<Integer, Seat[]> seatsMap;

    public SearchService(List<BusRoute> routes, Map<Integer, Seat[]> seatsMap) {
        this.routes = routes;
        this.seatsMap = seatsMap;
    }

    public List<BusRoute> searchBusRoute(int departure, int destination) {
        return routes.stream()
                .filter(r -> r.getDeparture() == departure)
                .filter(r -> r.getDestination() == destination)
                .collect(Collectors.toList());
    }

    public List<BusRoute> searchBusRoute(int departure, int destination, String time) {
        return routes.stream()
                .filter(r -> r.getDeparture() == departure)
                .filter(r -> r.getDestination() == destination)
                .filter(r -> r.getDepartureTime().equals(time))
                .collect(Collectors.toList());
    }

    public Seat[] checkSeats(int busRouteId) {
        return seatsMap.get(busRouteId);
    }
}
