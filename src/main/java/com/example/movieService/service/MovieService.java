package com.example.movieService.service;


import com.example.movieService.model.Movie;
import com.example.movieService.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Optional<Movie> getMovieById(int id) {
        return movieRepository.findById(id);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie updateShowtimes(int movieId, List<LocalDateTime> showtimes) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        movie.setShowtimes(showtimes);
        return movieRepository.save(movie);
    }
    public boolean existsById(int movieId) {
        return movieRepository.existsById(movieId);
    }
}
