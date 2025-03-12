package com.project.onlinestore.order.Entity;

import com.project.onlinestore.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Table(name = "payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Payment {  // 결제가 완료될 시 생성

    @Id
    @Column(name = "payment_id")
    private String paymentId; // 프론트에서 생성된 결제 고유 번호

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;    // 주문 테이블과 일대일 (연관관계 주인은 주문)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;      // 사용자

    @Column(name = "order_name")
    private String orderName;   // 상품 이름

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;  // 상품 총 가격

    @Column(name = "payment_status")
    private Boolean paymentStatus;  // 결제 상태

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;    // 주문 시각

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;    // 주문 시각
}
