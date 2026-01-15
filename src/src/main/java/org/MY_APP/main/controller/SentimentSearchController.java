package org.MY_APP.main.controller;

import org.MY_APP.main.dto.MovieDtos;
import org.MY_APP.main.service.SentimentSearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/sentiment")
@CrossOrigin(origins = "*") // Πολύ σημαντικό για να μην βγάζει error σύνδεσης από frontend
public class SentimentSearchController {

    private final SentimentSearchService searchService;

    public SentimentSearchController(SentimentSearchService searchService) {
        this.searchService = searchService;
    }

    // Endpoint για κείμενο
    @PostMapping("/analyze")
    public ResponseEntity<MovieDtos.SentimentResultDTO> analyzeText(
            @RequestBody Map<String, String> request
    ) {
        String text = request.getOrDefault("text", "");
        MovieDtos.SentimentResultDTO result = searchService.analyzeText(text);
        return ResponseEntity.ok(result);
    }

    // Endpoint για εικόνα
    @PostMapping("/analyze-image")
    public ResponseEntity<MovieDtos.SentimentResultDTO> analyzeImage(
            @RequestParam("file") MultipartFile file
    ) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            MovieDtos.SentimentResultDTO result = searchService.analyzeImage(file.getBytes());
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            // Αν κάτι πάει στραβά με το διάβασμα των bytes
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
