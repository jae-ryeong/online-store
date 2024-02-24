package com.project.onlinestore.Item.repository;

import com.project.onlinestore.Item.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
