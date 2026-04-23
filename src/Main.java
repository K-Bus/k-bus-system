import java.util.List;

import DTO.Reservation;
import Service.FileIOService;

public class Main {
    public static void main(String[] args) {
		  FileIOService fs = new FileIOService(); 
		  fs.writeReservation(new Reservation("ljw5953@gmail.com", 2));
		  fs.writeReservation(new Reservation("l@gmail.com", 3));
		  fs.writeReservation(new Reservation("lj@gmail.com", 4));
		  fs.writeReservation(new Reservation("ljw@gmail.com", 5));
		  fs.writeReservation(new Reservation("ljw3@naver.com", 6));
		  fs.writeReservation(new Reservation("ljw53@naver.com", 7));
		  fs.writeReservation(new Reservation("ljw5953111111111@gmail.com", 8));
	
		  List<Reservation> l = fs.readReservation();
		  for(Reservation r : l) {
			  System.out.printf("%s %s\n",r.getUserId(),r.getBusRouteId()); 
		  }
    }
}