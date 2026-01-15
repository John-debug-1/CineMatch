/**
 * Αυτό το test ελέγχει το TmdbService σαν unit test χωρίς Spring context,
 * κάνοντας mock τα HTTP calls προς το TMDb. Στο @BeforeEach δημιουργεί χειροκίνητα
 * το service, γεμίζει τα @Value fields (apiKey, apiUrl) με ReflectionTestUtils, παίρνει
 * το εσωτερικό RestTemplate και “δένει” πάνω του ένα MockRestServiceServer ώστε να μην
 * γίνουν πραγματικά requests. Κάθε test ορίζει το ακριβές URL που πρέπει να χτιστεί,
 * επιστρέφει ένα ψεύτικο JSON response, και μετά ελέγχει ότι η μέθοδος
 * (search, getMovieDetails, getTrendingMovies, getMovieCast) γυρίζει σωστά δεδομένα
 * και ότι το request έγινε όπως αναμενόταν (server.verify())
 */


package org.MY_APP.main.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.http.HttpMethod.GET;

class TmdbServiceTest {

    private TmdbService tmdbService;
    private MockRestServiceServer server;

    @BeforeEach
    void setup() {
        tmdbService = new TmdbService();

        // Βάζουμε τιμές στα @Value fields (χωρίς Spring context)
        ReflectionTestUtils.setField(tmdbService, "apiKey", "TEST_KEY");
        ReflectionTestUtils.setField(tmdbService, "apiUrl", "https://api.themoviedb.org/3");

        // Παίρνουμε το private final RestTemplate που έχει ΜΕΣΑ το service
        RestTemplate rt = (RestTemplate) ReflectionTestUtils.getField(tmdbService, "restTemplate");
        assertNotNull(rt, "restTemplate field was null (reflection failed)");

        // Στήνουμε mock server πάνω στο ίδιο RestTemplate
        server = MockRestServiceServer.bindTo(rt).build();
    }

    @Test
    void search_buildsCorrectUrl_andReturnsMap() {
        String expectedUrl =
                "https://api.themoviedb.org/3/search/multi?api_key=TEST_KEY&query=batman&include_adult=false";

        server.expect(requestTo(expectedUrl))
                .andExpect(method(GET))
                .andRespond(withSuccess("{\"results\":[]}", MediaType.APPLICATION_JSON));

        var res = tmdbService.search("batman");

        assertNotNull(res);
        assertTrue(res.containsKey("results"));
        server.verify();
    }

    @Test
    void getMovieDetails_appendsVideos() {
        String expectedUrl =
                "https://api.themoviedb.org/3/movie/123?api_key=TEST_KEY&append_to_response=videos";

        server.expect(requestTo(expectedUrl))
                .andExpect(method(GET))
                .andRespond(withSuccess("{\"id\":123,\"videos\":{\"results\":[]}}", MediaType.APPLICATION_JSON));

        var res = tmdbService.getMovieDetails(123);

        assertNotNull(res);
        assertEquals(123, ((Number) res.get("id")).intValue());
        server.verify();
    }

    @Test
    void getTrendingMovies_returnsResultsList() {
        String expectedUrl =
                "https://api.themoviedb.org/3/trending/movie/week?api_key=TEST_KEY&page=2";

        String body = """
                {
                  "results": [
                    {"id": 1, "title": "A"},
                    {"id": 2, "title": "B"}
                  ]
                }
                """;

        server.expect(requestTo(expectedUrl))
                .andExpect(method(GET))
                .andRespond(withSuccess(body, MediaType.APPLICATION_JSON));

        var list = tmdbService.getTrendingMovies(2);

        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals(1, ((Number) list.get(0).get("id")).intValue());
        server.verify();
    }

    @Test
    void getMovieCast_callsCredits_andReturnsCast() {
        String expectedUrl =
                "https://api.themoviedb.org/3/movie/555/credits?api_key=TEST_KEY";

        String body = """
                {
                  "cast": [
                    {"id": 10, "name": "Actor 1"},
                    {"id": 11, "name": "Actor 2"}
                  ]
                }
                """;

        server.expect(requestTo(expectedUrl))
                .andExpect(method(GET))
                .andRespond(withSuccess(body, MediaType.APPLICATION_JSON));

        var cast = tmdbService.getMovieCast(555);

        assertEquals(2, cast.size());
        assertEquals("Actor 1", cast.get(0).get("name"));
        server.verify();
    }
}
