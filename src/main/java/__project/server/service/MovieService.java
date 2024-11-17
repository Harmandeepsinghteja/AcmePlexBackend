package __project.server.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import __project.server.repositories.MovieRepository;

import __project.server.model.Movie;
import java.util.List;


@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getPublicMovies() {
        
        return movieRepository.findMovieAddedGreaterThanOneWeek();
    }

    public boolean isMoviePublic(int id) {
        List<Movie> movies= movieRepository.isMovieAddedGreaterThanOneWeek(id);
        return movies.size() > 0;
    }

    public int getMovieId(String title) {
        int id = movieRepository.findByMovieName(title);  
        System.out.println("id" + id);
        return id;}

    public List<Movie> getNonPublicMovies() {
        
        return movieRepository.findMovieAddedLessThanOneWeek();
    }


}
