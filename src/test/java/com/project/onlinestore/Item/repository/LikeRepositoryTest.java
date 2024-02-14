package com.project.onlinestore.Item.repository;

import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.Item.entity.Like;
import com.project.onlinestore.Item.entity.enums.Category;
import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.entity.enums.RoleType;
import com.project.onlinestore.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DataJpaTest
class LikeRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private LikeRepository likeRepository;

    @BeforeEach
    void setUp() {
        /*User user = User.builder()
                .userName("test")
                .password("1234")
                .storeName("회사")
                .roleType(RoleType.SELLER)
                .build();

        userRepository.save(user);

        Item item = Item.builder()
                .itemName("item")
                .count(100L)
                .price(10000)
                .category(Category.PET)
                .quantity(1000)
                .soldOut(false)
                .build();

        itemRepository.save(item);*/
    }

    @Test
    void existsByItem_IdAndUser_Id() {
        // given
        User user = User.builder()
                .userName("test")
                .password("1234")
                .storeName("회사")
                .roleType(RoleType.SELLER)
                .build();
        userRepository.save(user);

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

        // when
        likeRepository.save(Like.builder().user(user).item(item).build());

        boolean bool = likeRepository.existsByItem_IdAndUser_Id(item.getId(), user.getId());

        // then
        assertThat(likeRepository.count()).isEqualTo(1);
        assertThat(bool).isTrue();
    }

    @Test
    void deleteByItem_IdAndUser_Id() {
        // given
        User user = User.builder()
                .userName("test")
                .password("1234")
                .storeName("회사")
                .roleType(RoleType.SELLER)
                .build();
        userRepository.save(user);

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

        // when
        likeRepository.save(Like.builder().user(user).item(item).build());

        likeRepository.deleteByItem_IdAndUser_Id(item.getId(), user.getId());
        boolean bool = likeRepository.existsByItem_IdAndUser_Id(item.getId(), user.getId());

        // then
        assertThat(likeRepository.count()).isEqualTo(0);
        assertThat(bool).isFalse();
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

    private Item createItem() {
        Item item = Item.builder()
                .itemName("item")
                .user(sellerUser())
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