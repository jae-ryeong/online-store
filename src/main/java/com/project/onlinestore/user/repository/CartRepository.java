package com.project.onlinestore.user.repository;

import com.project.onlinestore.user.entity.Cart;
import com.project.onlinestore.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);

    @Modifying
    @Query("update cart c set c.user = :user where :userId = c.id")
    void updateCartByUser(@Param("user") User user,@Param("userId") Long userId);
}
