import model.BusRoute;
import model.Reservation;
import model.Seat;
import service.FileIOService;
import service.ReservationService;
import service.SearchService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    private static final int SEATS_PER_BUS = 28;
    private static final Pattern USER_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9]{2,10}$");

    enum MenuOption {
        RESERVE, CHECK, EXIT;

        static MenuOption of(int i) {
            switch (i) {
                case 1: return RESERVE;
                case 2: return CHECK;
                case 3: return EXIT;
                default: throw new IllegalArgumentException("메뉴 번호 오류: " + i);
            }
        }
    }

    private static FileIOService fileIO;
    private static Map<Integer, String> cities;
    private static List<BusRoute> routes;
    private static Map<Integer, Seat[]> seatsMap;
    private static List<Reservation> reservations;
    private static SearchService searchService;
    private static ReservationService reservationService;
    private static Scanner sc;

    public static void main(String[] args) {
        init();
        runMenuLoop();
        System.out.println("프로그램을 종료합니다.");
    }

    private static void init() {
        fileIO = new FileIOService();
        cities = fileIO.readCities();
        routes = fileIO.readSchedule();
        seatsMap = fileIO.readSeats();
        reservations = fileIO.readReservation();

        // 좌석 정보 없는 노선은 초기화
        boolean seatsInitialized = false;
        for (BusRoute r : routes) {
            if (!seatsMap.containsKey(r.getBusRouteId())) {
                Seat[] s = new Seat[SEATS_PER_BUS];
                for (int i = 0; i < s.length; i++) s[i] = new Seat(false);
                seatsMap.put(r.getBusRouteId(), s);
                seatsInitialized = true;
            }
            r.setSeats(seatsMap.get(r.getBusRouteId()));
        }
        if (seatsInitialized) fileIO.writeSeats(seatsMap);

        searchService = new SearchService(routes, seatsMap);
        reservationService = new ReservationService(fileIO, seatsMap, reservations);
        sc = new Scanner(System.in);

        System.out.println("=== K-Bus 예매 시스템 ===");
        System.out.println("도시: " + cities.size() + "개, 노선: " + routes.size() + "건, 예약: " + reservations.size() + "건");
    }

    private static void runMenuLoop() {
        while (true) {
            System.out.println();
            System.out.println("1. 예약하기");
            System.out.println("2. 예약확인");
            System.out.println("3. 종료");
            System.out.print("> ");
            try {
                MenuOption opt = MenuOption.of(Integer.parseInt(sc.nextLine().trim()));
                switch (opt) {
                    case RESERVE: doReserve(); break;
                    case CHECK:   doCheck();   break;
                    case EXIT:    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력하세요.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (IllegalStateException e) {
                System.out.println("오류: " + e.getMessage());
            }
        }
    }

    private static void doReserve() {
        String userId = inputUserId();
        printCities();

        int dep = inputInt("출발지 번호: ");
        int dst = inputInt("도착지 번호: ");
        if (!cities.containsKey(dep) || !cities.containsKey(dst)) {
            System.out.println("존재하지 않는 도시입니다.");
            return;
        }

        List<BusRoute> found = searchService.searchBusRoute(dep, dst);
        if (found.isEmpty()) {
            System.out.println("해당 노선이 없습니다.");
            return;
        }
        System.out.println("--- 노선 목록 ---");
        found.forEach(r -> System.out.println(
                " [" + r.getBusRouteId() + "] " + cities.get(r.getDeparture())
                        + " -> " + cities.get(r.getDestination())
                        + " / " + r.getDepartureTime()));

        int rid = inputInt("예매할 노선 번호: ");
        BusRoute target = found.stream().filter(r -> r.getBusRouteId() == rid)
                .findFirst().orElse(null);
        if (target == null) {
            System.out.println("목록에 없는 노선 번호입니다.");
            return;
        }

        printSeats(target);
        int seatNo = inputInt("좌석 번호 (1-" + SEATS_PER_BUS + "): ");
        Reservation r = reservationService.makeReservation(userId, target, seatNo);
        System.out.println("예약 완료: " + r);
    }

    private static void doCheck() {
        String userId = inputUserId();
        List<Reservation> mine = reservationService.checkReservation(userId);
        if (mine.isEmpty()) {
            System.out.println("예약 내역이 없습니다.");
            return;
        }
        System.out.println("--- 내 예약 내역 ---");
        mine.forEach(r -> System.out.println(" " + r));

        System.out.print("취소할 예약 id (0 = 취소 안 함): ");
        int cid;
        try {
            cid = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("숫자 아님, 취소 안 함.");
            return;
        }
        if (cid == 0) return;

        Reservation toCancel = mine.stream().filter(r -> r.getId() == cid)
                .findFirst().orElse(null);
        if (toCancel == null) {
            System.out.println("해당 예약이 없습니다.");
            return;
        }
        boolean ok = reservationService.cancelReservation(toCancel);
        System.out.println(ok ? "취소 완료." : "취소 실패.");
    }

    private static String inputUserId() {
        while (true) {
            System.out.print("사용자 ID (영문/숫자 2~10자): ");
            String id = sc.nextLine().trim();
            if (USER_ID_PATTERN.matcher(id).matches()) return id;
            System.out.println("올바른 ID 형식이 아닙니다.");
        }
    }

    private static int inputInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력하세요.");
            }
        }
    }

    private static void printCities() {
        System.out.println("--- 도시 목록 ---");
        cities.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> System.out.println(" " + e.getKey() + " : " + e.getValue()));
    }

    private static void printSeats(BusRoute r) {
        Seat[] s = searchService.checkSeats(r.getBusRouteId());
        System.out.println("--- 좌석 현황 (X = 예약됨) ---");
        for (int i = 0; i < s.length; i++) {
            System.out.printf("%2d%s ", (i + 1), s[i].isSeat() ? "X" : ".");
            if ((i + 1) % 7 == 0) System.out.println();
        }
        System.out.println();
    }
}
