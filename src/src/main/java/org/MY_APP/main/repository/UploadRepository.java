package org.MY_APP.main.repository;

import org.MY_APP.main.model.Upload;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UploadRepository extends JpaRepository<Upload, Long> {

    List<Upload> findAllByOrderByCreatedAtDesc();
}