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

import java.util.List;
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

    @Test
    void findAllCheckedCartTest() {
        //given
        Cart cart = createCart();
        cartRepository.save(cart);

        User sellerUser = sellerUser();
        userRepository.save(sellerUser);

        Item item1 = createItem(sellerUser);
        itemRepository.save(item1);

        ItemCart itemCart = createItemCart(cart, item1);
        itemCartRepository.save(itemCart);

        Item item2 = Item.builder()
                .user(sellerUser)
                .itemName("item2").quantity(100).price(10000).count(10000L).category(Category.PET).build();
        itemRepository.save(item2);

        ItemCart uncheckedItemCart = createUncheckedItemCart(cart, item2);
        itemCartRepository.save(uncheckedItemCart);

        //when
        List<ItemCart> allCheckedCart = itemCartRepository.findAllCheckedCart(cart);

        //then
        assertThat(allCheckedCart.size()).isEqualTo(1);
        assertThat(allCheckedCart.get(0).getItem().getItemName()).isEqualTo("item");
    }

    @Test
    public void addQuantityTest() throws Exception{
        //given
        Cart cart = createCart();
        cartRepository.save(cart);

        User sellerUser = sellerUser();
        userRepository.save(sellerUser);

        Item item1 = createItem(sellerUser);
        itemRepository.save(item1);

        ItemCart itemCart = createItemCart(cart, item1);
        itemCartRepository.save(itemCart);

        //when
        itemCartRepository.addQuantity(cart, item1);
        ItemCart result = itemCartRepository.findById(itemCart.getId()).get();

        //then
        assertThat(result.getQuantity()).isEqualTo(2);
    }

    @Test
    public void minusQuantityTest() throws Exception{
        //given
        Cart cart = createCart();
        cartRepository.save(cart);

        User sellerUser = sellerUser();
        userRepository.save(sellerUser);

        Item item1 = createItem(sellerUser);
        itemRepository.save(item1);

        ItemCart itemCart = createItemCart(cart, item1);
        itemCartRepository.save(itemCart);

        //when
        itemCartRepository.minusQuantity(cart, item1);
        ItemCart result = itemCartRepository.findById(itemCart.getId()).get();

        //then
        assertThat(result.getQuantity()).isEqualTo(0);
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

    private ItemCart createUncheckedItemCart(Cart cart, Item item) {
        return ItemCart.builder()
                .cart(cart).item(item).cartCheck(false).quantity(100).build();
    }
}
