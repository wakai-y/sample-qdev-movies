package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Ahoy matey! Test class for the MovieService treasure chest operations.
 * These tests be more thorough than a pirate's search for buried gold!
 */
public class MovieServiceTest {

    private MovieService movieService;

    @BeforeEach
    public void setUp() {
        movieService = new MovieService();
    }

    @Test
    @DisplayName("Arrr! Test getting all movies from the treasure chest")
    public void testGetAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        
        assertNotNull(movies, "Movies list should not be null, ye scallywag!");
        assertFalse(movies.isEmpty(), "Movies list should not be empty, matey!");
        assertTrue(movies.size() > 0, "Should have at least one movie in the treasure chest!");
    }

    @Test
    @DisplayName("Shiver me timbers! Test getting movie by valid ID")
    public void testGetMovieByValidId() {
        Optional<Movie> movie = movieService.getMovieById(1L);
        
        assertTrue(movie.isPresent(), "Movie with ID 1 should exist, arrr!");
        assertEquals(1L, movie.get().getId(), "Movie ID should match the requested ID!");
    }

    @Test
    @DisplayName("Blimey! Test getting movie by invalid ID")
    public void testGetMovieByInvalidId() {
        Optional<Movie> movie = movieService.getMovieById(-1L);
        assertFalse(movie.isPresent(), "Movie with negative ID should not exist!");
        
        movie = movieService.getMovieById(0L);
        assertFalse(movie.isPresent(), "Movie with zero ID should not exist!");
        
        movie = movieService.getMovieById(null);
        assertFalse(movie.isPresent(), "Movie with null ID should not exist!");
        
        movie = movieService.getMovieById(999L);
        assertFalse(movie.isPresent(), "Movie with non-existent ID should not exist!");
    }

    @Test
    @DisplayName("Yo ho ho! Test searching movies by name")
    public void testSearchMoviesByName() {
        // Test partial matching (case insensitive)
        List<Movie> results = movieService.searchMoviesByName("prison");
        assertFalse(results.isEmpty(), "Should find movies with 'prison' in the name!");
        assertTrue(results.stream().anyMatch(m -> m.getMovieName().toLowerCase().contains("prison")),
                  "Results should contain movies with 'prison' in the name!");
        
        // Test case insensitive search
        List<Movie> resultsUpper = movieService.searchMoviesByName("PRISON");
        assertEquals(results.size(), resultsUpper.size(), "Case insensitive search should return same results!");
        
        // Test exact matching
        results = movieService.searchMoviesByName("The Prison Escape");
        assertFalse(results.isEmpty(), "Should find exact movie name match!");
        
        // Test non-existent movie
        results = movieService.searchMoviesByName("Non-existent Movie");
        assertTrue(results.isEmpty(), "Should return empty list for non-existent movie!");
    }

    @Test
    @DisplayName("Batten down the hatches! Test searching movies by empty or null name")
    public void testSearchMoviesByEmptyName() {
        List<Movie> allMovies = movieService.getAllMovies();
        
        // Test null name
        List<Movie> results = movieService.searchMoviesByName(null);
        assertEquals(allMovies.size(), results.size(), "Null name should return all movies!");
        
        // Test empty name
        results = movieService.searchMoviesByName("");
        assertEquals(allMovies.size(), results.size(), "Empty name should return all movies!");
        
        // Test whitespace only name
        results = movieService.searchMoviesByName("   ");
        assertEquals(allMovies.size(), results.size(), "Whitespace-only name should return all movies!");
    }

    @Test
    @DisplayName("Chart a course! Test searching movies by genre")
    public void testSearchMoviesByGenre() {
        // Test partial genre matching
        List<Movie> results = movieService.searchMoviesByGenre("Drama");
        assertFalse(results.isEmpty(), "Should find movies with 'Drama' genre!");
        assertTrue(results.stream().allMatch(m -> m.getGenre().toLowerCase().contains("drama")),
                  "All results should contain 'drama' in genre!");
        
        // Test case insensitive search
        List<Movie> resultsUpper = movieService.searchMoviesByGenre("DRAMA");
        assertEquals(results.size(), resultsUpper.size(), "Case insensitive genre search should return same results!");
        
        // Test compound genre
        results = movieService.searchMoviesByGenre("Crime");
        assertFalse(results.isEmpty(), "Should find movies with 'Crime' in genre!");
        
        // Test non-existent genre
        results = movieService.searchMoviesByGenre("Horror");
        assertTrue(results.isEmpty(), "Should return empty list for non-existent genre!");
    }

    @Test
    @DisplayName("Shiver me timbers! Test searching movies by empty or null genre")
    public void testSearchMoviesByEmptyGenre() {
        List<Movie> allMovies = movieService.getAllMovies();
        
        // Test null genre
        List<Movie> results = movieService.searchMoviesByGenre(null);
        assertEquals(allMovies.size(), results.size(), "Null genre should return all movies!");
        
        // Test empty genre
        results = movieService.searchMoviesByGenre("");
        assertEquals(allMovies.size(), results.size(), "Empty genre should return all movies!");
        
        // Test whitespace only genre
        results = movieService.searchMoviesByGenre("   ");
        assertEquals(allMovies.size(), results.size(), "Whitespace-only genre should return all movies!");
    }

    @Test
    @DisplayName("Arrr! Test advanced search with multiple criteria")
    public void testSearchMoviesWithMultipleCriteria() {
        // Test search by name and genre
        List<Movie> results = movieService.searchMovies("Family", null, "Crime");
        assertFalse(results.isEmpty(), "Should find movies matching both name and genre criteria!");
        assertTrue(results.stream().allMatch(m -> 
            m.getMovieName().toLowerCase().contains("family") && 
            m.getGenre().toLowerCase().contains("crime")),
            "All results should match both name and genre criteria!");
        
        // Test search by ID only (should return single movie or empty)
        results = movieService.searchMovies(null, 1L, null);
        assertEquals(1, results.size(), "Search by ID should return exactly one movie!");
        assertEquals(1L, results.get(0).getId(), "Returned movie should have the correct ID!");
        
        // Test search with non-existent ID
        results = movieService.searchMovies("any name", 999L, "any genre");
        assertTrue(results.isEmpty(), "Search with non-existent ID should return empty list!");
    }

    @Test
    @DisplayName("Yo ho ho! Test search with all empty criteria")
    public void testSearchMoviesWithAllEmptyCriteria() {
        List<Movie> allMovies = movieService.getAllMovies();
        List<Movie> results = movieService.searchMovies(null, null, null);
        
        assertEquals(allMovies.size(), results.size(), "Search with all null criteria should return all movies!");
        
        results = movieService.searchMovies("", null, "");
        assertEquals(allMovies.size(), results.size(), "Search with all empty criteria should return all movies!");
    }

    @Test
    @DisplayName("Batten down the hatches! Test getting all genres")
    public void testGetAllGenres() {
        List<String> genres = movieService.getAllGenres();
        
        assertNotNull(genres, "Genres list should not be null!");
        assertFalse(genres.isEmpty(), "Genres list should not be empty!");
        
        // Check that genres are unique
        long uniqueCount = genres.stream().distinct().count();
        assertEquals(uniqueCount, genres.size(), "All genres should be unique!");
        
        // Check that genres are sorted
        List<String> sortedGenres = genres.stream().sorted().toList();
        assertEquals(sortedGenres, genres, "Genres should be sorted alphabetically!");
        
        // Verify some expected genres exist
        assertTrue(genres.contains("Drama"), "Should contain 'Drama' genre!");
        assertTrue(genres.contains("Crime/Drama"), "Should contain 'Crime/Drama' genre!");
    }

    @Test
    @DisplayName("Chart a course! Test search edge cases")
    public void testSearchEdgeCases() {
        // Test search with very long string
        String longString = "a".repeat(1000);
        List<Movie> results = movieService.searchMoviesByName(longString);
        assertTrue(results.isEmpty(), "Search with very long string should return empty results!");
        
        // Test search with special characters
        results = movieService.searchMoviesByName("@#$%^&*()");
        assertTrue(results.isEmpty(), "Search with special characters should return empty results!");
        
        // Test search with numbers in name
        results = movieService.searchMoviesByName("123");
        assertTrue(results.isEmpty(), "Search with numbers should return empty results for this dataset!");
        
        // Test search with negative ID in advanced search
        results = movieService.searchMovies("any", -5L, "any");
        assertTrue(results.isEmpty(), "Search with negative ID should return empty results!");
    }
}