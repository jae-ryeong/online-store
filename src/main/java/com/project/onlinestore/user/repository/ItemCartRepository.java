package com.project.onlinestore.user.repository;

import com.project.onlinestore.user.entity.ItemCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemCartRepository extends JpaRepository<ItemCart, Long> {
}
