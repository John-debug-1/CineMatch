package org.MY_APP.main.controller;

import jakarta.servlet.http.HttpSession;
import org.MY_APP.main.model.Upload;
import org.MY_APP.main.model.User;
import org.MY_APP.main.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/uploads")
public class UploadRestController {

    @Autowired
    private UploadService uploadService;

    // ✅ SAVE UPLOAD (message + multiple files)
    @PostMapping
    public Upload upload(
            @RequestParam("message") String message,
            @RequestParam(value = "files", required = false) MultipartFile[] files,
            HttpSession session
    ) {
        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            System.out.println("⛔ ERROR: No logged user in session.");
            throw new RuntimeException("User not logged in.");
        }

        return uploadService.saveUpload(user, message, files);
    }

    // ✅ GET ALL UPLOADS
    @GetMapping
    public List<Upload> getUploads() {
        return uploadService.getAllUploads();
    }
}
