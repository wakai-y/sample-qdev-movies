package com.amazonaws.samples.qdevmovies.movies;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

@Service
public class MovieService {
    private static final Logger logger = LogManager.getLogger(MovieService.class);
    private final List<Movie> movies;
    private final Map<Long, Movie> movieMap;

    public MovieService() {
        this.movies = loadMoviesFromJson();
        this.movieMap = new HashMap<>();
        for (Movie movie : movies) {
            movieMap.put(movie.getId(), movie);
        }
    }

    private List<Movie> loadMoviesFromJson() {
        List<Movie> movieList = new ArrayList<>();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("movies.json");
            if (inputStream != null) {
                Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
                String jsonContent = scanner.useDelimiter("\\A").next();
                scanner.close();
                
                JSONArray moviesArray = new JSONArray(jsonContent);
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject movieObj = moviesArray.getJSONObject(i);
                    movieList.add(new Movie(
                        movieObj.getLong("id"),
                        movieObj.getString("movieName"),
                        movieObj.getString("director"),
                        movieObj.getInt("year"),
                        movieObj.getString("genre"),
                        movieObj.getString("description"),
                        movieObj.getInt("duration"),
                        movieObj.getDouble("imdbRating")
                    ));
                }
            }
        } catch (Exception e) {
            logger.error("Failed to load movies from JSON: {}", e.getMessage());
        }
        return movieList;
    }

    public List<Movie> getAllMovies() {
        return movies;
    }

    public Optional<Movie> getMovieById(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(movieMap.get(id));
    }

    /**
     * Ahoy matey! Search for movies by name with partial matching.
     * This method be as flexible as a pirate's moral code!
     * 
     * @param name The movie name to search for (case-insensitive partial matching)
     * @return List of movies matching the search criteria
     */
    public List<Movie> searchMoviesByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            logger.info("Arrr! Empty search name provided, returning all treasure (movies)");
            return new ArrayList<>(movies);
        }
        
        String searchTerm = name.trim().toLowerCase();
        logger.info("Ahoy! Searching for movies with name containing: {}", searchTerm);
        
        List<Movie> results = movies.stream()
            .filter(movie -> movie.getMovieName().toLowerCase().contains(searchTerm))
            .collect(java.util.stream.Collectors.toList());
            
        logger.info("Arrr! Found {} movies matching yer search, matey!", results.size());
        return results;
    }

    /**
     * Batten down the hatches! Search for movies by genre.
     * This method be more precise than a pirate's compass!
     * 
     * @param genre The genre to search for (case-insensitive partial matching)
     * @return List of movies matching the genre criteria
     */
    public List<Movie> searchMoviesByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            logger.info("Arrr! No genre specified, returning all treasure (movies)");
            return new ArrayList<>(movies);
        }
        
        String searchGenre = genre.trim().toLowerCase();
        logger.info("Ahoy! Searching for movies in genre: {}", searchGenre);
        
        List<Movie> results = movies.stream()
            .filter(movie -> movie.getGenre().toLowerCase().contains(searchGenre))
            .collect(java.util.stream.Collectors.toList());
            
        logger.info("Shiver me timbers! Found {} movies in that genre, ye scallywag!", results.size());
        return results;
    }

    /**
     * Chart a course through the movie treasure chest with multiple search criteria!
     * This be the most powerful search method in all the seven seas!
     * 
     * @param name Movie name to search for (optional)
     * @param id Movie ID to search for (optional)
     * @param genre Movie genre to search for (optional)
     * @return List of movies matching all provided criteria
     */
    public List<Movie> searchMovies(String name, Long id, String genre) {
        logger.info("Ahoy matey! Starting advanced search with name: '{}', id: {}, genre: '{}'", 
                   name, id, genre);
        
        List<Movie> results = new ArrayList<>(movies);
        
        // Filter by ID first if provided (most specific)
        if (id != null && id > 0) {
            Optional<Movie> movieById = getMovieById(id);
            if (movieById.isPresent()) {
                results = java.util.Arrays.asList(movieById.get());
                logger.info("Arrr! Found specific movie by ID: {}", id);
            } else {
                logger.warn("Blimey! No movie found with ID: {}", id);
                return new ArrayList<>(); // Return empty list if specific ID not found
            }
        }
        
        // Filter by name if provided
        if (name != null && !name.trim().isEmpty()) {
            String searchName = name.trim().toLowerCase();
            results = results.stream()
                .filter(movie -> movie.getMovieName().toLowerCase().contains(searchName))
                .collect(java.util.stream.Collectors.toList());
            logger.info("Filtered by name '{}', {} movies remain", searchName, results.size());
        }
        
        // Filter by genre if provided
        if (genre != null && !genre.trim().isEmpty()) {
            String searchGenre = genre.trim().toLowerCase();
            results = results.stream()
                .filter(movie -> movie.getGenre().toLowerCase().contains(searchGenre))
                .collect(java.util.stream.Collectors.toList());
            logger.info("Filtered by genre '{}', {} movies remain", searchGenre, results.size());
        }
        
        logger.info("Yo ho ho! Search complete! Found {} movies matching yer criteria, ye savvy sailor!", 
                   results.size());
        return results;
    }

    /**
     * Get all unique genres from the movie treasure chest.
     * Useful for populating search dropdowns, arrr!
     * 
     * @return List of unique genres available
     */
    public List<String> getAllGenres() {
        return movies.stream()
            .map(Movie::getGenre)
            .distinct()
            .sorted()
            .collect(java.util.stream.Collectors.toList());
    }
}
