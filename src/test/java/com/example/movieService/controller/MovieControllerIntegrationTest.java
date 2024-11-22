package com.example.movieService.controller;

import com.example.movieService.model.Movie;
import com.example.movieService.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovieControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;


    @Autowired
    private MovieRepository movieRepository;

    private String baseUrl;

    @BeforeEach
    public void setup() {
        testRestTemplate = testRestTemplate.withBasicAuth("admin", "admin");
        baseUrl = "http://localhost:" + port + "/api/movies";
        movieRepository.deleteAll(); // Clear database before each test
    }

    @Test
    public void testAddMovie() {
        // Arrange
        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setDescription("A mind-bending thriller");
        movie.setDuration(148);

        // Act
        ResponseEntity<Movie> response = testRestTemplate.postForEntity(baseUrl, movie, Movie.class);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Inception", response.getBody().getTitle());
        assertEquals("A mind-bending thriller", response.getBody().getDescription());
        assertEquals(148, response.getBody().getDuration());
    }

    @Test
    public void testGetMovieById() {
        // Arrange
        Movie movie = new Movie();
        movie.setTitle("Avatar");
        movie.setDescription("A sci-fi epic");
        movie.setDuration(162);
        movie = movieRepository.save(movie);

        // Act
        ResponseEntity<Movie> response = testRestTemplate.getForEntity(baseUrl + "/" + movie.getId(), Movie.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Avatar", response.getBody().getTitle());
    }

    @Test
    public void testGetAllMovies() {
        // Arrange
        Movie movie1 = new Movie();
        movie1.setTitle("Titanic");
        movie1.setDescription("A historical romance");
        movie1.setDuration(195);

        movieRepository.save(movie1);

        Movie movie2 = new Movie();
        movie2.setTitle("The Matrix");
        movie2.setDescription("A sci-fi masterpiece");
        movie2.setDuration(136);
        movieRepository.save(movie2);

        // Act
        ResponseEntity<Movie[]> response = testRestTemplate.getForEntity(baseUrl, Movie[].class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
    }

    @Test
    public void testUpdateMovie() {
        // Arrange
        Movie movie = new Movie();
        movie.setTitle("Old Name");
        movie.setDescription("Old Description");
        movie.setDuration(100);
        movie = movieRepository.save(movie);

        Movie updatedDetails = new Movie();
        updatedDetails.setTitle("New Name");
        updatedDetails.setDescription("New Description");
        updatedDetails.setDuration(120);

        HttpEntity<Movie> requestEntity = new HttpEntity<>(updatedDetails);

        // Act
        ResponseEntity<Movie> response = testRestTemplate.exchange(
                baseUrl + "/" + movie.getId(),
                HttpMethod.PUT,
                requestEntity,
                Movie.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("New Name", response.getBody().getTitle());
        assertEquals(120, response.getBody().getDuration());
    }

    @Test
    public void testDeleteMovie() {
        // Arrange
        Movie movie = new Movie();
        movie.setTitle("To Delete");
        movie.setDescription("Temporary movie");
        movie.setDuration(90);
        movie = movieRepository.save(movie);

        // Act
        ResponseEntity<String> response = testRestTemplate.exchange(
                baseUrl + "/" + movie.getId(),
                HttpMethod.DELETE,
                null,
                String.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deletion successfull", response.getBody());
        assertFalse(movieRepository.existsById(movie.getId()));
    }

    @Test
    public void testCheckIfMovieExists() {
        // Arrange
        Movie movie = new Movie();
        movie.setTitle("Exist Check");
        movie.setDescription("Testing existence");
        movie.setDuration(120);

        movie = movieRepository.save(movie);

        // Act
        ResponseEntity<Boolean> response = testRestTemplate.getForEntity(baseUrl + "/" + movie.getId() + "/exists", Boolean.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }
}
