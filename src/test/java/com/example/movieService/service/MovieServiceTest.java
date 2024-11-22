package com.example.movieService.service;

import com.example.movieService.exception.MovieNotFoundException;
import com.example.movieService.model.Movie;
import com.example.movieService.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MovieServiceTest {

    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieRepository movieRepository;

    public MovieServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addMovieTest() {
        Movie movie = new Movie(1, "Title", "Description", 120);

        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        Movie result = movieService.addMovie(movie);

        verify(movieRepository, times(1)).save(movie);
        assertNotNull(result);
        assertEquals("Title", result.getTitle());
    }

    @Test
    public void getMovieByIdTest_MovieExists() {
        Movie movie = new Movie(1, "Title", "Description", 120);
        when(movieRepository.findById(1)).thenReturn(Optional.of(movie));

        Optional<Movie> result = movieService.getMovieById(1);

        verify(movieRepository, times(1)).findById(1);
        assertTrue(result.isPresent());
        assertEquals("Title", result.get().getTitle());
    }

    @Test
    public void getMovieByIdTest_MovieNotFound() {
        when(movieRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Movie> result = movieService.getMovieById(1);

        verify(movieRepository, times(1)).findById(1);
        assertFalse(result.isPresent());
    }

    @Test
    public void getAllMoviesTest() {
        List<Movie> movies = Arrays.asList(
                new Movie(1, "Title1", "Description1", 120),
                new Movie(2, "Title2", "Description2", 150)
        );

        when(movieRepository.findAll()).thenReturn(movies);

        List<Movie> result = movieService.getAllMovies();

        verify(movieRepository, times(1)).findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void updateMovieTest() {
        Movie existingMovie = new Movie(1, "Old Title", "Old Description", 120);
        Movie updatedMovieDetails = new Movie(0, "New Title", null, 0);

        when(movieRepository.findById(1)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.save(existingMovie)).thenReturn(existingMovie);

        Movie result = movieService.updateMovie(1, updatedMovieDetails);

        verify(movieRepository, times(1)).findById(1);
        verify(movieRepository, times(1)).save(existingMovie);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        assertEquals("Old Description", result.getDescription()); // Unchanged
        assertEquals(120, result.getDuration()); // Unchanged
    }

    @Test
    public void updateMovieTest_MovieNotFound() {
        Movie updatedMovieDetails = new Movie(0, "New Title", null, 0);

        when(movieRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                movieService.updateMovie(1, updatedMovieDetails)
        );

        verify(movieRepository, times(1)).findById(1);
        assertEquals("Movie not found", exception.getMessage());
    }

    @Test
    public void deleteMovieTest_MovieExists() {
        when(movieRepository.existsById(1)).thenReturn(true);

        movieService.deleteMovie(1);

        verify(movieRepository, times(1)).existsById(1);
        verify(movieRepository, times(1)).deleteById(1);
    }

    @Test
    public void deleteMovieTest_MovieNotFound() {
        when(movieRepository.existsById(1)).thenReturn(false);

        Exception exception = assertThrows(MovieNotFoundException.class, () ->
                movieService.deleteMovie(1)
        );

        verify(movieRepository, times(1)).existsById(1);
        assertEquals("Movie not found", exception.getMessage());
    }

    @Test
    public void existsByIdTest() {
        when(movieRepository.existsById(1)).thenReturn(true);

        boolean result = movieService.existsById(1);

        verify(movieRepository, times(1)).existsById(1);
        assertTrue(result);
    }
}

