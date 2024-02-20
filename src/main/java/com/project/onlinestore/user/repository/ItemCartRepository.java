package com.project.onlinestore.user.repository;

import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.user.entity.Cart;
import com.project.onlinestore.user.entity.ItemCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemCartRepository extends JpaRepository<ItemCart, Long> {
    List<ItemCart> findAllByCart(Cart cart);

    boolean existsByCartAndItem(Cart cart, Item item);

    ItemCart findByCartAndItem(Cart cart, Item item);

    @Modifying
    @Query("update item_cart i set i.quantity = i.quantity+1 where i.cart = :cart and  i.item = :item")
    void addQuantity(@Param("cart")Cart cart, @Param("item")Item item);

    @Modifying
    @Query("update item_cart i set i.cartCheck = true where i.id = :itemCartId")
    void checkTrue(@Param("itemCartId")Long itemCardId);

    @Modifying
    @Query("update item_cart i set i.cartCheck = false where i.id = :itemCartId")
    void checkFalse(@Param("itemCartId")Long itemCardId);
}
