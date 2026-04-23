<<<<<<< Updated upstream
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

        for (int i = 1; i <= 5; i++) {
            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            System.out.println("i = " + i);
=======
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

        FileIOService fs = new FileIOService();
        /*
        Reservation(String userId, String date, int busRouteId, int seatNumber)
         */
        fs.writeReservation(new Reservation("mm1234@naver.com", "2026-04-23", 1, 1));
        fs.writeReservation(new Reservation("mn1234@naver.com", "2026-04-23", 1, 2));
        fs.writeReservation(new Reservation("mb1234@naver.com", "2026-04-23", 1, 3));
        Map<Integer, String> cities = fs.readCities();
        List<Schedule> scheduleList = fs.readScehdule();
        List<Reservation> reservationList = fs.readReservation();

        SearchService searchService = new SearchService();
        ReservationService reservationService = new ReservationService(reservationList, fs);


        // if () {
        // }
        Seat[] seatList = new Seat[scheduleList.size()];

        Scanner sc= new Scanner(System.in);

        while (true) {
            BusRoute busRoute = new BusRoute(scheduleList);
            System.out.print("날짜 입력 (yyyy-mm-dd) >> ");
            String date = sc.nextLine();

            System.out.print("출발지 >> ");
            int departure = Integer.parseInt(sc.nextLine());

            System.out.print("목적지 >> ");
            int destination = Integer.parseInt(sc.nextLine());

            // 1. ex) 서울 - 부산 해당하는 시간대가 들어있는 List
            List<String> findBusRoute = searchService.searchBusRoute(departure, destination, scheduleList);
            for (String s : findBusRoute) System.out.println(s);
            System.out.print("예약을 원하는 시간을 입력해주세요 (00, 01) >> ");
            String departureTime = sc.nextLine();

            // 2. 찾은 Seat field가 담고있어야 할 정보 (언제(date), 어느 버스(index가 사실 버스 노선번호), 좌석 예매 여부)
            int busRouteId = searchService.findBusRouteId(departure, destination, departureTime, busRoute);
            Seat seats = searchService.searchBusRouteSeat(busRouteId, busRoute);

            for (int i = 0; i < seats.getSeatStatus().length; i++) {
                if (!seats.getSeatStatus()[i]) {
                    System.out.print("[" + i+1 + "] ");
                }
                else {
                    System.out.print("[" + "X" + "] ");
                }
            }
            System.out.print("원하는 좌석 번호를 입력해주세요. (1~30) >> ");
            int selectedSeatNumber= Integer.parseInt(sc.nextLine()) - 1;

            // ---예약로직---
            System.out.print("이메일 입력 >> ");
            String userId = sc.nextLine();

            reservationService.makeReservation(userId, date, busRouteId, selectedSeatNumber);
>>>>>>> Stashed changes
        }
    }
}