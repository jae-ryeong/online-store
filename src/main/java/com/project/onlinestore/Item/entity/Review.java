package com.project.onlinestore.Item.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "review")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;
}
