package __project.server.service;

import __project.server.model.Schedule;
import __project.server.repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private ScreenService screenService;
    @Autowired
    private MovieService movieService;


    public List<Schedule> getShowTimes(int id) {
        return scheduleRepository.findByMovieId(id);
    }

    public String getScreenName(int id) {
        return screenService.getScreenName(id);
    }

    public List<Schedule> getShowTimesByName(String movieName) {
        // System.out.println("movieName" + movieName);
        int id = movieService.getMovieId(movieName);
        return getShowTimes(id);
    }

    public Integer getScreenId(int movieId,int screenId, Timestamp time) {
        try{
            return scheduleRepository.findByMovieScreenDate(movieId,screenId, time);
        }
        catch(Exception e){
            System.out.println("Error " + e);
            return -1;
        }
       
    }



}
