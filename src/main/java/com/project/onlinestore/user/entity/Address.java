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

    private String address;

    @Column(name = "detail_address")
    private String detailAddress;

    @Column(name = "postal_code")
    private Integer postalCode;

    private String tel;
    // TODO: 배송지 id가 들어와있으면 들어와 있는 애로 배송 하기, 없으면 에러 발생, 무조건 배송지를 만들어 줘서 id 값을 받아와야 한다.
    // TODO: 스프링 address 엔티티, USER 엔티티에서 List<Address> 가능할까??
}
