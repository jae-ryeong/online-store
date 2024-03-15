package com.project.onlinestore.user.entity;

import com.project.onlinestore.Item.entity.Item;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "item_cart")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_cart_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private Integer quantity;

    @Column(name = "cart_check")
    private boolean cartCheck;  // 장바구니 선택 체크
}
