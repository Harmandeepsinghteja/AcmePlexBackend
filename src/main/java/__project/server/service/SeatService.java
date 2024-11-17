package __project.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import __project.server.repositories.SeatRepository;
import __project.server.model.seat;
import java.util.Optional;

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



    public void reserveSeat(int screenId, int seatId) {
        System.out.println("Reserving seat " + seatId + " for screen " + screenId);
        seatRepository.reserveSeat(screenId, seatId);
    }




    public Boolean isNonPublicSeatsFilled(int movieId, int screenId, LocalDateTime date) {

        ArrayList<ArrayList<Boolean>> seatStructure = getSeats(movieId, screenId, date);
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
