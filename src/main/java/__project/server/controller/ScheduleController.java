package __project.server.controller;

import __project.server.service.SeatService;
import __project.server.model.Schedule;
import __project.server.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;
 

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import __project.server.utils.JwtUtil;



@RequestMapping("/")
@RestController
@CrossOrigin
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private SeatService seatService;

    @GetMapping("/showtimes/{movieId}")
    public ResponseEntity<Map<String, Map<String, List<String>>>> getSchedules(@PathVariable("movieId") int movieId,
                                                                                @RequestHeader String token) {
                                                                                    
        int userId = JwtUtil.verifyJwt(token);
        // String movieIdString = request.get("movieId").toString();
        int movieIdInt = movieId;

        List<Schedule> schedules = scheduleService.getShowTimes(movieIdInt);
        
        Map<String, Map<Integer, Set<String>>> groupedSchedules = new TreeMap<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        Map<String, Map<String, List<String>>> result = new TreeMap<>();
        

        for (Schedule schedule : schedules) {
            System.out.println("Schedule " + schedule.getStartTime());
            String date = dateFormat.format(schedule.getStartTime());
            String time = timeFormat.format(schedule.getStartTime());
            String screenName =  scheduleService.getScreenName(schedule.getScreenId()); 
            
            

            result.computeIfAbsent(date, k -> new TreeMap<>())
                  .computeIfAbsent(screenName, k -> new ArrayList<>())
                  .add(time);
        }


        

        // Sort the times for each screen
        for (Map<String, List<String>> dateMap : result.values()) {
            for (List<String> times : dateMap.values()) {
                Collections.sort(times);
            }
        }

        // System.out.println(result);
        return ResponseEntity.ok(result);
    }




    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<Map<String,Object>> getSchedule(@PathVariable("scheduleId") int scheduleId,
                                                @RequestHeader String token) {
        // int userId = JwtUtil.verifyJwt(token);
        Map<String,Object> result = new TreeMap<>();
        ArrayList<ArrayList<Boolean>> seats = seatService.getSeats(scheduleId);
        int price = scheduleService.getPrice(scheduleId);
        Boolean areNonPublicSeatsAvailable = seatService.isNonPublicSeatsFilled(scheduleId);
        int screenId = scheduleService.getScreenId(scheduleId);
        Timestamp startTime = scheduleService.getStartTime(scheduleId);
        String movieName = scheduleService.getMovieName(scheduleId);

        result.put("seats", seats);
        result.put("price", price);
        result.put("areNonPublicSeatsFilled", areNonPublicSeatsAvailable);
        result.put("screenId", screenId);
        result.put("startTime", startTime);
        result.put("movieName", movieName);
        return ResponseEntity.ok(result);

        

    }


}


