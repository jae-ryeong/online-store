package com.project.onlinestore.user.repository;

import com.project.onlinestore.user.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
