package __project.server.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import __project.server.repositories.movieRepository;

import __project.server.model.movie;
import java.util.List;


@Service
public class movieService {

    @Autowired
    private movieRepository movieRepository;

    public List<movie> getPublicMovies() {
        
        return movieRepository.findMovieAddedGreaterThanOneWeek();
    }

    public boolean isMoviePublic(int id) {
        List<movie> movies= movieRepository.isMovieAddedGreaterThanOneWeek(id);
        return movies.size() > 0;
    }

    public int getMovieId(String title) {
        int id = movieRepository.findByMovieName(title);  
        System.out.println("id" + id);
        return id;}

    public List<movie> getNonPublicMovies() {
        
        return movieRepository.findMovieAddedLessThanOneWeek();
    }


}
