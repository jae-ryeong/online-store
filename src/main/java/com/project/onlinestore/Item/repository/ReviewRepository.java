package com.project.onlinestore.Item.repository;

import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.Item.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Modifying(clearAutomatically = true)   //  update쿼리 이후 영속성 컨텍스트를 초기화
    @Query("update review r set r.content = :content where r.id = :reviewId")
    void updateReviewContent(@Param("content") String content, @Param("reviewId") Long reviewId);

    boolean existsByItemAndCreatedBy(Item item, String createdBy);

    void deleteAllByItem(Item item);
}
