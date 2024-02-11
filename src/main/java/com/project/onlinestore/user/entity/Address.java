package com.project.onlinestore.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "address")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {  // 배송지 정보

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADDRESS_ID")
    private Long id;
}
