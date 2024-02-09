package com.project.onlinestore.user.entity;

import com.project.onlinestore.user.entity.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    private String storeName;   // sellerType만
    
    // grade 나중에 추가예정
}
