package org.MY_APP.main.controller;

import org.MY_APP.main.service.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class LoadMoreController {

    @Autowired
    private TmdbService tmdbService;

    @GetMapping("/load-more")
    public List<Map<String, Object>> loadMore(
            @RequestParam String type,
            @RequestParam int page
    ) {

        return switch (type) {
            case "movies" -> tmdbService.getTrendingMovies(page);
            case "latest" -> tmdbService.getLatestMovies(page);
            case "top" -> tmdbService.getTopRatedMovies(page);
            case "tv" -> tmdbService.getTrendingSeries(page);
            case "streaming" -> tmdbService.getStreamingPopular(page);
            default -> List.of();
        };
    }

    @GetMapping("/load-more-directors")
    public List<Map<String, Object>> loadMoreDirectors(
            @RequestParam int page
    ) {
        return tmdbService.getTopRatedDirectors(page);
    }
}
