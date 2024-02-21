package com.project.onlinestore.user.service;

import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.Item.repository.ItemRepository;
import com.project.onlinestore.exception.ApplicationException;
import com.project.onlinestore.exception.ErrorCode;
import com.project.onlinestore.user.dto.response.AddCartResponseDto;
import com.project.onlinestore.user.dto.response.CartCheckResponseDto;
import com.project.onlinestore.user.dto.response.CartViewResponseDto;
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

        if (itemCartRepository.existsByCartAndItem(cart, item)) {   // 중복 체크
            ItemCart itemCart = itemCartRepository.findByCartAndItem(cart, item);
            itemCartRepository.addQuantity(cart, item); // 중복된 제품을 장바구니 한번 더 넣을 경우 구매 수량을 +1 한다.
            return new AddCartResponseDto(
                    itemId, user.getId(), item.getUser().getId(), item.getItemName(), item.getUser().getStoreName(), itemCart.getQuantity()+1
            );
        }

        itemCartRepository.save(
                ItemCart.builder()
                        .cart(cart)
                        .item(item)
                        .quantity(1)    // 장바구니에 넣을 시 기본 구매 수량은 1
                        .cartCheck(true)    // 기본은 체크되어 있는 상태로
                        .build()
        );

        return new AddCartResponseDto(
                itemId, user.getId(), item.getUser().getId(), item.getItemName(), item.getUser().getStoreName(), 1
        );
    }

    public List<CartViewResponseDto> allItemCartView(String userName) {
        User user = findUser(userName);

        List<ItemCart> itemCarts = itemCartRepository.findAllByCart(user.getCart());
        return itemCarts.stream().map(CartViewResponseDto::fromEntity).toList();
    }

    @Transactional
    public CartCheckResponseDto cartCheck(String userName, Long itemCartId) {
        User user = findUser(userName);

        ItemCart itemCart = findItemCart(itemCartId);

        if (itemCart.getCart() != user.getCart()){
            throw new ApplicationException(ErrorCode.INVALID_USER, "잘못된 유저 접근입니다.");
        }

        if(itemCart.isCartCheck()){
            itemCartRepository.checkFalse(itemCart.getId());
        } else{
            itemCartRepository.checkTrue(itemCartId);
        }

        return new CartCheckResponseDto(itemCartId, !itemCart.isCartCheck());
    }

    @Transactional
    public void delete(String userName, Long itemCartId) {
        User user = findUser(userName);
        ItemCart itemCart = findItemCart(itemCartId);

        if (itemCart.getCart() != user.getCart()){
            throw new ApplicationException(ErrorCode.INVALID_USER, "잘못된 유저 접근입니다.");
        }

        itemCartRepository.deleteById(itemCartId);
    }
    private User findUser(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() ->
                new ApplicationException(ErrorCode.USERNAME_NOT_FOUND, userName + "를 찾을 수 없습니다."));
    }
    private Item findItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new ApplicationException(ErrorCode.ITEM_ID_NOT_FOUND, itemId + "를 찾을 수 없습니다."));
    }

    private ItemCart findItemCart(Long itemCartId) {
        return itemCartRepository.findById(itemCartId).orElseThrow(() ->
                new ApplicationException(ErrorCode.ITEM_CART_NOT_FOUNT, "해당 상품이 장바구니에 존재하지 않습니다.")
        );
    }
}
