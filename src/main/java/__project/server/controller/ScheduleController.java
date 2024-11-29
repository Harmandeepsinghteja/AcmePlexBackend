package __project.server.controller;

import __project.server.model.Schedule;
import __project.server.service.ScheduleService;
import __project.server.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;



@RequestMapping("/")
@RestController
@CrossOrigin
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private SeatService seatService;

    @GetMapping("/showtimes/{movieId}")
    public ResponseEntity<Map<String, Map<String, List<Map<String,Object>>>>> getSchedules(
            @PathVariable("movieId") int movieId) {
        try{
        
        if(movieId < 0 ){
            return ResponseEntity.badRequest().build();
        }

        List<Schedule> schedules = scheduleService.getShowTimes(movieId);

        Map<String, Map<Integer, Set<String>>> groupedSchedules = new TreeMap<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        Map<String, Map<String, List<Map<String,Object>>>> result = new TreeMap<>();
        for (Schedule schedule : schedules) {
            System.out.println("Schedule " + schedule.getStartTime());
            String date = dateFormat.format(schedule.getStartTime());
            String time = timeFormat.format(schedule.getStartTime());
            int scheduleId = schedule.getId();
            String screenName =  scheduleService.getScreenName(schedule.getScreenId()); 
            
            Map<String, Object> scheduleMap = new HashMap<>();
            scheduleMap.put("time", time);  // Keep time as String
            scheduleMap.put("scheduleId", scheduleId); 
            result.computeIfAbsent(date, k -> new TreeMap<>())
                  .computeIfAbsent(screenName, k -> new ArrayList<>())
                    .add(scheduleMap);        
        }
        // Sort the times for each screen
        // for (Map<String, List<List<String>>> dateMap : result.values()) {
        //     for (List<List<String>> times : dateMap.values()) {
        //         Collections.sort(times, (a, b) -> a.get(0).compareTo(b.get(0)));
        //     }
        // }
        return ResponseEntity.ok(result);
    }
    catch(Exception e){
        System.out.println(e);
        return ResponseEntity.badRequest().build();
        }
    }




    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<Map<String,Object>> getSchedule(@PathVariable("scheduleId") int scheduleId) {
        try {
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
        catch(Exception e){
            System.out.println(e);
            return ResponseEntity.badRequest().build();
        }
    }
}


