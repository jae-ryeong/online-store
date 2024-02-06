package com.project.onlinestore.Item.repository;

import com.project.onlinestore.Item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
