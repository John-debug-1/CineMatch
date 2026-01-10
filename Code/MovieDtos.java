package org.MY_APP.main.dto;

import java.util.List;
import java.util.Map;

public class MovieDtos {

    public static class TmdbReviewResponse {
        private List<TmdbReview> results;

        public TmdbReviewResponse() {}

        public List<TmdbReview> getResults() { return results; }
        public void setResults(List<TmdbReview> results) { this.results = results; }
    }

    public static class TmdbReview {
        private String content;

        public TmdbReview() {}

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    public static class SentimentResultDTO {
        private int movieId;
        private double averageScore;
        private String sentimentLabel;
        private List<Map<String, Object>> recommendations;

        public SentimentResultDTO() {}

        public SentimentResultDTO(int movieId, double averageScore, String sentimentLabel, List<Map<String, Object>> recommendations) {
            this.movieId = movieId;
            this.averageScore = averageScore;
            this.sentimentLabel = sentimentLabel;
            this.recommendations = recommendations;
        }

        public int getMovieId() { return movieId; }
        public double getAverageScore() { return averageScore; }
        public String getSentimentLabel() { return sentimentLabel; }
        public List<Map<String, Object>> getRecommendations() { return recommendations; }
    }
}
public int getMovieId() { return movieId; }
        public double getAverageScore() { return averageScore; }
        public String getSentimentLabel() { return sentimentLabel; }
        public List<Map<String, Object>> getRecommendations() { return recommendations; }
    }
