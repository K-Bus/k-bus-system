package Service;

import DTO.Reservation;
import DTO.BusRoute;
import java.util.List;    
import java.util.stream.Collectors;

public class ReservationService {
	
	private List<Reservation> reservationList;
	private FileIOService fileIOService;
	
	public ReservationService(List<Reservation> reservationList, FileIOService fileIOService){
		this.reservationList=reservationList;
		this.fileIOService=fileIOService;
	}
	
	public void makeReservation(String userId, BusRoute busRoute) {
		Reservation reservation = new Reservation(userId, busRoute.getBusRouteId());
		reservationList.add(reservation);
		fileIOService.writeReservation(reservationList);
		
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
		fileIOService.writeReservation(reservationList);
		
		System.out.println("예약을 취소했습니다.");
		printReservationDetail(reservation);
		System.out.println();
	}
	
	public void printReservationDetail(Reservation reservation) {
		// TODO: 예약 내역 출력 구현
		System.out.printf("[%s->%s] %s월 %s일 %s %d번 좌석\n", "대구", "부산", 0,0,0,0);
	}
}
