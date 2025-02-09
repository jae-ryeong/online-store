package com.project.onlinestore.Item.controller;

import com.project.onlinestore.Item.service.GCPService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/item/gcp")
public class GCPController {

    private final GCPService gcpService;

    @PostMapping("/image/upload")
    public ResponseEntity<String> imageUpload(Authentication authentication, @RequestParam("file") MultipartFile file) throws IOException {
        try {
            String imageUrl = gcpService.uploadImage(file, authentication.getName());
            System.out.println(imageUrl);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(imageUrl);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error uploading image: " + e.getMessage());
        }
    }

    @DeleteMapping("/image/delete/{filename}")
    public ResponseEntity<String> deleteImage(Authentication authentication, @PathVariable("filename") String fileName) {

        try{
            gcpService.deleteImage(authentication.getName(), fileName);
            return ResponseEntity.ok("File deleted successfully");
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("image not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting image: " + e.getMessage());
        }
    }
}

