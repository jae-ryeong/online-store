package com.project.onlinestore.Item.repository;

import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.Item.entity.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Modifying
    @Query("update item i set i.soldCount = i.soldCount + :count, i.quantity = i.quantity - :count where i.id = :itemId")
    void itemCountAndQuantityUpdate(@Param("count")Integer count, @Param("itemId")Long itemId);

    @Modifying
    @Query("update item i set i.soldOut = true where i.id = :itemId")
    void itemSoldOutTrue(@Param("itemId")Long itemId);

    @Modifying
    @Query("update item i set i.soldOut = false where i.id = :itemId")
    void itemSoldOutFalse(@Param("itemId")Long itemId);

    @Modifying
    @Query("update item i set i.quantity = i.quantity + :quantity where i.id =:itemId")
    void itemQuantityUpdate(@Param("quantity")Integer quantity, @Param("itemId")Long itemId);

    Page<Item> findAllByCategory(Category category, Pageable pageable);
}
