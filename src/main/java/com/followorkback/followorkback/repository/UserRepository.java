package com.followorkback.followorkback.repository;

import com.followorkback.followorkback.entity.Role;
import com.followorkback.followorkback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
//    List<User> findByRolesIn(Set<Role> roles, Pageable pageable);
    List<User> findByRolesIsContaining(Role role);
}
