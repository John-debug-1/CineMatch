package org.MY_APP.main.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest(classes = SentimentSearchService.class)
class SentimentSearchServiceTest {

    @Autowired
    private SentimentSearchService sentimentSearchService;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        // Fake TMDB API key
        ReflectionTestUtils.setField(sentimentSearchService, "tmdbApiKey", "TEST_KEY");

        // Παίρνουμε το RestTemplate από το service (private final)
        RestTemplate restTemplate =
                (RestTemplate) ReflectionTestUtils.getField(sentimentSearchService, "restTemplate");

        assertNotNull(restTemplate, "RestTemplate field should not be null");

        // Δένουμε MockRestServiceServer πάνω σε αυτό το RestTemplate
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void analyzeText_happyReturnsExcitedAndActionMovies() {
        // EXCITED -> genreId = 28
        mockServer.expect(once(), requestTo(containsString("with_genres=28")))
                .andExpect(method(GET))
                .andRespond(withSuccess("""
                        {
                          "results": [
                            { "id": 1, "title": "Action 1" },
                            { "id": 2, "title": "Action 2" },
                            { "id": 3, "title": "Action 3" },
                            { "id": 4, "title": "Action 4" }
                          ]
                        }
                        """, MediaType.APPLICATION_JSON));

        var result = sentimentSearchService.analyzeText(
                "I am very happy and excited, it's a good day!"
        );

        mockServer.verify();

        assertNotNull(result);
        assertEquals("EXCITED", result.getSentimentLabel());
        assertTrue(result.getAverageScore() > 0, "Score should be positive for happy text");

        List<Map<String, Object>> movies = result.getRecommendations();
        assertNotNull(movies);
        assertEquals(3, movies.size(), "Should return exactly 3 movies after shuffle + limit");
    }

    @Test
    void analyzeText_sadReturnsSadAndComedyMovies() {
        // SAD -> genreId = 35
        mockServer.expect(once(), requestTo(containsString("with_genres=35")))
                .andExpect(method(GET))
                .andRespond(withSuccess("""
                        {
                          "results": [
                            { "id": 10, "title": "Comedy 1" },
                            { "id": 11, "title": "Comedy 2" },
                            { "id": 12, "title": "Comedy 3" }
                          ]
                        }
                        """, MediaType.APPLICATION_JSON));

        var result = sentimentSearchService.analyzeText("I feel very sad and bad today");

        mockServer.verify();

        assertNotNull(result);
        assertEquals("SAD", result.getSentimentLabel());
        assertTrue(result.getAverageScore() < 0, "Score should be negative for sad/bad text");

        List<Map<String, Object>> movies = result.getRecommendations();
        assertNotNull(movies);
        assertEquals(3, movies.size());
    }

    @Test
    void analyzeText_neutralReturnsNeutralAndAdventureMovies() {
        // NEUTRAL -> genreId = 12
        mockServer.expect(once(), requestTo(containsString("with_genres=12")))
                .andExpect(method(GET))
                .andRespond(withSuccess("""
                        {
                          "results": [
                            { "id": 20, "title": "Adventure 1" },
                            { "id": 21, "title": "Adventure 2" },
                            { "id": 22, "title": "Adventure 3" }
                          ]
                        }
                        """, MediaType.APPLICATION_JSON));

        var result = sentimentSearchService.analyzeText(
                "This is a plain sentence with no sentiment words."
        );

        mockServer.verify();

        assertNotNull(result);
        assertEquals("NEUTRAL", result.getSentimentLabel());
        assertEquals(0, result.getAverageScore(), "Score should be 0 when no known words are present");

        List<Map<String, Object>> movies = result.getRecommendations();
        assertNotNull(movies);
        assertEquals(3, movies.size());
    }

    @Test
    void analyzeText_handlesTmdbErrorGracefully() {
        // happy => EXCITED => genreId = 28
        mockServer.expect(once(), requestTo(containsString("with_genres=28")))
                .andExpect(method(GET))
                .andRespond(withServerError());

        var result = sentimentSearchService.analyzeText("I am happy");

        mockServer.verify();

        assertNotNull(result);
        assertEquals("EXCITED", result.getSentimentLabel());
        // Σε περίπτωση λάθους TMDB, fetchMovies() γυρίζει empty list
        assertNotNull(result.getRecommendations());
        assertTrue(result.getRecommendations().isEmpty(), "Movies list should be empty when TMDB fails");
    }

    @Test
    void analyzeText_whenTextIsNullOrBlank_returnsNeutralWithZeroScore() {
        // Το service πάντα καλεί fetchMovies, άρα περιμένουμε NEUTRAL => genreId = 12
        mockServer.expect(once(), requestTo(containsString("with_genres=12")))
                .andExpect(method(GET))
                .andRespond(withSuccess("""
                        { "results": [] }
                        """, MediaType.APPLICATION_JSON));

        var result = sentimentSearchService.analyzeText("   ");

        mockServer.verify();

        assertNotNull(result);
        assertEquals("NEUTRAL", result.getSentimentLabel());
        assertEquals(0, result.getAverageScore());
        assertNotNull(result.getRecommendations());
        assertEquals(0, result.getRecommendations().size());
    }
}
