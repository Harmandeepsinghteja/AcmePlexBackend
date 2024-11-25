package __project.server.controller;

import __project.server.model.Movie;
import __project.server.service.MovieService;
import __project.server.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RequestMapping("/")
@RestController
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/public-movies")  
    public ResponseEntity<List<Map<String, Object>>> getMovies() {
        try{
            List<Movie> movies = movieService.getPublicMovies();
            List<Map<String, Object>> response = new ArrayList<>();
            for (Movie movie : movies) {
                Map<String, Object> item = new HashMap<>();
                item.put("movieId", movie.getId());
                item.put("movieName", movie.getMovieName());
                item.put("addedDate", movie.getAddedDate());
                item.put("url", movie.getUrl());
                response.add(item);
            }
            return ResponseEntity.ok(response);
        }
        catch (Exception e) {

            return ResponseEntity.status(501).build();
    
        }

    }
        // @GetMapping("/is-movie-public")
    // public boolean isMoviePublic(@PathVariable("id") int id) {
    //     boolean isPublic = movieService.isMoviePublic(id);

    //     return isPublic;
    // }
    

    // TODO: Api Not Used -> Need to be removed
    @PostMapping("/is-movie-public")
    public ResponseEntity<Boolean> isMoviePublic(@RequestBody Map<String, String> request) {
        String movieIdString = request.get("movieId").toString();
        
        if (movieIdString == null || movieIdString.isEmpty()) {
            return ResponseEntity.badRequest().body(false);
        }

        try{
        int movieIdInt = Integer.parseInt(movieIdString);
        boolean isPublic = movieService.isMoviePublic(movieIdInt);
        return ResponseEntity.ok(isPublic);
    }
        catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    
    @GetMapping("/non-public-movies") 
    public ResponseEntity<List<Map<String, Object>>> getNonPublicMovies(@RequestHeader String token) {
        int userId = JwtUtil.verifyJwt(token); 
        try{   
            List<Movie> movies = movieService.getNonPublicMovies();
            List<Map<String, Object>> response = new ArrayList<>();
            for (Movie movie : movies) {
                Map<String, Object> item = new HashMap<>();
                item.put("movieId", movie.getId());
                item.put("movieName", movie.getMovieName());
                item.put("addedDate", movie.getAddedDate());
                item.put("url", movie.getUrl());
                response.add(item);
        }
        return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            return ResponseEntity.status(501).build();
        }
    }
        


        @GetMapping("/movie/{movieId}") 
        public ResponseEntity<Map<String, Object>> getMovie(@PathVariable("movieId") int movieId) {

        if(movieId < 0 ){
            return ResponseEntity.badRequest().build();
        }
        
        boolean isPublic = movieService.isMoviePublic(movieId);
        // Get Movie Name
        // String movieName = movieService.getMovieName(movieId);
        // String url = movieService.geturl(movieId);
        Movie movie = movieService.getMovie(movieId);
        Map<String, Object> response = new HashMap<>();
        response.put("movieName", movie.getMovieName());
        response.put("url", movie.getUrl());
        response.put("isMoviePublic", isPublic);
        return ResponseEntity.ok(response);
    }

}
