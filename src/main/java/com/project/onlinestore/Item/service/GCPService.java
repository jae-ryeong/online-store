package com.project.onlinestore.Item.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.project.onlinestore.user.service.UserService;
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
    private final UserService userService;

    public String uploadImage(MultipartFile file, String userName) throws IOException {
        // 파일명 생성 (중복 방지)
        String uuid = UUID.randomUUID().toString();
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String storedFileName = uuid + "." + extension;
        String storeName = userService.storeNameReturn(userName);

        String objectName = storeName + "/" + storedFileName;

        // Blob 정보 생성
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, objectName)
                .setContentType(file.getContentType())
                .build();

        storage.create(blobInfo, file.getBytes());
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, objectName);
    }

    public void deleteImage(String userName, String fileName) throws IOException {
        String storeName = userService.storeNameReturn(userName);
        String objectName = storeName + "/" + fileName;

        BlobId blobId = BlobId.of(bucketName, objectName);
        boolean deleted = storage.delete(blobId);
        if (!deleted) {
            throw new IOException("Failed to delete " + objectName);
        }
    }
}
