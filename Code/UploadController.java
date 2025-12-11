package org.MY_APP.main.controller;

import org.MY_APP.main.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
    
@RequestMapping("/api/upload")
public class UploadController {

    private final FileStorageService storageService;

    public UploadController(FileStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping
    public ResponseEntity<?> upload(@RequestParam("media") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("No file sent");
            }

            String savedName = storageService.saveFile(file);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "fileName", savedName,
                    "url", "/uploads/" + savedName
            ));

        
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }
}


