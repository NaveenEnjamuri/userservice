package com.examportal.repository;

import com.examportal.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {

    public Optional<Role> findByRoleName(String roleName);
}
