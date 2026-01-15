package org.MY_APP.main.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class TmdbService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    // 🔎 SEARCH (για το search bar σου)
    public Map<String, Object> search(String query) {

        String url = UriComponentsBuilder
                .fromUriString(apiUrl + "/search/multi")
                .queryParam("api_key", apiKey)
                .queryParam("query", query)
                .queryParam("include_adult", false)
                .toUriString();

        return restTemplate.getForObject(url, Map.class);
    }

    // 🎬 MOVIE DETAILS
    public Map<String, Object> getMovieDetails(int id) {

        String url = UriComponentsBuilder
                .fromUriString(apiUrl + "/movie/" + id)
                .queryParam("api_key", apiKey)
                .queryParam("append_to_response", "videos")
                .toUriString();

        return restTemplate.getForObject(url, Map.class);
    }

    // 🧑 PERSON DETAILS
    public Map<String, Object> getPersonDetails(int id) {

        String url = UriComponentsBuilder
                .fromUriString(apiUrl + "/person/" + id)
                .queryParam("api_key", apiKey)
                .toUriString();

        return restTemplate.getForObject(url, Map.class);
    }

    // 📺 TV DETAILS
    public Map<String, Object> getTvDetails(int id) {

        String url = UriComponentsBuilder
                .fromUriString(apiUrl + "/tv/" + id)
                .queryParam("api_key", apiKey)
                .queryParam("append_to_response", "videos")
                .toUriString();

        return restTemplate.getForObject(url, Map.class);
    }

    // 🔥 TRENDING MOVIES (χρησιμοποιείται ήδη σε άλλους controllers)
    public List<Map<String, Object>> getTrendingMovies(int page) {
        String url = UriComponentsBuilder
                .fromUriString(apiUrl + "/trending/movie/week")
                .queryParam("api_key", apiKey)
                .queryParam("page", page)
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        return (List<Map<String, Object>>) response.get("results");
    }

    // 🔥 TRENDING SERIES (tv/week)
    public List<Map<String, Object>> getTrendingSeries(int page) {
        String url = UriComponentsBuilder
                .fromUriString(apiUrl + "/trending/tv/week")
                .queryParam("api_key", apiKey)
                .queryParam("page", page)
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        return (List<Map<String, Object>>) response.get("results");
    }

    // ⭐ TOP RATED MOVIES
    public List<Map<String, Object>> getTopRatedMovies(int page) {
        String url = UriComponentsBuilder
                .fromUriString(apiUrl + "/movie/top_rated")
                .queryParam("api_key", apiKey)
                .queryParam("page", page)
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        return (List<Map<String, Object>>) response.get("results");
    }

    // 🧑 TRENDING ACTORS (αν το χρειαστούν άλλοι controllers)
    public List<Map<String, Object>> getTrendingActors() {

        String url = UriComponentsBuilder
                .fromUriString(apiUrl + "/person/popular")
                .queryParam("api_key", apiKey)
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        return (List<Map<String, Object>>) response.get("results");
    }

    // 🧑 POPULAR ACTORS – αυτό θα χρησιμοποιεί το homepage
    public List<Map<String, Object>> getPopularActors() {

        String url = UriComponentsBuilder
                .fromUriString(apiUrl + "/person/popular")
                .queryParam("api_key", apiKey)
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        return (List<Map<String, Object>>) response.get("results");
    }

    // ⭐ SORT BY RATING
    public List<Map<String, Object>> getMoviesByRating(int page) {
        String url = UriComponentsBuilder
                .fromUriString(apiUrl + "/discover/movie")
                .queryParam("api_key", apiKey)
                .queryParam("sort_by", "vote_average.desc")
                .queryParam("page", page)
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        return (List<Map<String, Object>>) response.get("results");
    }

    // 📅 SORT BY RELEASE DATE
    public List<Map<String, Object>> getMoviesByReleaseDate(int page) {
        String url = UriComponentsBuilder
                .fromUriString(apiUrl + "/discover/movie")
                .queryParam("api_key", apiKey)
                .queryParam("sort_by", "release_date.desc")
                .queryParam("page", page)
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        return (List<Map<String, Object>>) response.get("results");
    }

    // 🔥 SORT BY POPULARITY
    public List<Map<String, Object>> getMoviesByPopularity(int page) {
        String url = UriComponentsBuilder
                .fromUriString(apiUrl + "/discover/movie")
                .queryParam("api_key", apiKey)
                .queryParam("sort_by", "popularity.desc")
                .queryParam("page", page)
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        return (List<Map<String, Object>>) response.get("results");
    }


    // 🆕 LATEST MOVIES (με βάση ημερομηνία)
    public List<Map<String, Object>> getLatestMovies(int page) {

        String url = UriComponentsBuilder
                .fromUriString(apiUrl + "/discover/movie")
                .queryParam("api_key", apiKey)
                .queryParam("sort_by", "release_date.desc")
                .queryParam("page", page)
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        return (List<Map<String, Object>>) response.get("results");
    }

    // 🎯 SIMILAR MOVIES BASED ON A MOVIE ID
    public List<Map<String, Object>> getSimilarMovies(int movieId, int page) {

        String url = UriComponentsBuilder
                .fromUriString(apiUrl + "/movie/" + movieId + "/similar")
                .queryParam("api_key", apiKey)
                .queryParam("page", page)
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        return (List<Map<String, Object>>) response.get("results");
    }


    // 🎬 TOP RATED DIRECTORS (7+)
    public List<Map<String, Object>> getTopRatedDirectors(int page) {

        String url = UriComponentsBuilder
                .fromUriString(apiUrl + "/trending/movie/week")
                .queryParam("api_key", apiKey)
                .queryParam("page", page)
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        List<Map<String, Object>> movies =
                (List<Map<String, Object>>) response.get("results");

        Set<Integer> directorIds = new HashSet<>();
        List<Map<String, Object>> directors = new ArrayList<>();

        for (Map<String, Object> movie : movies) {
            Integer movieId = (Integer) movie.get("id");

            String creditsUrl = UriComponentsBuilder
                    .fromUriString(apiUrl + "/movie/" + movieId + "/credits")
                    .queryParam("api_key", apiKey)
                    .toUriString();

            Map<String, Object> credits =
                    restTemplate.getForObject(creditsUrl, Map.class);

            List<Map<String, Object>> crew =
                    (List<Map<String, Object>>) credits.get("crew");

            for (Map<String, Object> person : crew) {
                if ("Director".equals(person.get("job"))) {
                    Integer pid = (Integer) person.get("id");

                    if (!directorIds.contains(pid)) {
                        directorIds.add(pid);
                        directors.add(person);
                    }
                }
            }
        }

        System.out.println("✅ REAL DIRECTORS FOUND: " + directors.size());

        return directors;
    }

    // 🎥 WHAT'S POPULAR ON STREAMING (όπως στο TMDB)
    public List<Map<String, Object>> getStreamingPopular(int page) {

        String url = UriComponentsBuilder
                .fromUriString(apiUrl + "/trending/all/week")
                .queryParam("api_key", apiKey)
                .queryParam("page", page)
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        return (List<Map<String, Object>>) response.get("results");
    }
    public List<Map<String, Object>> getMovieCast(int movieId) {
        String url = UriComponentsBuilder
                .fromUriString(apiUrl + "/movie/" + movieId + "/credits")
                .queryParam("api_key", apiKey)
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        return (List<Map<String, Object>>) response.get("cast");
    }
}
