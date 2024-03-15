package com.project.onlinestore.Item.repository;

import com.project.onlinestore.Item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Modifying//(clearAutomatically = true)
    @Query("update item i set i.count = i.count + :count, i.quantity = i.quantity - :count where i.id = :itemId")
    void itemCountAndQuantityUpdate(@Param("count")Integer count, @Param("itemId")Long itemId);
}
