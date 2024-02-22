package com.project.onlinestore.user.repository;

import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.Item.entity.enums.Category;
import com.project.onlinestore.Item.repository.ItemRepository;
import com.project.onlinestore.user.entity.Cart;
import com.project.onlinestore.user.entity.ItemCart;
import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.entity.enums.RoleType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemCartRepositoryTest {

    @Autowired
    private ItemCartRepository itemCartRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;

    @Test
    void checkTrue() {  // 쿼리 확인 테스트
        //given
        Cart cart = createCart();
        User sellerUser = sellerUser();
        Item item = createItem(sellerUser);
        ItemCart itemCart = createItemCart(cart, item);

        //when
        itemCartRepository.checkTrue(itemCart.getId());
    }

    @Test
    void checkFalse() {  // 쿼리 확인 테스트
        //given
        Cart cart = createCart();
        User sellerUser = sellerUser();
        Item item = createItem(sellerUser);
        ItemCart itemCart = createItemCart(cart, item);

        //when
        itemCartRepository.checkFalse(itemCart.getId());
    }

    private User sellerUser() {
        return User.builder().userName("seller")
                .password("1234")
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
                .itemName("item").quantity(100).price(10000).count(10000L).category(Category.PET).build();
    }

    private ItemCart createItemCart(Cart cart, Item item) {
        return ItemCart.builder()
                .cart(cart).item(item).cartCheck(true).quantity(1).build();
    }

    private Cart createCart() {
        return Cart.builder()
                .build();
    }
}