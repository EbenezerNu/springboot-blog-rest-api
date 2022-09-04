package com.springboot.blog.repository;

import com.springboot.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM USERS WHERE USERNAME =:usernameOrEmail OR EMAIL =:usernameOrEmail LIMIT 1", nativeQuery = true)
    Optional<User> findByUsernameOrEmail(String usernameOrEmail);

    Optional<User> findByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);
}
