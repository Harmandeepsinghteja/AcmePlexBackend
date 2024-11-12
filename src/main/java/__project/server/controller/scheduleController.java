package __project.server.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.Map;
import org.springframework.web.bind.annotation.PathVariable;
import __project.server.service.scheduleService;
import __project.server.model.schedule;



@RequestMapping("/")
@RestController
@CrossOrigin
public class scheduleController {

    @Autowired
    private scheduleService scheduleService;

    @GetMapping("/showtimes/{id}")
    public ResponseEntity<List<Map<String, Object>>> getShowTimes(@PathVariable int id){
        List<Map<String, Object>> response = new ArrayList<>();
        List<schedule> showTimes = scheduleService.getShowTimes(id);
        System.out.println(showTimes);
        return ResponseEntity.ok(response);
    }


}
