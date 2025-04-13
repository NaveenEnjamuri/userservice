package com.usermanagement.repository;

import com.usermanagement.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {

    public Optional<Role> findByRoleName(String roleName);
}
