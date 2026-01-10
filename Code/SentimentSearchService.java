package org.MY_APP.main.service;

import org.MY_APP.main.dto.MovieDtos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;
import java.io.OutputStream;
import com.fasterxml.jackson.core.type.TypeReference;

@Service
public class SentimentSearchService {

    private static final Logger log = LoggerFactory.getLogger(SentimentSearchService.class);

    @Value("${tmdb.api.key}")
    private String tmdbApiKey;

    @Value("${huggingface.token}")
    private String hfToken;

    @Value("${huggingface.model-url}")
    private String hfApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();


    private final ObjectMapper objectMapper = new ObjectMapper();

    // ==========================
//      IMAGE ANALYSIS
// ==========================
    public MovieDtos.SentimentResultDTO analyzeImage(byte[] imageBytes) {
        try {
            List<Map<String, Object>> results = callHuggingFace(imageBytes);

            log.info("HF raw body parsed: {}", results);

            if (results != null && !results.isEmpty()) {
                Map<String, Object> first = results.get(0);
                Object labelObj = first.get("label");
                Object scoreObj = first.get("score");

                log.info("HF first label: {}, score: {}", labelObj, scoreObj);

                if (labelObj instanceof String labelStr) {
                    String label = labelStr.toUpperCase(Locale.ROOT);
                    String mapped = mapEmotion(label);

                    log.info("Mapped emotion: {}", mapped);

                    return analyzeText(mapped);
                }
            } else {
                log.warn("HF returned empty or null results");
            }
        } catch (Exception e) {
            log.error("HF Error (manual HTTP): {}", e.getMessage(), e);
        }

        // fallback αν κάτι πάει στραβά
        return analyzeText("NEUTRAL");
    }

    private List<Map<String, Object>> callHuggingFace(byte[] imageBytes) throws Exception {
        URL url = new URL(hfApiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Bearer " + hfToken);
        conn.setRequestProperty("Content-Type", "application/octet-stream");
        conn.setRequestProperty("Accept", "application/json");

        // Στέλνουμε τα bytes της εικόνας
        try (OutputStream os = conn.getOutputStream()) {
            os.write(imageBytes);
        }

        int status = conn.getResponseCode();
        InputStream is = (status >= 200 && status < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();

        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        log.info("HF status: {}, body: {}", status, body);

        if (status < 200 || status >= 300) {
            // Αν δεν πήραμε 2xx, δεν προσπαθούμε καν να κάνουμε parse ως predictions
            return Collections.emptyList();
        }

        // Περιμένουμε JSON array: [ { "label": "...", "score": ... }, ... ]
        return objectMapper.readValue(body, new TypeReference<List<Map<String, Object>>>() {});
    }
    // ==========================
    //     EMOTION MAPPING
    // ==========================
    private String mapEmotion(String hfLabel) {
        // HF model label examples:
        // sad, angry, fear, neutral, disgust, happy, surprise
        if (hfLabel.contains("SAD") || hfLabel.contains("ANGRY") || hfLabel.contains("FEAR") || hfLabel.contains("DISGUST")) {
            return "SAD";
        }
        if (hfLabel.contains("HAPPY") || hfLabel.contains("SURPRISE")) {
            return "EXCITED";
        }
        return "NEUTRAL";
    }


    // ==========================
    //     TEXT ANALYSIS
    // ==========================
    public MovieDtos.SentimentResultDTO analyzeText(String text) {
        int score = calculateScore(text);
        String emotion = interpretEmotion(score, text);

        int genreId = emotion.equals("SAD") ? 35
                : emotion.equals("EXCITED") ? 28
                : 12;

        List<Map<String, Object>> movies = fetchMovies(genreId);

        return new MovieDtos.SentimentResultDTO(0, score, emotion, movies);
    }


    private int calculateScore(String text) {
        if (text == null || text.isBlank()) return 0;

        Map<String, Integer> dict = Map.of(
                "sad", -3,
                "bad", -2,
                "happy", 3,
                "good", 1,
                "excited", 3
        );

        int s = 0;
        for (String w : text.toLowerCase(Locale.ROOT).split("\\W+")) {
            s += dict.getOrDefault(w, 0);
        }
        return s;
    }


    private String interpretEmotion(int score, String rawText) {
        if ("SAD".equalsIgnoreCase(rawText)) return "SAD";
        if ("EXCITED".equalsIgnoreCase(rawText)) return "EXCITED";

        if (score < 0) return "SAD";
        if (score > 0) return "EXCITED";

        return "NEUTRAL";
    }


    // ==========================
    //     TMDB MOVIE FETCH
    // ==========================
    private final Random random = new Random();

    private List<Map<String, Object>> fetchMovies(int genreId) {
        int page = 1 + random.nextInt(5); // random page 1-5

        String url = "https://api.themoviedb.org/3/discover/movie" +
                "?api_key=" + tmdbApiKey +
                "&with_genres=" + genreId +
                "&sort_by=popularity.desc" +
                "&page=" + page;

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response == null) return Collections.emptyList();

            Object resultsObj = response.get("results");
            if (!(resultsObj instanceof List<?> rawList)) return Collections.emptyList();

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> results = (List<Map<String, Object>>) rawList;

            Collections.shuffle(results);

            return results.stream()
                    .limit(3)
                    .collect(Collectors.toList());

        } catch (RestClientException e) {
            log.error("TMDB Error: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
dsvhvfsvvsdjvjds



