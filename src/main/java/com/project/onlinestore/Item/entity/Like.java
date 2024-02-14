package com.project.onlinestore.Item.entity;

import com.project.onlinestore.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "likes")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;
}