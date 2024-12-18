package __project.server.controller;

import __project.server.service.ScheduleService;
import __project.server.service.SeatService;
import __project.server.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;


@RestController
@CrossOrigin
@RequestMapping("/")
public class SeatController {

    @Autowired
    private SeatService seatService;
    @Autowired ScheduleService scheduleService;

    //TODO: Not Used Anyomre
    @GetMapping("/seats")
    public ArrayList<ArrayList<Boolean>> getSeats(
            @RequestBody Map<String, String> request,
            @RequestHeader String token

    ) {
        int userId = JwtUtil.verifyJwt(token);
        String movieIdString = request.get("movieId").toString();
        String screenId = request.get("screenId").toString();
        String dateString = request.get("date").toString();
        String timeString = request.get("time").toString();
        int screenIdInt = Integer.parseInt(screenId);
        int movieIdInt = Integer.parseInt(movieIdString);
        LocalDate date = LocalDate.parse(dateString);
        LocalTime time = LocalTime.parse(timeString);
        LocalDateTime combined =  LocalDateTime.of(date, time);
        Timestamp timestampDate = Timestamp.valueOf(combined);

        int screenIdFromSchedule = scheduleService.getScreenId(movieIdInt,screenIdInt,timestampDate);

        if(screenIdFromSchedule == -1){
            System.out.println("Screen Id not found");
            return new ArrayList<ArrayList<Boolean>>(5);
        }
        return seatService.getSeats(screenIdFromSchedule);
    }

    
    // TODO: Not Used Anymore. Ticket Controlller Handling this api
    // @PostMapping("/reserve")
    // public void reserveSeat(@RequestBody Map<String,String> request,
    //                            @RequestHeader String token) {   
    //     int userId = JwtUtil.verifyJwt(token);
    //     int scheduleId = Integer.parseInt(request.get("scheduleId").toString());
    //     String seatId = request.get("seatNumber").toString();
    //     int seatIdInt = Integer.parseInt(seatId);
    //     seatService.reserveSeat(scheduleId, seatIdInt);
    // }

    // TODO: Not Used Anymore
    // @GetMapping("/is-non-public-seats-filled")
    // public Boolean getNonPublicSeatsFilled(@RequestBody Map<String, String> request,
    //                                         @RequestHeader String token) {
    //     int userId = JwtUtil.verifyJwt(token);
    //     String movieIdString = request.get("movieId").toString();
    //     String screenId = request.get("screenId").toString();
    //     String dateString = request.get("date").toString();
    //     String timeString = request.get("time").toString();
    //     int screenIdInt = Integer.parseInt(screenId);
    //     int movieIdInt = Integer.parseInt(movieIdString);
    //     LocalDate date = LocalDate.parse(dateString);
    //     LocalTime time = LocalTime.parse(timeString);
    //     LocalDateTime combined =  LocalDateTime.of(date, time);
    //     Timestamp timestampDate = Timestamp.valueOf(combined);


    //     int screenIdFromSchedule = scheduleService.getScreenId(movieIdInt,screenIdInt,timestampDate);
    //     if(screenIdFromSchedule == -1){
    //         System.out.println("Screen Id not found");
    //         return false;
    //     }
    //     // System.out.println("id: " + movieIdInt);
    //     // System.out.println("screenId: " + screenIdInt);
    //     // System.out.println("date: " + date);
    //     // System.out.println("time: " + time);
    //     // System.out.println("combined: " + combined);
    //     return seatService.isNonPublicSeatsFilled(screenIdFromSchedule);
    // }



}
