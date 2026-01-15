/**
 * Αυτό είναι unit test για το MovieController με MockMvc σε standaloneSetup και Mockito,
 * χωρίς Spring context. Κάνει mock τα dependencies (TmdbService, FavoriteRepository) ώστε
 * ο controller να πάρει “ψεύτικα” δεδομένα ταινίας και counts αγαπημένων, και μετά καλεί
 * το endpoint GET /movie/{id} για να ελέγξει τι βάζει στο model και ποιο view επιστρέφει.
 * Τα tests καλύπτουν 3 σενάρια: (1) όταν στα videos.results υπάρχει Trailer, ο controller
 * βρίσκει το πρώτο Trailer και βάζει trailerKey="abc123" στο model, (2) όταν υπάρχουν
 * videos αλλά κανένα Trailer, δεν δημιουργείται trailerKey και αν λείπει poster_path
 * γίνεται fallback σε "/images/no-image.png", και (3) όταν δεν υπάρχει καν το πεδίο videos,
 * το request δεν σκάει και επιστρέφει κανονικά το view "movie-details" με το movie στο model.
 */




package org.MY_APP.main;

import jakarta.servlet.http.HttpSession;
import org.MY_APP.main.controller.MovieController;
import org.MY_APP.main.repository.FavoriteRepository;
import org.MY_APP.main.service.TmdbService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MovieControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TmdbService tmdbService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private MovieController movieController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();
    }

    @Test
    void movieDetails_setsTrailerKey_whenTrailerExists() throws Exception {
        int id = 123;

        Map<String, Object> rawMovieData = Map.of(
                "id", id,
                "title", "Movie With Trailer",
                "overview", "Overview",
                "poster_path", "/poster.jpg",
                "release_date", "2020-01-01",
                "vote_average", 8.2,
                "popularity", 100.0,
                "videos", Map.of(
                        "results", List.of(
                                Map.of("type", "Trailer", "key", "abc123"),
                                Map.of("type", "Teaser", "key", "zzz999")
                        )
                )
        );

        when(tmdbService.getMovieDetails(id)).thenReturn(rawMovieData);
        when(favoriteRepository.countByMovieId(id)).thenReturn(7);

        mockMvc.perform(get("/movie/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("movie-details"))
                .andExpect(model().attributeExists("movie"))
                .andExpect(model().attribute("trailerKey", "abc123"))
                .andExpect(model().attribute("trailerKey", notNullValue()));
    }

    @Test
    void movieDetails_trailerKeyDoesNotExist_whenVideosExistButNoTrailer() throws Exception {
        int id = 456;

        Map<String, Object> rawMovieData = new java.util.HashMap<>();
        rawMovieData.put("id", id);
        rawMovieData.put("title", "No Trailer Movie");
        rawMovieData.put("overview", "Overview");
        rawMovieData.put("poster_path", null);
        rawMovieData.put("release_date", "2020-01-01");
        rawMovieData.put("vote_average", 9);
        rawMovieData.put("popularity", "55.5");

        // videos/results
        java.util.List<Map<String, Object>> results = new java.util.ArrayList<>();
        results.add(Map.of("type", "Teaser", "key", "tease1"));
        results.add(Map.of("type", "Clip", "key", "clip1"));
        Map<String, Object> videos = new java.util.HashMap<>();
        videos.put("results", results);

        rawMovieData.put("videos", videos);

        when(tmdbService.getMovieDetails(id)).thenReturn(rawMovieData);
        when(favoriteRepository.countByMovieId(id)).thenReturn(0);

        mockMvc.perform(get("/movie/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("movie-details"))
                .andExpect(model().attributeExists("movie"))
                // ✅ ΕΔΩ Η ΑΛΛΑΓΗ:
                .andExpect(model().attributeDoesNotExist("trailerKey"))
                .andExpect(model().attribute("movie",
                        hasProperty("posterPath", is("/images/no-image.png"))));
    }

    @Test
    void movieDetails_doesNotFail_whenVideosMissing_entirely() throws Exception {
        int id = 789;

        Map<String, Object> rawMovieData = Map.of(
                "id", id,
                "title", "Videos Missing Movie",
                "overview", "Overview",
                "poster_path", "/poster.jpg",
                "release_date", "2020-01-01",
                "vote_average", 7.0,
                "popularity", 10.0
                // ✅ NO "videos" key at all
        );

        when(tmdbService.getMovieDetails(id)).thenReturn(rawMovieData);
        when(favoriteRepository.countByMovieId(id)).thenReturn(2);

        mockMvc.perform(get("/movie/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("movie-details"))
                .andExpect(model().attributeExists("movie"))
                // ✅ εδώ ΔΕΝ απαιτούμε trailerKey, μόνο ότι δεν σκάει το request
                .andExpect(model().attributeExists("movie"));
    }
}
