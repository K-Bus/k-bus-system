import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import DTO.BusRoute;
import DTO.Reservation;
import DTO.Schedule;
import DTO.Seat;
import Service.FileIOService;
import Service.ReservationService;
import Service.SearchService;

public class Main {
    public static void main(String[] args) throws IOException {
    	
        Scanner sc= new Scanner(System.in);
        FileIOService fs = new FileIOService();
    
        // init (DTO)
        Map<Integer, String> cities = fs.readCities();
        List<Schedule> scheduleList = fs.readScehdule();
        List<Reservation> reservationList = fs.readReservation();
        List<Seat> seatList = null;

        // init (Service)
        SearchService searchService = new SearchService();
        ReservationService reservationService = new ReservationService(reservationList, fs);

        
        while (true) {
        	
            // 버스 노선 조회 및 선택
        	
            System.out.print("날짜 입력 (yyyy-mm-dd) >> ");
            String date = sc.nextLine();

            System.out.print("출발지 >> ");
            int departure = Integer.parseInt(sc.nextLine());

            System.out.print("목적지 >> ");
            int destination = Integer.parseInt(sc.nextLine());

            List<Schedule> scheduleListByDate = searchService.findScheduleListByDate(departure, destination, scheduleList);
            for (Schedule schedule : scheduleListByDate) {
                System.out.println(schedule.getDepartureTime() + ":00");
            }
            
            System.out.print("예약을 원하는 시간을 입력해주세요 (00, 01, 02 ... ) >> ");
            String departureTime = sc.nextLine();

            int busId = searchService.findBusId(departure, destination, departureTime, scheduleList);
            
            
            // 버스 좌석 조회
            
            Seat seats = searchService.findSeats(date, busId, seatList);

            for (int i = 0; i < 30; i++) {
            	if (seats.isBooked(i)) System.out.println("[X]");
            	else System.out.println("[ ]");
            }
            
            System.out.print("원하는 좌석 번호를 입력해주세요. (1~30) >> ");
            int selectedSeatNumber= Integer.parseInt(sc.nextLine());

            System.out.print("이메일 입력 >> ");
            String userId = sc.nextLine();

            reservationService.makeReservation(userId, date, busId, selectedSeatNumber);
        }
    }
}