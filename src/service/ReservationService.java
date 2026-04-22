package service;

import model.BusRoute;
import model.Reservation;
import model.Seat;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReservationService {

    private final FileIOService fileIO;
    private final Map<Integer, Seat[]> seatsMap;
    private final List<Reservation> reservations;
    private int nextId;

    public ReservationService(FileIOService fileIO,
                              Map<Integer, Seat[]> seatsMap,
                              List<Reservation> reservations) {
        this.fileIO = fileIO;
        this.seatsMap = seatsMap;
        this.reservations = reservations;
        this.nextId = reservations.stream()
                .mapToInt(Reservation::getId)
                .max()
                .orElse(0) + 1;
    }

    public Reservation makeReservation(String userId, BusRoute busRoute, int seatNo) {
        Seat[] seats = seatsMap.get(busRoute.getBusRouteId());
        if (seats == null) {
            throw new IllegalStateException("좌석 정보가 없습니다.");
        }
        if (seatNo < 1 || seatNo > seats.length) {
            throw new IllegalArgumentException("좌석 번호 범위 오류: " + seatNo);
        }
        if (seats[seatNo - 1].isSeat()) {
            throw new IllegalStateException("이미 예약된 좌석입니다.");
        }

        seats[seatNo - 1].setSeat(true);
        Reservation r = new Reservation(
                nextId++,
                userId,
                busRoute.getBusRouteId(),
                LocalDate.now().toString(),
                seatNo
        );
        reservations.add(r);

        fileIO.writeSeats(seatsMap);
        fileIO.writeReservation(reservations);
        return r;
    }

    public List<Reservation> checkReservation(String userId) {
        return reservations.stream()
                .filter(r -> r.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public boolean cancelReservation(Reservation reservation) {
        boolean removed = reservations.removeIf(r -> r.getId() == reservation.getId());
        if (!removed) return false;

        Seat[] seats = seatsMap.get(reservation.getBusRouteId());
        if (seats != null) {
            int idx = reservation.getSeatNumber() - 1;
            if (idx >= 0 && idx < seats.length) {
                seats[idx].setSeat(false);
            }
        }

        fileIO.writeSeats(seatsMap);
        fileIO.writeReservation(reservations);
        return true;
    }
}
