package __project.server.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Collections;
import org.springframework.web.bind.annotation.PathVariable;
import __project.server.service.scheduleService;
import __project.server.model.schedule;
import java.util.Date;
import java.util.Set;


@RequestMapping("/")
@RestController
@CrossOrigin
public class scheduleController {

    @Autowired
    private scheduleService scheduleService;

    @GetMapping("/showtimes/{movieId}")
    public ResponseEntity<Map<String, Map<String, List<String>>>> getSchedules(@PathVariable int movieId) {
        List<schedule> schedules = scheduleService.getShowTimes(movieId);
        
        Map<String, Map<Integer, Set<String>>> groupedSchedules = new TreeMap<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d");
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");

        Map<String, Map<String, List<String>>> result = new TreeMap<>();

        for (schedule schedule : schedules) {
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
