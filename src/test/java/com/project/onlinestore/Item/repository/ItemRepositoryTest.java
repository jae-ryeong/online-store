package com.project.onlinestore.Item.repository;

import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.Item.entity.enums.Category;
import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.entity.enums.RoleType;
import com.project.onlinestore.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void itemPlusCount() {
        // given
        User user = sellerUser();
        Item item = Item.builder().itemName("item1").count(0L).price(10000).category(Category.BOOK).user(user).quantity(1000).soldOut(false).build();
        itemRepository.save(item);

        // when
        itemRepository.itemCountUpdate(3, item.getId());
        Item result = itemRepository.findById(item.getId()).get();

        // then
        assertThat(result.getCount()).isEqualTo(3);
    }

    private User sellerUser() {
        User user = User.builder()
                .userName("test")
                .password("1234")
                .storeName("회사")
                .roleType(RoleType.SELLER)
                .build();
        userRepository.save(user);
        return user;
    }

    private Item createItem(User user) {
        Item item = Item.builder()
                .itemName("item")
                .user(user)
                .count(100L)
                .price(10000)
                .category(Category.PET)
                .quantity(1000)
                .soldOut(false)
                .build();

        itemRepository.save(item);
        return item;
    }
}