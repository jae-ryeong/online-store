package com.project.onlinestore.Item.repository;

import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.Item.entity.Review;
import com.project.onlinestore.Item.entity.enums.Category;
import com.project.onlinestore.user.entity.Cart;
import com.project.onlinestore.user.entity.ItemCart;
import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.entity.enums.RoleType;
import com.project.onlinestore.user.repository.CartRepository;
import com.project.onlinestore.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void updateReviewContentTest() {
        //given
        Cart cart = createCart();
        cartRepository.save(cart);
        User seller = sellerUser(cart);
        userRepository.save(seller);
        Item item = createItem(seller);
        itemRepository.save(item);
        Review review = Review.builder().item(item).content("리뷰").build();
        reviewRepository.save(review);

        //when
        reviewRepository.updateReviewContent("리뷰2", review.getId());
        Optional<Review> result = reviewRepository.findById(review.getId());

        //then
        assertThat(result.get().getContent()).isEqualTo("리뷰2"); // @Modifying(clearAutomatically = true)으로 update쿼리 이후 영속성 컨텍스트를 초기화시켜 영속성 컨텍스트가 비어있으니, DB에서 조회한다.
    }

    private User sellerUser(Cart cart) {
        return User.builder().userName("seller")
                .password("1234")
                .cart(cart)
                .storeName("회사")
                .roleType(RoleType.SELLER)
                .build();
    }

    private Item createItem(User user) {
        return Item.builder()
                .user(user)
                .itemName("item").quantity(100).price(10000).category(Category.PET).build();
    }

    private Cart createCart() {
        return Cart.builder()
                .build();
    }
}