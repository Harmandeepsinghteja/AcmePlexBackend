package __project.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import __project.server.service.SeatService;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Optional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import __project.server.model.seat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestBody;
import __project.server.service.ScheduleService;
import java.sql.Timestamp;

@RestController
@CrossOrigin
@RequestMapping("/")
public class SeatController {

    @Autowired
    private SeatService seatService;
    @Autowired ScheduleService scheduleService;


    @GetMapping("/seats")
    public ArrayList<ArrayList<Boolean>> getSeats(
            @RequestBody Map<String, String> request

    ) {
        String movieIdString = request.get("movieId").toString();
        String screenId = request.get("screenId").toString();
        String dateString = request.get("date").toString();
        String timeString = request.get("time").toString();
        int screenIdInt = Integer.parseInt(screenId);
        int movieIdInt = Integer.parseInt(movieIdString);
        LocalDate date = LocalDate.parse(dateString);
        LocalTime time = LocalTime.parse(timeString);
        LocalDateTime combined =  LocalDateTime.of(date, time);
        System.out.println("combined: " + combined);
        Timestamp timestampDate = Timestamp.valueOf(combined);
        System.out.println("Timestamp: " + timestampDate);

        int screenIdFromSchedule = scheduleService.getScreenId(movieIdInt,screenIdInt,timestampDate);

        if(screenIdFromSchedule == -1){
            System.out.println("Screen Id not found");
            return new ArrayList<ArrayList<Boolean>>(5);
        }
        System.out.println("screenIdFromSchedule: " + screenIdFromSchedule);
    
        System.out.println("id: " + movieIdInt);
        System.out.println("screenId: " + screenIdInt);
        System.out.println("date: " + date);
        System.out.println("time: " + time);
        System.out.println("combined: " + combined);

        return seatService.getSeats(screenIdFromSchedule);
    }

    @PostMapping("/reserve")
    public Boolean reserveSeat(@RequestBody Map<String,String> request){
        String movieIdString = request.get("movieId").toString();
        String screenId = request.get("screenId").toString();
        String dateString = request.get("date").toString();
        String timeString = request.get("time").toString();
        int screenIdInt = Integer.parseInt(screenId);
        int movieIdInt = Integer.parseInt(movieIdString);
        LocalDate date = LocalDate.parse(dateString);
        LocalTime time = LocalTime.parse(timeString);
        LocalDateTime combined =  LocalDateTime.of(date, time);
    
        String seatId = request.get("seatId").toString();
        int seatIdInt = Integer.parseInt(seatId);
  


        
        ArrayList<ArrayList<Boolean>> seatStructure =   seatService.getSeats(movieIdInt,movieIdInt,combined);
        
        int row = (seatIdInt-1)/10;
        int col = (seatIdInt-1)%10;
        if(seatStructure.get(row).get(col)){
            seatService.reserveSeat(screenIdInt, seatIdInt);
            return true;
        }
        else{
            System.out.println("Seat is not available");
        }


        return false;
    }




    @GetMapping("/is-non-public-seats-filled")
    public Boolean getNonPublicSeatsFilled(@RequestBody Map<String, String> request) {
 
        String movieIdString = request.get("movieId").toString();
        String screenId = request.get("screenId").toString();
        String dateString = request.get("date").toString();
        String timeString = request.get("time").toString();
        int screenIdInt = Integer.parseInt(screenId);
        int movieIdInt = Integer.parseInt(movieIdString);
        LocalDate date = LocalDate.parse(dateString);
        LocalTime time = LocalTime.parse(timeString);
        LocalDateTime combined =  LocalDateTime.of(date, time);
    
        // System.out.println("id: " + movieIdInt);
        // System.out.println("screenId: " + screenIdInt);
        // System.out.println("date: " + date);
        // System.out.println("time: " + time);
        // System.out.println("combined: " + combined);

        return seatService.isNonPublicSeatsFilled(movieIdInt, screenIdInt, combined);
    }



}
