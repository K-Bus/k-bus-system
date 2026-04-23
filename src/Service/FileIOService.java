package Service;

import java.io.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DTO.Reservation;
import DTO.Schedule;
import DTO.Seat;


public class FileIOService {
    private final BufferedReader CityBufferedReader;
    private final BufferedReader ScheduleBufferedReader;
    
    public FileIOService() throws IOException {
    	//절대경로로 박으면 배포 시 환경 다름으로 인한 예외 방지
		CityBufferedReader = Files.newBufferedReader(Paths.get("data", "cities.txt"), StandardCharsets.UTF_8);
		ScheduleBufferedReader = Files.newBufferedReader(Paths.get("data", "schedule.txt"), StandardCharsets.UTF_8);
    }
	
    //cities.txt : 평문 읽기
    public Map<Integer, String> readCities(){
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
	
	//schedule.txt : 평문 읽기
    public ArrayList<Schedule> readScehdule(){
		ArrayList<Schedule> schedules = new ArrayList<>();
		
        try {
        	String line;
			while ((line = ScheduleBufferedReader.readLine()) != null) {
				String[] row = line.split(" ");
			    //citiesMap.put(Integer.parseInt(row[0]), row[1]);
				schedules.add(new Schedule (Integer.parseInt(row[0]), Integer.parseInt(row[1]), row[2].substring(0, 2), Integer.parseInt(row[3])));
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

	// 직렬화 쓰기 : reservation.txt
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
