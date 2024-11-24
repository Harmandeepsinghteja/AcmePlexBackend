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

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private MovieService movieService;

    // TODO: Should be fetched from database
    private final int row = 5;
    private final int col = 10;
    private final int totalPercentageSeatsAllowedForRegisteredUser = 10;
    private final int capacity = 50;

    public ArrayList<ArrayList<Boolean>> getSeats(int screenIdFromSchedule) { 

        ArrayList<ArrayList<Boolean>>  seat_map = new ArrayList<ArrayList<Boolean>>();
        for(int i=0; i<row; i++){
            ArrayList<Boolean> row_list = new ArrayList<Boolean>();
            for(int j=1; j<=col; j++){
            int seatNumber = i*col + j;
            System.out.println("Seat Number " + seatNumber);
            Boolean isAvaliable = false;
            try{
                isAvaliable = seatRepository.findByScheduleId(screenIdFromSchedule,seatNumber);
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
        return seat_map;
    }
    

    public void reserveSeat(int scheduleId, int seatId) {
        if(isSeatAvailable(scheduleId, seatId)){
            seatRepository.reserveSeat(scheduleId, seatId);
        }
        else{
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Seat is not available");
        }
    }

    public void makeSeatAvailable(int scheduleId, int seatId) {
        seatRepository.makeSeatAvailable(scheduleId, seatId);
    }

    public Boolean isNonPublicSeatsFilled(int screenIdFromSchedule) {
        // If movie is public then return false
        int movieId = scheduleService.getMovieId(screenIdFromSchedule);
        if(movieService.isMoviePublic(movieId)){
            return false;
        }
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

    private Boolean isSeatAvailable(int scheduleId, int seatId) {

        return seatRepository.isSeatAvailable(scheduleId, seatId);
    }






}
