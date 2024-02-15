package com.project.onlinestore.user.repository;

import com.project.onlinestore.user.entity.Cart;
import com.project.onlinestore.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);
}
