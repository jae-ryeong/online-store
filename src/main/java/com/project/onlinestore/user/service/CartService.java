package com.project.onlinestore.user.service;

import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.Item.repository.ItemRepository;
import com.project.onlinestore.exception.ApplicationException;
import com.project.onlinestore.exception.ErrorCode;
import com.project.onlinestore.user.dto.response.AddCartResponseDto;
import com.project.onlinestore.user.entity.Cart;
import com.project.onlinestore.user.entity.ItemCart;
import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.repository.CartRepository;
import com.project.onlinestore.user.repository.ItemCartRepository;
import com.project.onlinestore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final ItemCartRepository itemCartRepository;

    @Transactional
    public AddCartResponseDto addCart(String userName, Long itemId) {
        User user = findUser(userName);
        Item item = findItem(itemId);

        Cart cart = cartRepository.findById(user.getCart().getId()).orElseThrow(() ->
                        new ApplicationException(ErrorCode.CART_NOT_FOUND, userName + "의 cart를 찾을 수 없습니다."));

        /*if (cart.getUser() != null) { // 굳이 필요 없어 보인다, 내 생각이 맞으면
            cartRepository.updateCartByUser(user, user.getId());
        }*/

        // TODO: 중복 체크 해주기
        itemCartRepository.save(
                ItemCart.builder()
                        .cart(cart)
                        .item(item)
                        .build()
        );

        System.out.println("user.getCart().getItemCarts().get(0).getItem() = " + user.getCart().getItemCarts().get(0).getItem().getItemName());

        return new AddCartResponseDto(
                itemId, user.getId(), item.getUser().getId(), item.getItemName(), item.getUser().getStoreName()
        );
    }

    public List<ItemCart> allItemCartSearch(String userName) {
        User user = findUser(userName);
        List<ItemCart> allCart = itemCartRepository.findByCart_Id(user.getCart().getId());
        return allCart;
    }
    private User findUser(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() ->
                new ApplicationException(ErrorCode.USERNAME_NOT_FOUND, userName + "를 찾을 수 없습니다."));
    }
    private Item findItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new ApplicationException(ErrorCode.ITEM_ID_NOT_FOUND, itemId + "를 찾을 수 없습니다."));
    }
}
