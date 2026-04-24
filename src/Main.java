import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
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
        List<Seat> seatList = fs.readSeats();

        // init (Service)
        SearchService searchService = new SearchService();
        ReservationService reservationService = new ReservationService(reservationList, fs);

        
        while (true) {
            System.out.println("\n===== K-BUS =====");

            System.out.println("1. 예약");
            System.out.println("2. 예약조회");
            System.out.println("3. 예약취소");
            System.out.println("4. 종료");
            System.out.print("선택 >> ");

            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    System.out.print("날짜 입력 (yyyy-mm-dd) >> ");
                    String date = sc.nextLine();

                    System.out.println(cities.toString());
                    System.out.print("출발지 번호 >> ");
                    int departure = Integer.parseInt(sc.nextLine());

                    System.out.print("목적지 번호 >> ");
                    int destination = Integer.parseInt(sc.nextLine());

                    List<Schedule> scheduleListByDate = searchService.findScheduleListByDate(departure, destination, scheduleList);
                    for (Schedule schedule : scheduleListByDate) {
                        System.out.println(schedule.getDepartureTime() + ":00");
                    }

                    System.out.print("예약을 원하는 시간을 입력해주세요 (00, 01, 02 ... ) >> ");
                    String departureTime = sc.nextLine();

                    int busId = searchService.findBusId(departure, destination, departureTime, scheduleList);
                    if (busId == -1) {
                        System.out.println("잘못된 시간 선택입니다.");
                        break;
                    }

                    Seat seats = searchService.findSeats(date, busId, seatList);
                    if (seats == null) {
                        seats = new Seat(date, busId);
                        seatList.add(seats);
                    }

                    for (int i = 0; i < 30; i++) {
                        if (seats.isBooked(i)) System.out.print("[X]");
                        else System.out.print("[ ]");
                    }
                    System.out.println();

                    int seatNum;
                    while (true) {
                        System.out.print("원하는 좌석 번호를 입력해주세요. (0~29, 취소: -1) >> ");
                        String input = sc.nextLine().trim();

                        try {
                            seatNum = Integer.parseInt(input);
                        } catch (NumberFormatException e) {
                            System.out.println("숫자만 입력해주세요.");
                            continue;
                        }

                        if (seatNum == -1) {
                            System.out.println("예약을 취소합니다.");
                            break;
                        }
                        if (seatNum < 0 || seatNum > 29) {
                            System.out.println("좌석 번호는 0~29 사이여야 합니다.");
                            continue;
                        }
                        if (seats.isBooked(seatNum)) {
                            System.out.println("이미 예약된 좌석입니다.");
                            continue;
                        }

                        seats.setBooked(seatNum, true);
                        break;
                    }

                    if (seatNum == -1) break;

                    String userId;
                    while (true) {
                        System.out.print("이메일 입력 >> ");
                        userId = sc.nextLine();

                        if (userId.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) break;
                        else System.out.println("올바른 이메일 형식이 아닙니다.");
                    }
                    fs.writeSeats(seatList);
                    reservationService.makeReservation(userId, date, busId, seatNum);

                    System.out.println("예약 완료!");
                    break;

                case "2":
                    System.out.print("조회할 이메일 입력 >> ");
                    String searchUser = sc.nextLine();

                    reservationService.checkReservation(searchUser);
                    break;
                case "3":
                    System.out.print("조회할 이메일 입력 >> ");
                    String cancelUser = sc.nextLine();
                    List<Reservation> result = reservationService.findByUserId(cancelUser);
                    if (result.isEmpty()) {
                        System.out.println("예약 내역이 없습니다.");
                        break;
                    }

                    // 목록 출력
                    for (int i = 0; i < result.size(); i++) {
                        System.out.println((i + 1) + ". " + result.get(i));
                    }

                    System.out.print("취소할 번호 선택 >> ");
                    int idx = Integer.parseInt(sc.nextLine());
                    Reservation target = result.get(idx - 1);
                    reservationService.cancelReservation(target, seatList);
                    break;
                case "4":
                    System.out.println("프로그램 종료");
                    return;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }
}