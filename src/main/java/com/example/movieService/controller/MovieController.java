package com.example.movieService.controller;

import com.example.movieService.model.Movie;
import com.example.movieService.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        Movie savedMovie = movieService.addMovie(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable int id) {
        return movieService.getMovieById(id)
                .map(movie -> ResponseEntity.ok().body(movie))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable int id, @RequestBody Movie movieDetails) {
        Movie updatedMovie = movieService.updateMovie(id, movieDetails);
        return ResponseEntity.ok(updatedMovie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable int id){
        movieService.deleteMovie(id);
        return ResponseEntity.ok("Deletion successfull");
    }

    @GetMapping("/{movieId}/exists")
    public ResponseEntity<Boolean> checkIfMovieExists(@PathVariable int movieId) {
        boolean exists = movieService.existsById(movieId);
        return ResponseEntity.ok(exists);
    }
}

