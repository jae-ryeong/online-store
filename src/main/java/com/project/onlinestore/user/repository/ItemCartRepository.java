package com.project.onlinestore.user.repository;

import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.user.entity.ItemCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemCartRepository extends JpaRepository<ItemCart, Long> {
    List<ItemCart> findByCart_Id(Long cartId);
}
