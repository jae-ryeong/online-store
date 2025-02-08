package com.project.onlinestore.Item.dto.request;

import com.project.onlinestore.Item.entity.enums.Category;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RegistrationRequestDto {
    private String itemName;
    private Integer quantity;
    private Integer price;
    private Category category;
    private MultipartFile mainImageUrl;
    private String description;
    private List<MultipartFile> imageUrls;
}
