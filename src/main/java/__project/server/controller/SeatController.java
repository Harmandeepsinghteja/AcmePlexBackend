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
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin
@RequestMapping("/")
public class SeatController {

    @Autowired
    private SeatService seatService;


    @GetMapping("/seats")
    public ArrayList<ArrayList<Boolean>> getSeats(
            @RequestBody Map<String, String> request

    ) {
        String movieId =  request.get("movieId").toString() ;
        String screenId = request.get("screenId").toString();
        String date = request.get("date").toString();
        String time = request.get("time").toString();

        // String response = String.format("Seat info for id: %s, screenId: %s, date: %s, time: %s", movieId, screenId, date, time);
        int screenIdInt = Integer.parseInt(screenId);
        return seatService.getSeats(screenIdInt);
    }

    @PostMapping("/reserve")
    public Boolean reserveSeat(@RequestBody Map<String,String> request){
        String movieId =  request.get("movieId").toString() ;
        String screenId = request.get("screenId").toString();
        String date = request.get("date").toString();
        String time = request.get("time").toString();
        String seatId = request.get("seatId").toString();
        int screenIdInt = Integer.parseInt(screenId);
        int seatIdInt = Integer.parseInt(seatId);

        
        ArrayList<ArrayList<Boolean>> seatStructure =   seatService.getSeats(screenIdInt);
        
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




    // @GetMapping("/is-non-public-seats-filled")
    // public Boolean getNonPublicSeatsFilled(@RequestBody Map<String, String> request) {
    //     String movieId =  request.get("movieId").toString() ;
    //     String screenId = request.get("screenId").toString();
    //     String date = request.get("date").toString();
    //     String time = request.get("time").toString();
    //     int screenIdInt = Integer.parseInt(screenId);

    //     return seatService.isNonPublicSeatsFilled(screenIdInt);
    // }



}
