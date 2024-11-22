package __project.server.service;

import __project.server.repositories.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    private final int row = 5;
    private final int col = 10;
    private final int totalPercentageSeatsAllowedForRegisteredUser = 10;
    private final int capacity = 50;



    public ArrayList<ArrayList<Boolean>> getSeats(int screenIdFromSchedule) { {
        

        ArrayList<ArrayList<Boolean>>  seat_map = new ArrayList<ArrayList<Boolean>>();
        for(int i=0; i<row; i++){
            ArrayList<Boolean> row_list = new ArrayList<Boolean>();
            for(int j=1; j<=col; j++){
            int seatNumber = i*col + j;
            System.out.println("Seat Number " + seatNumber);
            Boolean isAvaliable = false;
            try{
            
                isAvaliable = seatRepository.findByScheduleId(screenIdFromSchedule,seatNumber);
            System.out.println("seatNumber " + seatNumber + " isAvaliable " + isAvaliable);

            }
            catch(Exception e){
                System.out.println("Error " + e);
            }
            if(isAvaliable){
                row_list.add(true);
            }
            else{
                row_list.add(false);
            }
           
            }
            seat_map.add(row_list);
        }
        System.out.println("Seat Map " + seat_map);

        // seat seats =  seatRepository.findById(id,);
        // System.out.println("Seats " + seats);

        return seat_map;}
    }



    public void reserveSeat(int scheduleId, int seatId) {
        ArrayList<ArrayList<Boolean>> seatStructure =   getSeats(scheduleId);
        int row = (seatId-1)/10;
        int col = (seatId-1)%10;
        if(seatStructure.get(row).get(col)){
            seatRepository.reserveSeat(scheduleId, seatId);
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seat is not available");
        }
    }




    public Boolean isNonPublicSeatsFilled(int screenIdFromSchedule) {

        ArrayList<ArrayList<Boolean>> seatStructure = getSeats(screenIdFromSchedule);
        int totalSeatsBooked=0;

        for(int i=0; i<5; i++){
            for(int j=0; j<10; j++){
                if(!seatStructure.get(i).get(j)){
                    totalSeatsBooked++;
                }
            }
        }

        if(totalSeatsBooked >= (totalPercentageSeatsAllowedForRegisteredUser*capacity)/100){
            return true;
        }

        return false;
        
    }










}
