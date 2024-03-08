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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "addressee_name")
    private String addresseeName;   // 수취인 이름

    private String address;

    @Column(name = "detail_address")
    private String detailAddress;

    @Column(name = "postal_code")
    private Integer postalCode;

    private String tel;
}
