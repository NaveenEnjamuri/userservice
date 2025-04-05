package com.examportal.repository;

import com.examportal.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserProfileRepository extends JpaRepository<UserProfile, Long> {
}
