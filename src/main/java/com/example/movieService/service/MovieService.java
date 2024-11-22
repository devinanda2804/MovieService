package com.example.movieService.service;


import com.example.movieService.exception.MovieNotFoundException;
import com.example.movieService.model.Movie;
import com.example.movieService.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
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

    public Movie updateMovie(int movieId, Movie movieDetails) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        if(movieDetails.getTitle()!=null){
            movie.setTitle(movieDetails.getTitle());
        }
        if(movieDetails.getDescription()!=null){
            movie.setDescription(movieDetails.getDescription());
        }
        if(movieDetails.getDuration()!=0){
            movie.setDuration(movieDetails.getDuration());
        }

        return movieRepository.save(movie);
    }

    @Transactional
    public void deleteMovie(Integer id){

        if(!movieRepository.existsById(id)){
            throw new MovieNotFoundException("Movie not found");
        }
        else{
            movieRepository.deleteById(id);
        }
    }


    public boolean existsById(int movieId) {
        return movieRepository.existsById(movieId);
    }
}
