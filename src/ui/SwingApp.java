package ui;

import model.BusRoute;
import model.Reservation;
import model.Seat;
import service.FileIOService;
import service.ReservationService;
import service.SearchService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class SwingApp {

    private static final int SEATS_PER_BUS = 28;
    private static final int SEAT_COLS = 7;
    private static final Pattern USER_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9]{2,10}$");

    private final FileIOService fileIO;
    private final Map<Integer, String> cities;
    private final List<BusRoute> routes;
    private final Map<Integer, Seat[]> seatsMap;
    private final List<Reservation> reservations;
    private final SearchService searchService;
    private final ReservationService reservationService;

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cards;

    private JTextField reserveIdField;
    private JComboBox<CityItem> departureCombo;
    private JComboBox<CityItem> destinationCombo;
    private DefaultListModel<BusRoute> routeListModel;
    private JList<BusRoute> routeList;
    private JPanel seatGridPanel;
    private JButton[] seatButtons;
    private JLabel selectedInfoLabel;
    private BusRoute selectedRoute;
    private Integer selectedSeat;

    private JTextField checkIdField;
    private DefaultTableModel reservationTableModel;
    private JTable reservationTable;

    public SwingApp() {
        this.fileIO = new FileIOService();
        this.cities = fileIO.readCities();
        this.routes = fileIO.readSchedule();
        this.seatsMap = fileIO.readSeats();
        this.reservations = fileIO.readReservation();
        initSeats();
        this.searchService = new SearchService(routes, seatsMap);
        this.reservationService = new ReservationService(fileIO, seatsMap, reservations);
    }

    private void initSeats() {
        boolean inited = false;
        for (BusRoute r : routes) {
            if (!seatsMap.containsKey(r.getBusRouteId())) {
                Seat[] s = new Seat[SEATS_PER_BUS];
                for (int i = 0; i < s.length; i++) s[i] = new Seat(false);
                seatsMap.put(r.getBusRouteId(), s);
                inited = true;
            }
            r.setSeats(seatsMap.get(r.getBusRouteId()));
        }
        if (inited) fileIO.writeSeats(seatsMap);
    }

    public void show() {
        frame = new JFrame("K-Bus 예매 시스템");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(820, 560);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        cards.add(buildHomePanel(), "home");
        cards.add(buildReservePanel(), "reserve");
        cards.add(buildCheckPanel(), "check");

        frame.setContentPane(cards);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel buildHomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("K-Bus 고속버스 예매 시스템", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        c.gridy = 0;
        panel.add(title, c);

        JLabel info = new JLabel("도시 " + cities.size() + "개 · 노선 " + routes.size() + "건 · 예약 " + reservations.size() + "건",
                SwingConstants.CENTER);
        c.gridy = 1;
        panel.add(info, c);

        JButton reserveBtn = new JButton("예약하기");
        reserveBtn.addActionListener(e -> {
            resetReservePanel();
            cardLayout.show(cards, "reserve");
        });
        c.gridy = 2;
        c.ipady = 20;
        panel.add(reserveBtn, c);

        JButton checkBtn = new JButton("예약확인");
        checkBtn.addActionListener(e -> {
            resetCheckPanel();
            cardLayout.show(cards, "check");
        });
        c.gridy = 3;
        panel.add(checkBtn, c);

        JButton exitBtn = new JButton("종료");
        exitBtn.addActionListener(e -> frame.dispose());
        c.gridy = 4;
        panel.add(exitBtn, c);

        return panel;
    }

    private JPanel buildReservePanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        top.add(new JLabel("ID:"));
        reserveIdField = new JTextField(10);
        top.add(reserveIdField);

        top.add(new JLabel("출발:"));
        departureCombo = new JComboBox<>(cityItems());
        top.add(departureCombo);

        top.add(new JLabel("도착:"));
        destinationCombo = new JComboBox<>(cityItems());
        top.add(destinationCombo);

        JButton searchBtn = new JButton("검색");
        searchBtn.addActionListener(e -> doSearch());
        top.add(searchBtn);

        panel.add(top, BorderLayout.NORTH);

        routeListModel = new DefaultListModel<>();
        routeList = new JList<>(routeListModel);
        routeList.setCellRenderer(new RouteCellRenderer());
        routeList.addListSelectionListener(e -> onRouteSelected());
        JScrollPane routeScroll = new JScrollPane(routeList);
        routeScroll.setBorder(BorderFactory.createTitledBorder("노선 목록"));
        routeScroll.setPreferredSize(new Dimension(280, 300));

        JPanel seatPanelWrap = new JPanel(new BorderLayout());
        seatPanelWrap.setBorder(BorderFactory.createTitledBorder("좌석 현황 (X = 예약됨)"));
        seatGridPanel = new JPanel(new GridLayout(0, SEAT_COLS, 4, 4));
        seatPanelWrap.add(seatGridPanel, BorderLayout.CENTER);
        selectedInfoLabel = new JLabel(" ");
        seatPanelWrap.add(selectedInfoLabel, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, routeScroll, seatPanelWrap);
        split.setResizeWeight(0.35);
        panel.add(split, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backBtn = new JButton("뒤로");
        backBtn.addActionListener(e -> cardLayout.show(cards, "home"));
        bottom.add(backBtn);

        JButton reserveBtn = new JButton("예약");
        reserveBtn.addActionListener(e -> doReserve());
        bottom.add(reserveBtn);

        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildCheckPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        top.add(new JLabel("ID:"));
        checkIdField = new JTextField(10);
        top.add(checkIdField);
        JButton lookupBtn = new JButton("조회");
        lookupBtn.addActionListener(e -> doLookup());
        top.add(lookupBtn);
        panel.add(top, BorderLayout.NORTH);

        reservationTableModel = new DefaultTableModel(
                new Object[]{"id", "노선", "출발지", "도착지", "시간", "좌석", "예약일"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        reservationTable = new JTable(reservationTableModel);
        panel.add(new JScrollPane(reservationTable), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backBtn = new JButton("뒤로");
        backBtn.addActionListener(e -> cardLayout.show(cards, "home"));
        bottom.add(backBtn);

        JButton cancelBtn = new JButton("선택 예약 취소");
        cancelBtn.addActionListener(e -> doCancel());
        bottom.add(cancelBtn);

        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private CityItem[] cityItems() {
        List<CityItem> list = new ArrayList<>();
        cities.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(en -> list.add(new CityItem(en.getKey(), en.getValue())));
        return list.toArray(new CityItem[0]);
    }

    private void resetReservePanel() {
        reserveIdField.setText("");
        routeListModel.clear();
        seatGridPanel.removeAll();
        seatGridPanel.revalidate();
        seatGridPanel.repaint();
        selectedRoute = null;
        selectedSeat = null;
        seatButtons = null;
        selectedInfoLabel.setText(" ");
    }

    private void resetCheckPanel() {
        checkIdField.setText("");
        reservationTableModel.setRowCount(0);
    }

    private void doSearch() {
        CityItem dep = (CityItem) departureCombo.getSelectedItem();
        CityItem dst = (CityItem) destinationCombo.getSelectedItem();
        if (dep == null || dst == null || dep.id == dst.id) {
            JOptionPane.showMessageDialog(frame, "출발지와 도착지를 다르게 선택하세요.", "안내", JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<BusRoute> found = searchService.searchBusRoute(dep.id, dst.id);
        routeListModel.clear();
        found.forEach(routeListModel::addElement);
        if (found.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "해당 노선이 없습니다.", "안내", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onRouteSelected() {
        BusRoute r = routeList.getSelectedValue();
        if (r == null) return;
        selectedRoute = r;
        selectedSeat = null;
        renderSeatGrid(r);
    }

    private void renderSeatGrid(BusRoute route) {
        seatGridPanel.removeAll();
        Seat[] seats = searchService.checkSeats(route.getBusRouteId());
        seatButtons = new JButton[seats.length];
        for (int i = 0; i < seats.length; i++) {
            final int idx = i;
            JButton btn = new JButton((i + 1) + (seats[i].isSeat() ? " X" : ""));
            btn.setEnabled(!seats[i].isSeat());
            btn.addActionListener(e -> selectSeat(idx + 1));
            seatButtons[i] = btn;
            seatGridPanel.add(btn);
        }
        seatGridPanel.revalidate();
        seatGridPanel.repaint();
        selectedInfoLabel.setText("선택 노선: " + routeLabel(route));
    }

    private void selectSeat(int seatNo) {
        selectedSeat = seatNo;
        for (int i = 0; i < seatButtons.length; i++) {
            if (!seatButtons[i].isEnabled()) continue;
            seatButtons[i].setBackground(i + 1 == seatNo ? Color.YELLOW : null);
            seatButtons[i].setOpaque(i + 1 == seatNo);
        }
        selectedInfoLabel.setText("선택 노선: " + routeLabel(selectedRoute) + "  |  좌석: " + seatNo + "번");
    }

    private void doReserve() {
        String userId = reserveIdField.getText().trim();
        if (!USER_ID_PATTERN.matcher(userId).matches()) {
            JOptionPane.showMessageDialog(frame, "ID 는 영문/숫자 2~10자 입니다.", "입력 오류", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selectedRoute == null) {
            JOptionPane.showMessageDialog(frame, "노선을 먼저 선택하세요.", "안내", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selectedSeat == null) {
            JOptionPane.showMessageDialog(frame, "좌석을 먼저 선택하세요.", "안내", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Reservation r = reservationService.makeReservation(userId, selectedRoute, selectedSeat);
            JOptionPane.showMessageDialog(frame, "예약 완료\n" + r, "완료", JOptionPane.INFORMATION_MESSAGE);
            renderSeatGrid(selectedRoute);
            selectedSeat = null;
        } catch (IllegalStateException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "예약 실패", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doLookup() {
        String userId = checkIdField.getText().trim();
        if (!USER_ID_PATTERN.matcher(userId).matches()) {
            JOptionPane.showMessageDialog(frame, "ID 는 영문/숫자 2~10자 입니다.", "입력 오류", JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<Reservation> mine = reservationService.checkReservation(userId);
        reservationTableModel.setRowCount(0);
        for (Reservation r : mine) {
            BusRoute route = routes.stream()
                    .filter(br -> br.getBusRouteId() == r.getBusRouteId())
                    .findFirst().orElse(null);
            String depName = route == null ? "?" : cities.getOrDefault(route.getDeparture(), "?");
            String dstName = route == null ? "?" : cities.getOrDefault(route.getDestination(), "?");
            String time = route == null ? "?" : route.getDepartureTime();
            reservationTableModel.addRow(new Object[]{
                    r.getId(), r.getBusRouteId(), depName, dstName, time, r.getSeatNumber(), r.getDate()
            });
        }
        if (mine.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "예약 내역이 없습니다.", "안내", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void doCancel() {
        int row = reservationTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(frame, "취소할 예약을 선택하세요.", "안내", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int resId = (int) reservationTableModel.getValueAt(row, 0);
        Reservation target = reservations.stream()
                .filter(r -> r.getId() == resId)
                .findFirst().orElse(null);
        if (target == null) {
            JOptionPane.showMessageDialog(frame, "해당 예약을 찾을 수 없습니다.", "안내", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(frame,
                "예약 id " + resId + " 을(를) 취소하시겠습니까?",
                "확인", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        boolean ok = reservationService.cancelReservation(target);
        JOptionPane.showMessageDialog(frame, ok ? "취소 완료." : "취소 실패.", "결과",
                ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        if (ok) doLookup();
    }

    private String routeLabel(BusRoute r) {
        return "[" + r.getBusRouteId() + "] "
                + cities.getOrDefault(r.getDeparture(), "?")
                + " → " + cities.getOrDefault(r.getDestination(), "?")
                + " / " + r.getDepartureTime();
    }

    private class RouteCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof BusRoute) {
                setText(routeLabel((BusRoute) value));
            }
            return this;
        }
    }

    static class CityItem {
        final int id;
        final String name;
        CityItem(int id, String name) { this.id = id; this.name = name; }
        @Override public String toString() { return name + " (" + id + ")"; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SwingApp().show());
    }
}
