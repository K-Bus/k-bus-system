package service;

import model.BusRoute;
import model.Reservation;
import model.Seat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileIOService {

    public static final String DATA_DIR = "data";
    public static final String CITIES_TXT = DATA_DIR + "/cities.txt";
    public static final String SCHEDULE_TXT = DATA_DIR + "/schedule.txt";
    public static final String SEATS_SER = DATA_DIR + "/seats.ser";
    public static final String RESERVATION_SER = DATA_DIR + "/reservation.ser";

    public Map<Integer, String> readCities() {
        Map<Integer, String> cities = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CITIES_TXT))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] tokens = line.split("\\s+");
                int id = Integer.parseInt(tokens[0]);
                cities.put(id, tokens[1]);
            }
        } catch (IOException e) {
            throw new RuntimeException("cities.txt 읽기 실패", e);
        }
        return cities;
    }

    public List<BusRoute> readSchedule() {
        List<BusRoute> routes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(SCHEDULE_TXT))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] tokens = line.split("\\s+");
                int departure = Integer.parseInt(tokens[0]);
                int destination = Integer.parseInt(tokens[1]);
                String time = tokens[2];
                int busRouteId = Integer.parseInt(tokens[3]);
                routes.add(new BusRoute(busRouteId, departure, destination, null, time));
            }
        } catch (IOException e) {
            throw new RuntimeException("schedule.txt 읽기 실패", e);
        }
        return routes;
    }

    @SuppressWarnings("unchecked")
    public Map<Integer, Seat[]> readSeats() {
        File f = new File(SEATS_SER);
        if (!f.exists()) return new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (Map<Integer, Seat[]>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("seats.ser 읽기 실패", e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Reservation> readReservation() {
        File f = new File(RESERVATION_SER);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (List<Reservation>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("reservation.ser 읽기 실패", e);
        }
    }

    public void writeCities(Map<Integer, String> cities) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CITIES_TXT))) {
            for (Map.Entry<Integer, String> e : cities.entrySet()) {
                bw.write(e.getKey() + " " + e.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("cities.txt 쓰기 실패", e);
        }
    }

    public void writeSchedule(List<BusRoute> routes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SCHEDULE_TXT))) {
            for (BusRoute r : routes) {
                bw.write(r.getDeparture() + " " + r.getDestination() + " "
                        + r.getDepartureTime() + " " + r.getBusRouteId());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("schedule.txt 쓰기 실패", e);
        }
    }

    public void writeSeats(Map<Integer, Seat[]> seats) {
        ensureDataDir();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SEATS_SER))) {
            oos.writeObject(seats);
        } catch (IOException e) {
            throw new RuntimeException("seats.ser 쓰기 실패", e);
        }
    }

    public void writeReservation(List<Reservation> reservations) {
        ensureDataDir();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RESERVATION_SER))) {
            oos.writeObject(reservations);
        } catch (IOException e) {
            throw new RuntimeException("reservation.ser 쓰기 실패", e);
        }
    }

    private void ensureDataDir() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) dir.mkdirs();
    }
}
