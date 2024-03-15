package com.project.onlinestore.user.repository;

import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.user.entity.Cart;
import com.project.onlinestore.user.entity.ItemCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemCartRepository extends JpaRepository<ItemCart, Long> {
    List<ItemCart> findAllByCart(Cart cart);

    boolean existsByCartAndItem(Cart cart, Item item);

    ItemCart findByCartAndItem(Cart cart, Item item);

    @Modifying
    @Query("update item_cart i set i.quantity = i.quantity+1 where i.cart = :cart and  i.item = :item")
    void addQuantity(@Param("cart")Cart cart, @Param("item")Item item);

    @Modifying(clearAutomatically = true)
    @Query("update item_cart i set i.quantity = i.quantity-1 where i.cart = :cart and  i.item = :item")
    void minusQuantity(@Param("cart")Cart cart, @Param("item")Item item);

    @Modifying
    @Query("update item_cart i set i.cartCheck = true where i.id = :itemCartId")
    void checkTrue(@Param("itemCartId")Long itemCardId);

    @Modifying
    @Query("update item_cart i set i.cartCheck = false where i.id = :itemCartId")
    void checkFalse(@Param("itemCartId")Long itemCardId);

    void deleteAllByItem(Item item);

    @Modifying
    @Query("select i from item_cart i where i.cart = :cart and i.cartCheck = true")
    List<ItemCart> findAllCheckedCart(@Param("cart") Cart cart);
}
