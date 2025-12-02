package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.ui.Model;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ahoy matey! Test class for the MoviesController with pirate-themed search functionality.
 * These tests be more thorough than a pirate's inspection of treasure!
 */
public class MoviesControllerTest {

    private MoviesController moviesController;
    private Model model;
    private MovieService mockMovieService;
    private ReviewService mockReviewService;

    @BeforeEach
    public void setUp() {
        moviesController = new MoviesController();
        model = new ExtendedModelMap();
        
        // Create mock services with pirate-themed test data
        mockMovieService = new MovieService() {
            private final List<Movie> testMovies = Arrays.asList(
                new Movie(1L, "The Pirate's Treasure", "Captain Director", 2023, "Adventure", "A swashbuckling adventure", 120, 4.5),
                new Movie(2L, "Sea Battle", "Admiral Filmmaker", 2022, "Action", "Epic naval combat", 140, 4.0),
                new Movie(3L, "Treasure Island", "Buccaneer Producer", 2021, "Adventure", "Classic pirate tale", 110, 4.8)
            );
            
            @Override
            public List<Movie> getAllMovies() {
                return new ArrayList<>(testMovies);
            }
            
            @Override
            public Optional<Movie> getMovieById(Long id) {
                return testMovies.stream().filter(m -> m.getId().equals(id)).findFirst();
            }
            
            @Override
            public List<Movie> searchMovies(String name, Long id, String genre) {
                List<Movie> results = new ArrayList<>(testMovies);
                
                if (id != null && id > 0) {
                    Optional<Movie> movieById = getMovieById(id);
                    return movieById.map(Arrays::asList).orElse(new ArrayList<>());
                }
                
                if (name != null && !name.trim().isEmpty()) {
                    String searchName = name.trim().toLowerCase();
                    results = results.stream()
                        .filter(movie -> movie.getMovieName().toLowerCase().contains(searchName))
                        .collect(java.util.stream.Collectors.toList());
                }
                
                if (genre != null && !genre.trim().isEmpty()) {
                    String searchGenre = genre.trim().toLowerCase();
                    results = results.stream()
                        .filter(movie -> movie.getGenre().toLowerCase().contains(searchGenre))
                        .collect(java.util.stream.Collectors.toList());
                }
                
                return results;
            }
            
            @Override
            public List<String> getAllGenres() {
                return Arrays.asList("Action", "Adventure", "Drama");
            }
        };
        
        mockReviewService = new ReviewService() {
            @Override
            public List<Review> getReviewsForMovie(long movieId) {
                return new ArrayList<>();
            }
        };
        
        // Inject mocks using reflection
        try {
            java.lang.reflect.Field movieServiceField = MoviesController.class.getDeclaredField("movieService");
            movieServiceField.setAccessible(true);
            movieServiceField.set(moviesController, mockMovieService);
            
            java.lang.reflect.Field reviewServiceField = MoviesController.class.getDeclaredField("reviewService");
            reviewServiceField.setAccessible(true);
            reviewServiceField.set(moviesController, mockReviewService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock services", e);
        }
    }

    @Test
    @DisplayName("Yo ho ho! Test getting all movies without search criteria")
    public void testGetMoviesWithoutSearch() {
        String result = moviesController.getMovies(model, null, null, null);
        
        assertNotNull(result, "Result should not be null, ye scallywag!");
        assertEquals("movies", result, "Should return movies template!");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertNotNull(movies, "Movies attribute should not be null!");
        assertEquals(3, movies.size(), "Should return all 3 test movies!");
    }

    @Test
    @DisplayName("Arrr! Test searching movies by name")
    public void testGetMoviesWithNameSearch() {
        String result = moviesController.getMovies(model, "treasure", null, null);
        
        assertEquals("movies", result, "Should return movies template!");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertNotNull(movies, "Movies should not be null!");
        assertEquals(2, movies.size(), "Should find 2 movies with 'treasure' in name!");
        
        String searchMessage = (String) model.getAttribute("searchMessage");
        assertNotNull(searchMessage, "Search message should not be null!");
        assertTrue(searchMessage.contains("Found 2 movie treasures"), 
                  "Should have pirate-themed success message!");
    }

    @Test
    @DisplayName("Shiver me timbers! Test searching movies by genre")
    public void testGetMoviesWithGenreSearch() {
        String result = moviesController.getMovies(model, null, null, "Adventure");
        
        assertEquals("movies", result, "Should return movies template!");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertNotNull(movies, "Movies should not be null!");
        assertEquals(2, movies.size(), "Should find 2 adventure movies!");
        
        String searchMessage = (String) model.getAttribute("searchMessage");
        assertNotNull(searchMessage, "Search message should not be null!");
        assertTrue(searchMessage.contains("Found 2 movie treasures"), 
                  "Should have pirate-themed success message for multiple results!");
    }

    @Test
    @DisplayName("Blimey! Test searching movies by ID")
    public void testGetMoviesWithIdSearch() {
        String result = moviesController.getMovies(model, null, 1L, null);
        
        assertEquals("movies", result, "Should return movies template!");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertNotNull(movies, "Movies should not be null!");
        assertEquals(1, movies.size(), "Should find exactly 1 movie by ID!");
        assertEquals(1L, movies.get(0).getId(), "Found movie should have correct ID!");
        
        String searchMessage = (String) model.getAttribute("searchMessage");
        assertNotNull(searchMessage, "Search message should not be null!");
        assertTrue(searchMessage.contains("Found one fine movie treasure"), 
                  "Should have pirate-themed success message!");
    }

    @Test
    @DisplayName("Batten down the hatches! Test searching with invalid ID")
    public void testGetMoviesWithInvalidId() {
        String result = moviesController.getMovies(model, null, -1L, null);
        
        assertEquals("movies", result, "Should return movies template!");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertNotNull(movies, "Movies should not be null!");
        assertEquals(3, movies.size(), "Should return all movies when ID is invalid!");
        
        String searchMessage = (String) model.getAttribute("searchMessage");
        assertNotNull(searchMessage, "Search message should not be null!");
        assertTrue(searchMessage.contains("compass that points south"), 
                  "Should have pirate-themed error message for invalid ID!");
    }

    @Test
    @DisplayName("Chart a course! Test searching with no results")
    public void testGetMoviesWithNoResults() {
        String result = moviesController.getMovies(model, "nonexistent", null, null);
        
        assertEquals("movies", result, "Should return movies template!");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertNotNull(movies, "Movies should not be null!");
        assertTrue(movies.isEmpty(), "Should return empty list for no matches!");
        
        String searchMessage = (String) model.getAttribute("searchMessage");
        assertNotNull(searchMessage, "Search message should not be null!");
        assertTrue(searchMessage.contains("treasure chest be empty"), 
                  "Should have pirate-themed no results message!");
    }

    @Test
    @DisplayName("Shiver me timbers! Test getting movie details")
    public void testGetMovieDetails() {
        String result = moviesController.getMovieDetails(1L, model);
        assertNotNull(result, "Result should not be null!");
        assertEquals("movie-details", result, "Should return movie-details template!");
        
        Movie movie = (Movie) model.getAttribute("movie");
        assertNotNull(movie, "Movie attribute should be set!");
        assertEquals(1L, movie.getId(), "Movie should have correct ID!");
    }

    @Test
    @DisplayName("Blimey! Test getting movie details for non-existent movie")
    public void testGetMovieDetailsNotFound() {
        String result = moviesController.getMovieDetails(999L, model);
        assertNotNull(result, "Result should not be null!");
        assertEquals("error", result, "Should return error template for non-existent movie!");
        
        String title = (String) model.getAttribute("title");
        String message = (String) model.getAttribute("message");
        assertEquals("Movie Not Found", title, "Should set error title!");
        assertTrue(message.contains("999"), "Error message should contain the requested ID!");
    }
}
