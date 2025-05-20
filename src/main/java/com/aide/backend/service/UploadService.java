package com.aide.backend.service;

import com.aide.backend.model.entity.patients.Image;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    Image uploadImage(MultipartFile file);
}
