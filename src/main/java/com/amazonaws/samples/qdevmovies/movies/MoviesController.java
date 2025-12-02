package com.amazonaws.samples.qdevmovies.movies;

import com.amazonaws.samples.qdevmovies.utils.MovieIconUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@Controller
public class MoviesController {
    private static final Logger logger = LogManager.getLogger(MoviesController.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/movies")
    public String getMovies(
            org.springframework.ui.Model model,
            @org.springframework.web.bind.annotation.RequestParam(value = "name", required = false) String name,
            @org.springframework.web.bind.annotation.RequestParam(value = "id", required = false) Long id,
            @org.springframework.web.bind.annotation.RequestParam(value = "genre", required = false) String genre) {
        
        logger.info("Ahoy! Fetching movies with search criteria - name: '{}', id: {}, genre: '{}'", name, id, genre);
        
        List<Movie> movies;
        String searchMessage = null;
        boolean hasSearchCriteria = (name != null && !name.trim().isEmpty()) || 
                                   id != null || 
                                   (genre != null && !genre.trim().isEmpty());
        
        if (hasSearchCriteria) {
            // Validate ID if provided
            if (id != null && id <= 0) {
                movies = movieService.getAllMovies();
                searchMessage = "Arrr! That ID be as useless as a compass that points south! Showing all movies instead, ye scallywag!";
                logger.warn("Invalid movie ID provided: {}", id);
            } else {
                movies = movieService.searchMovies(name, id, genre);
                if (movies.isEmpty()) {
                    searchMessage = "Shiver me timbers! No movies found matching yer search criteria. The treasure chest be empty for this quest, matey!";
                } else {
                    searchMessage = movies.size() == 1 ? 
                        "Yo ho ho! Found one fine movie treasure for ye!" :
                        String.format("Batten down the hatches! Found %d movie treasures matching yer search, ye savvy sailor!", movies.size());
                }
            }
        } else {
            movies = movieService.getAllMovies();
            logger.info("No search criteria provided, showing all movies");
        }
        
        model.addAttribute("movies", movies);
        model.addAttribute("searchMessage", searchMessage);
        model.addAttribute("searchName", name);
        model.addAttribute("searchId", id);
        model.addAttribute("searchGenre", genre);
        model.addAttribute("availableGenres", movieService.getAllGenres());
        
        return "movies";
    }

    @GetMapping("/movies/{id}/details")
    public String getMovieDetails(@PathVariable("id") Long movieId, org.springframework.ui.Model model) {
        logger.info("Fetching details for movie ID: {}", movieId);
        
        Optional<Movie> movieOpt = movieService.getMovieById(movieId);
        if (!movieOpt.isPresent()) {
            logger.warn("Movie with ID {} not found", movieId);
            model.addAttribute("title", "Movie Not Found");
            model.addAttribute("message", "Movie with ID " + movieId + " was not found.");
            return "error";
        }
        
        Movie movie = movieOpt.get();
        model.addAttribute("movie", movie);
        model.addAttribute("movieIcon", MovieIconUtils.getMovieIcon(movie.getMovieName()));
        model.addAttribute("allReviews", reviewService.getReviewsForMovie(movie.getId()));
        
        return "movie-details";
    }
}