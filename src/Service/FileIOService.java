package Service;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DTO.Reservation;
import DTO.Schedule;

public class FileIOService {

    //평문 읽기 : cities.txt
	public Map<Integer, String> readCities() throws IOException{
		BufferedReader CityBufferedReader = Files.newBufferedReader(Paths.get("data", "cities.txt"), StandardCharsets.UTF_8);
		Map<Integer, String> citiesMap = new HashMap<>();
		
        try {
        	String line;
			while ((line = CityBufferedReader.readLine()) != null) {
				String[] row = line.split(" ");
			    citiesMap.put(Integer.parseInt(row[0]), row[1]);
			}
			return citiesMap;
        } catch (IOException e) {
			e.printStackTrace();
			return null;
        } finally {
        	CityBufferedReader.close();
        }
	}
	
	//평뮨 읽기 : schedule.txt
	public List<Schedule> readScehdule() throws IOException{
		BufferedReader ScheduleBufferedReader = Files.newBufferedReader(Paths.get("data", "schedule.txt"), StandardCharsets.UTF_8);
		List<Schedule> schedules = new ArrayList<>();
	
        try {
        	String line;
			while ((line = ScheduleBufferedReader.readLine()) != null) {
				String[] row = line.split(" ");
				schedules.add(new Schedule (Integer.parseInt(row[0]), Integer.parseInt(row[1]), Integer.parseInt(row[2].substring(0, 2)), row[3]));
			}
			return schedules;
        } catch (IOException e) {
			e.printStackTrace();
			return null;
        } finally {
        	ScheduleBufferedReader.close();
        }
	}
	
	//역직렬화 읽기 : reservation.txt
	public List<Reservation> readReservation(){
		File file = new File("data/reservation.txt");
		List<Reservation> reservations = new ArrayList<>();
		
		try (FileInputStream fis = new FileInputStream(file); ObjectInputStream ois = new ObjectInputStream(fis);){
			while (true) {
	            try {
	            	reservations.add((Reservation) ois.readObject());
	            } catch(ClassNotFoundException e) {
	            	e.printStackTrace();
	            } catch (EOFException e) {
	            	break;
	            }
	        }
		} catch(IOException e){
			e.printStackTrace();
		}
		
		return reservations;
	}
	
	//직렬화 쓰기 : reservation.txt
	public void writeReservation(Reservation reservation) {
	    File file = new File("data/reservation.txt");

	    try {
	        ObjectOutputStream oos;

	        if (file.exists() && file.length() > 0) {
	            // append
	            oos = new AppendableObjectOutputStream(
	                    new FileOutputStream(file, true));
	        } else {
	            // 처음 생성 → 헤더 필요
	            oos = new ObjectOutputStream(
	                    new FileOutputStream(file));
	        }

	        oos.writeObject(reservation);
	        oos.close();

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	//writeReservation()에서 사용되는 inner class : ObjectOutputStream은 append가 그냥 안됨(처음 생성시 스트림헤더를 파일에 쓰기 때문)
	private class AppendableObjectOutputStream extends ObjectOutputStream {
	    public AppendableObjectOutputStream(OutputStream out) throws IOException {
	        super(out);
	    }

	    @Override
	    protected void writeStreamHeader() throws IOException {
	        reset(); // 헤더 안 씀
	    }
	}
	
}
