package com.project.onlinestore.Item.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.project.onlinestore.Item.dto.response.GCPMainImageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GCPService {

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    private final Storage storage;

    public GCPMainImageResponseDto mainImageUploadFile(MultipartFile file, String userName) throws IOException {
        // 파일명 생성 (중복 방지)
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // Blob 정보 생성
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
                .setContentType(file.getContentType())
                .build();

        // GCP에 파일 업로드
        storage.create(blobInfo, file.getBytes());

        // 파일 URL 생성
        String fileUrl = "https://storage.googleapis.com/" + bucketName + "/" + fileName;

        return GCPMainImageResponseDto.builder(userName, fileName, fileUrl);
    }

    public String uploadImage(MultipartFile file, String folderPath) throws IOException {
        // 파일명 생성 (중복 방지)
        String uuid = UUID.randomUUID().toString();
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String storedFileName = uuid + "." + extension;

        String objectName = folderPath + "/" + storedFileName;

        // Blob 정보 생성
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, objectName)
                .setContentType(file.getContentType())
                .build();

        storage.create(blobInfo, file.getBytes());
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, storedFileName);
    }

    public void deleteImage(String objectName) throws IOException {
        BlobId blobId = BlobId.of(bucketName, objectName);
        boolean deleted = storage.delete(blobId);
        if (!deleted) {
            throw new IOException("Failed to delete " + objectName);
        }
    }
}
