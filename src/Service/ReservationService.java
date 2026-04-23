package Service;

import DTO.Reservation;
import DTO.BusRoute;
import DTO.Seat;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationService {
	
	private List<Reservation> reservationList;
	private FileIOService fileIOService;
	
	public ReservationService(List<Reservation> reservationList, FileIOService fileIOService){
		this.reservationList=reservationList;
		this.fileIOService=fileIOService;
	}
	
	public void makeReservation(String userId, String date, int busRouteId, int selectedSeatNumber) {
		
		// 에약이 안되는 경우		
        try {
			if (searchBusRouteSeat(date, busRouteId).isBooked(selectedSeatNumber)) {
				System.out.println("이미 예약된 좌석입니다.");
				return;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 예약 생성하기
        
		SearchService.searchBusRouteSeat(date, busRouteId, selectedSeatNumber);
		Reservation reservation = new Reservation(userId, date, busRouteId, selectedSeatNumber);
		reservationList.add(reservation);
        // fileIOService.writeReservation(reservationList);
		
		System.out.println(userId + "님 예약 완료되었습니다.");
		printReservationDetail(reservation);
		System.out.println();
	    
	}

	public void checkReservation(String userId) {
		List<Reservation> result = reservationList.stream()
								.filter(r->r.getUserId().equals(userId))
								.collect(Collectors.toList());
		
		System.out.println("예약 정보입니다.");
		for (Reservation reservation : result) {
			printReservationDetail(reservation);
		}		
		System.out.println();
	}
	
	public void cancelReservation(Reservation reservation) {
		reservationList.remove(reservation);
		// fileIOService.writeReservation(reservationList);
		
		System.out.println("예약을 취소했습니다.");
		printReservationDetail(reservation);
		System.out.println();
	}
	
	public void printReservationDetail(Reservation reservation) {
		// TODO: 예약 내역 출력 구현
		System.out.printf("[%s->%s] %s %s번 버스 %d번 좌석\n", "대구", "부산", reservation.getDate(), reservation.getBusRouteId(), reservation.getSeatNumber());
	}
}
