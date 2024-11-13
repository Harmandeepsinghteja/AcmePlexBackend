package __project.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import __project.server.service.seatService;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Optional;
import org.springframework.web.bind.annotation.PathVariable;
import __project.server.model.seat;
import java.util.ArrayList;


@RestController
@CrossOrigin
@RequestMapping("/seats")
public class seatController {

    @Autowired
    private seatService seatService;


    @GetMapping("/{movieId}/{scheduleId}/{date}/{time}")
    public ArrayList<ArrayList<Boolean>> getSeats(
            @PathVariable("movieId") int movieId,
            @PathVariable("scheduleId") int scheduleId,
            @PathVariable("date") String date,
            @PathVariable("time") String time
    ) {
        System.out.println("scheduleId: " + scheduleId);
        return seatService.getSeats(scheduleId);
    }



}
