package com.project.onlinestore.Item.repository;

import com.project.onlinestore.Item.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByItem_IdAndUser_Id(Long itemId, Long userId);
    void deleteByItem_IdAndUser_Id(Long itemId, Long userId);
    Integer countByItem_Id(Long itemId);

    void deleteAllByItem_Id(Long itemId);

    boolean existsByReview_IdAndUser_Id(Long reviewId, Long userId);
    void deleteByReview_IdAndUser_Id(Long reviewId, Long userId);
    Integer countByReview_Id(Long reviewId);

    void deleteAllByReview_Id(Long reviewId);

}
