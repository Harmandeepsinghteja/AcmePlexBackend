package __project.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import __project.server.repositories.scheduleRepository;
import java.util.List;
import __project.server.model.schedule;
import __project.server.service.screenService;

@Service
public class scheduleService {

    @Autowired
    private scheduleRepository scheduleRepository;
    @Autowired
    private screenService screenService;
    @Autowired
    private movieService movieService;


    public List<schedule> getShowTimes(int id) {
        return scheduleRepository.findByMovieId(id);
    }

    public String getScreenName(int id) {
        return screenService.getScreenName(id);
    }

    public List<schedule> getShowTimesByName(String movieName) {
        // System.out.println("movieName" + movieName);
        int id = movieService.getMovieId(movieName);
        return getShowTimes(id);
    }



}
