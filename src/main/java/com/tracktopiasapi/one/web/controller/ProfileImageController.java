package com.tracktopiasapi.one.web.controller;

import com.tracktopiasapi.one.services.impl.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/users/{userId}/photo")
@RequiredArgsConstructor
public class ProfileImageController {

    private final S3Service s3Service;

    @GetMapping
    public ResponseEntity<byte[]> getUserPhoto(@PathVariable String userId) {
        try {
            byte[] image = s3Service.download("profile-images/" + userId + ".jpg");

            if (image == null || image.length == 0) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(image);

        } catch (S3Exception e) {
            if (e.statusCode() == 403 || e.statusCode() == 404) {
                return ResponseEntity.noContent().build();
            }
            throw e;
        }
    }


    @PutMapping
    public ResponseEntity<String> uploadUserPhoto(
            @PathVariable String userId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        s3Service.upload("profile-images/" + userId + ".jpg", file.getBytes());
        return ResponseEntity.ok("Photo uploaded successfully.");
    }
}
