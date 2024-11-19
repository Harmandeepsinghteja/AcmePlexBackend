package __project.server.controller;

import __project.server.model.Movie;
import __project.server.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;

import __project.server.utils.JwtUtil;
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

    // @GetMapping("/is-movie-public")
    // public boolean isMoviePublic(@PathVariable("id") int id) {
    //     boolean isPublic = movieService.isMoviePublic(id);

    //     return isPublic;
    // }
    


    @PostMapping("/is-movie-public")
    public ResponseEntity<Boolean> isMoviePublic(@RequestBody Map<String, String> request) {
        String movieIdString = request.get("movieId").toString();
        int movieIdInt = Integer.parseInt(movieIdString);
        System.out.println("id: " + movieIdInt);
        if (movieIdString == null) {
            return ResponseEntity.badRequest().body(false);
        }
        boolean isPublic = movieService.isMoviePublic(movieIdInt);
        return ResponseEntity.ok(isPublic);
    }

        @GetMapping("/non-public-movies") 
        public ResponseEntity<List<Map<String, Object>>> getNonPublicMovies(@RequestHeader String token) {
        int userId = JwtUtil.verifyJwt(token);
        
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
        
        @PostMapping("/movie") 
        public ResponseEntity<Map<String, Object>> getMovie(@RequestBody Map<String, String> request) {

        String movieIdString = request.get("movieId").toString();
        int movieIdInt = Integer.parseInt(movieIdString);
        boolean isPublic = movieService.isMoviePublic(movieIdInt);

        // Get Movie Name
        String movieName = movieService.getMovieName(movieIdInt);

        Map<String, Object> response = new HashMap<>();
        response.put("movieName", movieName);
        response.put("isMoviePublic", isPublic);
        return ResponseEntity.ok(response);
    }

}
