package com.project.onlinestore.Item.controller;

import com.project.onlinestore.Item.dto.response.GCPMainImageResponseDto;
import com.project.onlinestore.Item.service.GCPService;
import com.project.onlinestore.user.service.UserService;
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

    @PostMapping("/main/image/upload")
    public ResponseEntity<GCPMainImageResponseDto> imageUpload(Authentication authentication, @RequestParam("file") MultipartFile file) throws IOException {

        GCPMainImageResponseDto gcpMainImageResponseDto = null;

        try {
            gcpMainImageResponseDto = gcpService.mainImageUploadFile(file, authentication.getName());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(gcpMainImageResponseDto);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(gcpMainImageResponseDto);
        }
    }

    @PostMapping("/content/image/upload")
    public ResponseEntity<Void> contentImageUpload(Authentication authentication, @RequestParam("file") MultipartFile file) throws IOException {

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/image/delete/{objectName}")
    public ResponseEntity<String> deleteImage(@PathVariable String objectName) {
        try{
            gcpService.deleteImage(objectName);
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

