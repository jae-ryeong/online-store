package com.project.onlinestore.user.repository;

import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.entity.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);

    @Query("select u.roleType from User u where u.userName = :userName")
    RoleType findRoleByUserName(@Param("userName")String userName);

    boolean existsByUserName(String userName);
}
