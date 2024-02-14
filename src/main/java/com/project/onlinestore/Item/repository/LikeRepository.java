package com.project.onlinestore.Item.repository;

import com.project.onlinestore.Item.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByItem_IdAndUser_Id(Long item_id, Long user_id);

    void deleteByItem_IdAndUser_Id(Long item_id, Long user_id);
}
