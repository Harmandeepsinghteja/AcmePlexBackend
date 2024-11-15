package __project.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import __project.server.model.movie;
import __project.server.service.movieService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.Map;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequestMapping("/")
@RestController
public class movieController {

    @Autowired
    private movieService movieService;

    @GetMapping("/public-movies")  
    public ResponseEntity<List<Map<String, Object>>> getMovies() {
        List<movie> movies = movieService.getPublicMovies();
        List<Map<String, Object>> response = new ArrayList<>();
        for (movie movie : movies) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", movie.getId());
            item.put("movieName", movie.getMovieName());
            item.put("addedDate", movie.getAddedDate());
            item.put("url", movie.getUrl());
            response.add(item);
        }

        return ResponseEntity.ok(response);
}

    @GetMapping("/is-movie-public/{id}")
    public boolean isMoviePublic(@PathVariable("id") int id) {
        boolean isPublic = movieService.isMoviePublic(id);

        return isPublic;
    }
    


    @GetMapping(value = "/is-movie-public")
    public ResponseEntity<Boolean> isMoviePublic(@RequestBody Map<String, Integer> request) {
    Integer id = request.get("movieId");
    System.out.println("id: " + id);
    if (id == null) {
        return ResponseEntity.badRequest().body(false);
    }
    boolean isPublic = movieService.isMoviePublic(id);
    return ResponseEntity.ok(isPublic);
}

        @GetMapping("/non-public-movies")  
        public ResponseEntity<List<Map<String, Object>>> getNonPublicMovies() {
        List<movie> movies = movieService.getNonPublicMovies();
        List<Map<String, Object>> response = new ArrayList<>();
        for (movie movie : movies) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", movie.getId());
            item.put("movieName", movie.getMovieName());
            item.put("addedDate", movie.getAddedDate());
            item.put("url", movie.getUrl());
            response.add(item);
        }

        return ResponseEntity.ok(response);
}



}
