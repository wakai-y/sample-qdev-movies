package com.amazonaws.samples.qdevmovies.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ahoy matey! This be the REST controller for movie search operations.
 * Chart yer course through the movie treasure with these fine endpoints!
 */
@RestController
public class MovieSearchController {
    private static final Logger logger = LogManager.getLogger(MovieSearchController.class);

    @Autowired
    private MovieService movieService;

    /**
     * Arrr! Search for movies using various criteria, ye savvy sailor!
     * This endpoint be more flexible than a pirate's schedule!
     * 
     * @param name Movie name to search for (optional, partial matching)
     * @param id Movie ID to search for (optional, exact matching)
     * @param genre Movie genre to search for (optional, partial matching)
     * @return JSON response with search results and pirate-themed messages
     */
    @GetMapping("/movies/search")
    public ResponseEntity<Map<String, Object>> searchMovies(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "genre", required = false) String genre) {
        
        logger.info("Ahoy! Received search request - name: '{}', id: {}, genre: '{}'", name, id, genre);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate ID parameter if provided
            if (id != null && id <= 0) {
                logger.warn("Blimey! Invalid movie ID provided: {}", id);
                response.put("success", false);
                response.put("message", "Arrr! That ID be as useless as a compass that points south! Provide a valid movie ID, ye scallywag!");
                response.put("movies", List.of());
                response.put("count", 0);
                return ResponseEntity.badRequest().body(response);
            }
            
            // Check if at least one search parameter is provided
            if ((name == null || name.trim().isEmpty()) && 
                id == null && 
                (genre == null || genre.trim().isEmpty())) {
                
                logger.info("No search criteria provided, returning all movies");
                List<Movie> allMovies = movieService.getAllMovies();
                response.put("success", true);
                response.put("message", "Ahoy! No search criteria provided, so here be all the treasure in our chest!");
                response.put("movies", allMovies);
                response.put("count", allMovies.size());
                return ResponseEntity.ok(response);
            }
            
            // Perform the search
            List<Movie> searchResults = movieService.searchMovies(name, id, genre);
            
            if (searchResults.isEmpty()) {
                logger.info("No movies found matching search criteria");
                response.put("success", true);
                response.put("message", "Shiver me timbers! No movies found matching yer search criteria. The treasure chest be empty for this quest, matey!");
                response.put("movies", searchResults);
                response.put("count", 0);
                return ResponseEntity.ok(response);
            } else {
                logger.info("Found {} movies matching search criteria", searchResults.size());
                String pirateMessage = searchResults.size() == 1 ? 
                    "Yo ho ho! Found one fine movie treasure for ye!" :
                    String.format("Batten down the hatches! Found %d movie treasures matching yer search, ye savvy sailor!", searchResults.size());
                
                response.put("success", true);
                response.put("message", pirateMessage);
                response.put("movies", searchResults);
                response.put("count", searchResults.size());
                return ResponseEntity.ok(response);
            }
            
        } catch (Exception e) {
            logger.error("Arrr! Error occurred during movie search: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Blimey! Something went wrong while searching the treasure chest. Try again later, ye landlubber!");
            response.put("movies", List.of());
            response.put("count", 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get all available genres from the movie treasure chest.
     * Useful for populating search forms, arrr!
     * 
     * @return JSON response with all available genres
     */
    @GetMapping("/movies/genres")
    public ResponseEntity<Map<String, Object>> getAllGenres() {
        logger.info("Ahoy! Fetching all available movie genres");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<String> genres = movieService.getAllGenres();
            response.put("success", true);
            response.put("message", "Here be all the movie genres in our treasure chest, matey!");
            response.put("genres", genres);
            response.put("count", genres.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Arrr! Error occurred while fetching genres: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Blimey! Couldn't fetch the genres from the treasure chest. Try again later!");
            response.put("genres", List.of());
            response.put("count", 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}