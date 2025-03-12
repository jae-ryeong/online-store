package com.project.onlinestore.order.Entity;

import com.project.onlinestore.user.entity.Address;
import com.project.onlinestore.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Order {    // 결제하기 버튼 클릭시 생성

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "id")
    private List<OrderItem> orderItems;

    @CreatedDate
    private LocalDateTime orderDate;    // 주문 시각

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "payment_status")
    private Boolean paymentStatus;  // 결제 상태

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "review")
    private Boolean review; // 리뷰 작성 여부

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;
}
