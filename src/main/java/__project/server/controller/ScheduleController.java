package __project.server.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.TreeMap;
import java.util.Collections;

import __project.server.service.ScheduleService;
import __project.server.utils.JwtUtil;
import __project.server.model.Schedule;

import java.util.Set;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RequestMapping("/")
@RestController
@CrossOrigin
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/showtimes")
    public ResponseEntity<Map<String, Map<String, List<String>>>> getSchedules(@RequestBody Map<String, String> request,
                                                                                @RequestHeader String token) {
                                                                                    
        int userId = JwtUtil.verifyJwt(token);
        String movieIdString = request.get("movieId").toString();
        int movieIdInt = Integer.parseInt(movieIdString);

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
}
