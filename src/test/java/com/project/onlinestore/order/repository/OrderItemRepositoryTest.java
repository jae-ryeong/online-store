package com.project.onlinestore.order.repository;

import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.Item.entity.enums.Category;
import com.project.onlinestore.Item.repository.ItemRepository;
import com.project.onlinestore.order.Entity.Order;
import com.project.onlinestore.order.Entity.OrderItem;
import com.project.onlinestore.order.Entity.enums.OrderStatus;
import com.project.onlinestore.user.entity.Address;
import com.project.onlinestore.user.entity.Cart;
import com.project.onlinestore.user.entity.ItemCart;
import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.entity.enums.RoleType;
import com.project.onlinestore.user.repository.CartRepository;
import com.project.onlinestore.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderItemRepositoryTest {

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void existsByOrderAndItem() {
        //given
        Cart cart1 = createCart();
        cartRepository.save(cart1);
        User seller = sellerUser(cart1);
        userRepository.save(seller);
        Item item = createItem(seller);
        itemRepository.save(item);
        Item item2 = Item.builder()
                .user(seller)
                .itemName("item3").count(0L).category(Category.BOOK).quantity(200).price(30000).build();
        itemRepository.save(item2);

        Cart cart2 = createCart();
        cartRepository.save(cart2);
        User customer = customerUser(cart2);
        userRepository.save(customer);

        Order order = Order.builder().user(customer).build();
        orderRepository.save(order);

        OrderItem orderItem = OrderItem.builder().item(item).count(1).orderPrice(10000).order(order).build();
        orderItemRepository.save(orderItem);
        OrderItem orderItem2 = OrderItem.builder().item(item2).count(1).orderPrice(10000).order(order).build();
        orderItemRepository.save(orderItem2);

        //when
        boolean result = orderItemRepository.existsByOrderAndItem(order.getId(), item.getId());

        //then
        assertThat(result).isTrue();
    }

    private User sellerUser(Cart cart) {
        return User.builder().userName("seller")
                .password("1234")
                .cart(cart)
                .storeName("회사")
                .roleType(RoleType.SELLER)
                .build();
    }

    private User customerUser(Cart cart) {
        return User.builder().userName("customer")
                .password("1234")
                .cart(cart)
                .roleType(RoleType.CUSTOMER)
                .build();
    }

    private Item createItem(User user) {
        return Item.builder()
                .user(user)
                .itemName("item").quantity(100).count(0L).price(10000).category(Category.PET).build();
    }

    private Cart createCart() {
        return Cart.builder()
                .build();
    }
}