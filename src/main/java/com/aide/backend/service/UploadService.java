package com.aide.backend.service;

import com.aide.backend.domain.dto.patients.ImageUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UploadService {
    List<ImageUploadResponse> uploadImages(List<MultipartFile> files);
}
