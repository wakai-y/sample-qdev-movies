package com.amazonaws.samples.qdevmovies.movies;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Ahoy matey! Test class for the MovieSearchController REST endpoints.
 * These tests be more comprehensive than a pirate's treasure map!
 */
@WebMvcTest(MovieSearchController.class)
public class MovieSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Movie> testMovies;
    private Movie testMovie1;
    private Movie testMovie2;

    @BeforeEach
    public void setUp() {
        testMovie1 = new Movie(1L, "The Prison Escape", "John Director", 1994, "Drama", "Test description 1", 142, 5.0);
        testMovie2 = new Movie(2L, "The Family Boss", "Michael Filmmaker", 1972, "Crime/Drama", "Test description 2", 175, 5.0);
        testMovies = Arrays.asList(testMovie1, testMovie2);
    }

    @Test
    @DisplayName("Yo ho ho! Test successful movie search with results")
    public void testSearchMoviesWithResults() throws Exception {
        when(movieService.searchMovies("prison", null, null)).thenReturn(Arrays.asList(testMovie1));

        mockMvc.perform(get("/movies/search")
                .param("name", "prison")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Yo ho ho! Found one fine movie treasure for ye!"))
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.movies").isArray())
                .andExpect(jsonPath("$.movies[0].id").value(1))
                .andExpect(jsonPath("$.movies[0].movieName").value("The Prison Escape"));
    }

    @Test
    @DisplayName("Shiver me timbers! Test movie search with multiple results")
    public void testSearchMoviesWithMultipleResults() throws Exception {
        when(movieService.searchMovies(null, null, "Drama")).thenReturn(testMovies);

        mockMvc.perform(get("/movies/search")
                .param("genre", "Drama")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Batten down the hatches! Found 2 movie treasures matching yer search, ye savvy sailor!"))
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.movies").isArray())
                .andExpect(jsonPath("$.movies[0].id").value(1))
                .andExpect(jsonPath("$.movies[1].id").value(2));
    }

    @Test
    @DisplayName("Arrr! Test movie search with no results")
    public void testSearchMoviesWithNoResults() throws Exception {
        when(movieService.searchMovies("nonexistent", null, null)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/movies/search")
                .param("name", "nonexistent")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Shiver me timbers! No movies found matching yer search criteria. The treasure chest be empty for this quest, matey!"))
                .andExpect(jsonPath("$.count").value(0))
                .andExpect(jsonPath("$.movies").isArray())
                .andExpect(jsonPath("$.movies").isEmpty());
    }

    @Test
    @DisplayName("Blimey! Test movie search with invalid ID")
    public void testSearchMoviesWithInvalidId() throws Exception {
        mockMvc.perform(get("/movies/search")
                .param("id", "-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Arrr! That ID be as useless as a compass that points south! Provide a valid movie ID, ye scallywag!"))
                .andExpect(jsonPath("$.count").value(0))
                .andExpect(jsonPath("$.movies").isArray())
                .andExpect(jsonPath("$.movies").isEmpty());
    }

    @Test
    @DisplayName("Batten down the hatches! Test movie search with no parameters")
    public void testSearchMoviesWithNoParameters() throws Exception {
        when(movieService.getAllMovies()).thenReturn(testMovies);

        mockMvc.perform(get("/movies/search")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Ahoy! No search criteria provided, so here be all the treasure in our chest!"))
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.movies").isArray())
                .andExpect(jsonPath("$.movies[0].id").value(1))
                .andExpect(jsonPath("$.movies[1].id").value(2));
    }

    @Test
    @DisplayName("Chart a course! Test movie search by ID")
    public void testSearchMoviesById() throws Exception {
        when(movieService.searchMovies(null, 1L, null)).thenReturn(Arrays.asList(testMovie1));

        mockMvc.perform(get("/movies/search")
                .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Yo ho ho! Found one fine movie treasure for ye!"))
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.movies[0].id").value(1))
                .andExpect(jsonPath("$.movies[0].movieName").value("The Prison Escape"));
    }

    @Test
    @DisplayName("Yo ho ho! Test movie search with multiple parameters")
    public void testSearchMoviesWithMultipleParameters() throws Exception {
        when(movieService.searchMovies("prison", null, "Drama")).thenReturn(Arrays.asList(testMovie1));

        mockMvc.perform(get("/movies/search")
                .param("name", "prison")
                .param("genre", "Drama")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Yo ho ho! Found one fine movie treasure for ye!"))
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.movies[0].id").value(1));
    }

    @Test
    @DisplayName("Shiver me timbers! Test getting all genres")
    public void testGetAllGenres() throws Exception {
        List<String> testGenres = Arrays.asList("Action/Crime", "Adventure/Fantasy", "Crime/Drama", "Drama");
        when(movieService.getAllGenres()).thenReturn(testGenres);

        mockMvc.perform(get("/movies/genres")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Here be all the movie genres in our treasure chest, matey!"))
                .andExpect(jsonPath("$.count").value(4))
                .andExpect(jsonPath("$.genres").isArray())
                .andExpect(jsonPath("$.genres[0]").value("Action/Crime"))
                .andExpect(jsonPath("$.genres[1]").value("Adventure/Fantasy"))
                .andExpect(jsonPath("$.genres[2]").value("Crime/Drama"))
                .andExpect(jsonPath("$.genres[3]").value("Drama"));
    }

    @Test
    @DisplayName("Arrr! Test service exception handling in search")
    public void testSearchMoviesServiceException() throws Exception {
        when(movieService.searchMovies(anyString(), any(), anyString()))
                .thenThrow(new RuntimeException("Database connection failed"));

        mockMvc.perform(get("/movies/search")
                .param("name", "test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Blimey! Something went wrong while searching the treasure chest. Try again later, ye landlubber!"))
                .andExpect(jsonPath("$.count").value(0))
                .andExpect(jsonPath("$.movies").isArray())
                .andExpect(jsonPath("$.movies").isEmpty());
    }

    @Test
    @DisplayName("Blimey! Test service exception handling in genres endpoint")
    public void testGetAllGenresServiceException() throws Exception {
        when(movieService.getAllGenres())
                .thenThrow(new RuntimeException("Service unavailable"));

        mockMvc.perform(get("/movies/genres")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Blimey! Couldn't fetch the genres from the treasure chest. Try again later!"))
                .andExpect(jsonPath("$.count").value(0))
                .andExpect(jsonPath("$.genres").isArray())
                .andExpect(jsonPath("$.genres").isEmpty());
    }

    @Test
    @DisplayName("Chart a course! Test search with empty string parameters")
    public void testSearchMoviesWithEmptyStringParameters() throws Exception {
        when(movieService.getAllMovies()).thenReturn(testMovies);

        mockMvc.perform(get("/movies/search")
                .param("name", "")
                .param("genre", "")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Ahoy! No search criteria provided, so here be all the treasure in our chest!"))
                .andExpect(jsonPath("$.count").value(2));
    }

    @Test
    @DisplayName("Batten down the hatches! Test search with zero ID")
    public void testSearchMoviesWithZeroId() throws Exception {
        mockMvc.perform(get("/movies/search")
                .param("id", "0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Arrr! That ID be as useless as a compass that points south! Provide a valid movie ID, ye scallywag!"));
    }
}