package com.project.onlinestore.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart { // 장바구니
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}