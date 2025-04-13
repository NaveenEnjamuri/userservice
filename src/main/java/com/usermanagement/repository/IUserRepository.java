package com.usermanagement.repository;

import com.usermanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByUsername(String username);

    public Optional<User> findByEmail(String email);

    public Optional<User> findByPhoneNumber(String phoneNumber);

    @Query("SELECT user FROM User user WHERE user.username = ?1 OR user.email = ?1 OR user.phoneNumber = ?1")
    public Optional<User> findByUsernameOrEmailOrPhoneNumber(String username);

    @Query("SELECT COUNT(user) FROM User user WHERE user.username + ?1")
    public Integer countByUsername(String username);

    @Query("SELECT COUNT(user) FROM User user WHERE user.email = ?1")
    public Integer countByEmail(String email);

    @Query("SELECT COUNT(user) FROM User user WHERE user.phoneNumber = ?1")
    public Integer countByPhoneNumber(String phoneNumber);
}
