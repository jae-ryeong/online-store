package com.project.onlinestore.order.repository;

import com.project.onlinestore.order.Entity.Order;
import com.project.onlinestore.order.Entity.enums.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void updateOrderStatusCancel() {
        //given
        Order order = Order.builder().orderStatus(OrderStatus.ORDER).build();
        orderRepository.save(order);

        //when
        orderRepository.updateOrderStatusCancel(OrderStatus.CANCEL, order.getId());

        //then
        Optional<Order> result = orderRepository.findById(order.getId());
        assertThat(result.get().getOrderStatus()).isEqualTo(OrderStatus.CANCEL);
    }
}