package it.gp.rest_auth.service;

import it.gp.rest_auth.model.UserEntity;

import java.util.List;

public interface UserService {

    UserEntity save(UserEntity user);
    UserEntity addRoleToUser(String username, String roleName);
    UserEntity findByUsername(String username);
    List<UserEntity> findAll();

}
