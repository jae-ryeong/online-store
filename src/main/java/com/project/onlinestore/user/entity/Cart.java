package com.project.onlinestore.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "cart")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart { // 장바구니
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;
}
