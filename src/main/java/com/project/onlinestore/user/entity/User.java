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
    @Column(name = "USER_ID")
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
    private List<Item> item = new ArrayList<Item>();
    
    // grade 나중에 추가예정
}
