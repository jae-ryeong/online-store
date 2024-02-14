package com.project.onlinestore.user.entity;

import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.user.entity.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    private String storeName;   // sellerType만

    @OneToMany(mappedBy = "user")
    private List<Item> item = new ArrayList<>();    // seller가 판매하는 Item

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;
    
    // grade 나중에 추가예정
}
