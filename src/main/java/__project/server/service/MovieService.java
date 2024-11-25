package __project.server.service;

import __project.server.model.Movie;
import __project.server.repositories.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public String getMovieName(int id) {
        String name = movieRepository.findMovieNamebyId(id);
        return name;
    }

    @Transactional
    public void setPubliclyAnnounced(int id, boolean publiclyAnnounced) {
        Movie movie = movieRepository.findById(id).get();
        movie.setPubliclyAnnounced(publiclyAnnounced);
    }

    @Transactional
    public void setPrivatelyAnnounced(int id, boolean privatelyAnnounced) {
        Movie movie = movieRepository.findById(id).get();
        movie.setPrivatelyAnnounced(privatelyAnnounced);
    }

    public Movie getMovie(int movieId){
        return movieRepository.findById(movieId).get();
    }


}
