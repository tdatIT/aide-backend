package com.aide.backend.service.impl;

import com.aide.backend.model.entity.patients.Image;
import com.aide.backend.repository.ImageRepository;
import com.aide.backend.service.UploadService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryUploadServiceImpl implements UploadService {

    private final Cloudinary cloudinary;
    private final ImageRepository imageRepository;

    @Override
    public Image uploadImage(MultipartFile file) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            Image image = new Image();
            image.setPublicId((String) uploadResult.get("public_id"));
            image.setUrl((String) uploadResult.get("url"));
            image.setFormat((String) uploadResult.get("format"));
            image.setSize((Integer) uploadResult.get("bytes"));
            image.setWidth((Integer) uploadResult.get("width"));
            image.setHeight((Integer) uploadResult.get("height"));

            return imageRepository.save(image);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }
}
