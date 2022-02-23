package com.followorkback.followorkback.service;

import com.followorkback.followorkback.entity.Role;
import com.followorkback.followorkback.entity.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    User updateUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String rolename);
    User getUser(String username);
    User getUserById(long id);
    List<User> getUsers();
    void deleteUserById(long id);
}
