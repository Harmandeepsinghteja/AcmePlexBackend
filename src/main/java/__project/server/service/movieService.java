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

    public int getMovieId(String title) {
        int id = movieRepository.findByTitle(title);  
        System.out.println("id" + id);
        return id;}


}
