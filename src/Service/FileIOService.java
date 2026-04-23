package Service;

import java.io.BufferedReader;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import DTO.Schedule;

class FileIOService {
    private final BufferedReader CityBufferedReader;
    private final BufferedReader ScheduleBufferedReader;
    
    FileIOService() throws IOException {
    	//절대경로로 박으면 배포 시 환경 다름으로 인한 예외 방지
		CityBufferedReader = Files.newBufferedReader(Paths.get("data", "cities.txt"), StandardCharsets.UTF_8);
		ScheduleBufferedReader = Files.newBufferedReader(Paths.get("data", "schedule.txt"), StandardCharsets.UTF_8);
    }
	
    //cities.txt : 평문 읽기
	Map<Integer, String> readCities(){
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
        }
	}
	
	//schedule.txt : 평문 읽기
	ArrayList<Schedule> readScehdule(){
		ArrayList<Schedule> schedules = new ArrayList<>();
		
        try {
        	String line;
			while ((line = ScheduleBufferedReader.readLine()) != null) {
				String[] row = line.split(" ");
			    //citiesMap.put(Integer.parseInt(row[0]), row[1]);
				schedules.add(new Schedule (Integer.parseInt(row[0]), Integer.parseInt(row[1]), Integer.parseInt(row[2].substring(0, 2)), row[3]));
			}
			return schedules;
        } catch (IOException e) {
			e.printStackTrace();
			return null;
        }
	}
	
}
