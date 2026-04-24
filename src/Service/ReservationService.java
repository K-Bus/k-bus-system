package Service;

import DTO.Reservation;
import DTO.Seat;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationService implements Serializable {
	
	private List<Reservation> reservationList;
	private FileIOService fileIOService;
	
	public ReservationService(List<Reservation> reservationList, FileIOService fileIOService){
		this.reservationList=reservationList;
		this.fileIOService=fileIOService;
	}
	
	public void makeReservation(String userId, String date, int busRouteId, int seatNumber) {

        Reservation reservation = new Reservation(userId, date, busRouteId, seatNumber);

        // 현재 들고있는 메모리에 추가하고
        reservationList.add(reservation);

        // 파일 저장
        fileIOService.writeReservation(reservation);
	}

	public void checkReservation(String userId) {
		List<Reservation> result = reservationList.stream()
								.filter(r->r.getUserId().equals(userId))
								.collect(Collectors.toList());
        if (result.isEmpty()) {
            System.out.println("예약 내역이 없습니다.");
            return;
        }

		System.out.println("예약 정보입니다.");
		for (Reservation reservation : result) {
			printReservationDetail(reservation);
		}
	}

    public void cancelReservation(Reservation reservation, List<Seat> seatList) {
        // 1. 예약 삭제
        reservationList.remove(reservation);
        // 2. Seat 반영 (좌석 다시 열기)
        for (Seat seat : seatList) {
            if (seat.getDate().equals(reservation.getDate())
                    && seat.getBusId() == reservation.getBusRouteId()) {

                seat.setBooked(reservation.getSeatNumber(), false);
                break;
            }
        }
        // 3. 파일 저장
        fileIOService.writeReservations(reservationList);

        System.out.println("예약을 취소했습니다.");
        printReservationDetail(reservation);
    }
	
	public void printReservationDetail(Reservation reservation) {
        System.out.printf("[%s] %s %d번 버스 %d번 좌석\n", "버스", reservation.getDate(), reservation.getBusRouteId(), reservation.getSeatNumber());
	}

    public List<Reservation> findByUserId(String userId) {
        return reservationList.stream()
                .filter(r -> r.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}
