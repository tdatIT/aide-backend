package com.aide.backend.service.impl;

import com.aide.backend.domain.dto.patients.ImageUploadResponse;
import com.aide.backend.domain.entity.patients.Image;
import com.aide.backend.exception.BusinessException;
import com.aide.backend.repository.ImageRepository;
import com.aide.backend.service.UploadService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryUploadServiceImpl implements UploadService {

    private final Cloudinary cloudinary;
    private final ImageRepository imageRepository;
    private static final int MAX_FILES = 5;

    @Override
    public List<ImageUploadResponse> uploadImages(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new BusinessException("No files provided");
        }

        if (files.size() > MAX_FILES) {
            throw new BusinessException("Maximum " + MAX_FILES + " files allowed");
        }

        List<ImageUploadResponse> responses = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new BusinessException("File is empty");
            }

            try {
                Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

                Image image = new Image();
                image.setPublicId((String) uploadResult.get("public_id"));
                image.setUrl((String) uploadResult.get("url"));
                image.setFormat((String) uploadResult.get("format"));
                image.setSize((Integer) uploadResult.get("bytes"));
                image.setWidth((Integer) uploadResult.get("width"));
                image.setHeight((Integer) uploadResult.get("height"));

                Image savedImage = imageRepository.save(image);
                responses.add(new ImageUploadResponse(savedImage.getId(), savedImage.getUrl()));
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }

        return responses;
    }
}
