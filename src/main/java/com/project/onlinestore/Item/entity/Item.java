package com.project.onlinestore.Item.entity;

import com.project.onlinestore.Item.entity.enums.Category;
import com.project.onlinestore.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "item")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_ID")
    private Long id;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;  // 판매자

    @Column(name = "SOLD_COUNT", nullable = false)
    private Long soldCount; // 팔린 갯수

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private boolean soldOut; // 판매여부

    @Column(name = "MAIN_IMAGE_URL", nullable = false)
    private String mainImageUrl;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
}
