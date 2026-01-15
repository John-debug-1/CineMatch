package org.MY_APP.main.service;

import org.MY_APP.main.model.Upload;
import org.MY_APP.main.model.User;
import org.MY_APP.main.repository.UploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UploadService {

    @Autowired
    private UploadRepository uploadRepository;

    private final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    public Upload saveUpload(User user, String message, MultipartFile[] files) {

        Upload upload = new Upload();
        upload.setUser(user);
        upload.setMessage(message);
        upload.setCreatedAt(LocalDateTime.now());

        if (files != null && files.length > 0 && !files[0].isEmpty()) {

            MultipartFile file = files[0];

            try {
                String uniqueName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR + uniqueName);
                Files.write(path, file.getBytes());

                upload.setFilePath(uniqueName);
                upload.setFileType(file.getContentType());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return uploadRepository.save(upload);
    }

    public List<Upload> getAllUploads() {
        return uploadRepository.findAllByOrderByCreatedAtDesc();
    }


}
