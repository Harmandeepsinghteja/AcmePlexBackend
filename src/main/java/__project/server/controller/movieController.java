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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.Map;
import org.springframework.web.bind.annotation.PathVariable;

@RequestMapping("/")
@RestController
@CrossOrigin
public class movieController {

    @Autowired
    private movieService movieService;

    @GetMapping("/public-movie")  
    public ResponseEntity<List<Map<String, Object>>> getMovies() {
        List<movie> movies = movieService.getPublicMovies();
        List<Map<String, Object>> response = new ArrayList<>();
        for (movie movie : movies) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", movie.getId());
            item.put("title", movie.getTitle());
            item.put("added_date", movie.getAdded_date());
            item.put("image", movie.getUrl());
            response.add(item);
        }

        return ResponseEntity.ok(response);
}

    @GetMapping("/is-movie-public/{id}")
    public boolean isMoviePublic(@PathVariable("id") int id) {
        boolean isPublic = movieService.isMoviePublic(id);

        return isPublic;
    }


}
