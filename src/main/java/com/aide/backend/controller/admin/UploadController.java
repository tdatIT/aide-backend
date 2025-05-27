package com.aide.backend.controller.admin;

import com.aide.backend.model.entity.patients.Image;
import com.aide.backend.service.UploadService;
import com.aide.backend.model.dto.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@RequestMapping("/api/v1/admin/uploads")
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @PostMapping("/images")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BaseResponse<Image>> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds 5MB limit");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        Image uploadedImage = uploadService.uploadImage(file);
        return ResponseEntity.ok(BaseResponse.success("Image uploaded successfully", uploadedImage));
    }
}
