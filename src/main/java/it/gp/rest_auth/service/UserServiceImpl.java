package it.gp.rest_auth.service;

import it.gp.rest_auth.model.RoleEntity;
import it.gp.rest_auth.model.UserEntity;
import it.gp.rest_auth.repository.RoleJpaRepository;
import it.gp.rest_auth.repository.UserJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;

    @Override
    public UserEntity save(UserEntity user) {
        log.info("Saving user {} to the database", user.getUsername());
        return userJpaRepository.save(user);
    }

    @Override
    public UserEntity addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}", roleName, username);
        UserEntity user = userJpaRepository.findByUsername(username);
        RoleEntity role = roleJpaRepository.findByName(roleName);
        user.getRoles().add(role);
        return user;
    }

    @Override
    public UserEntity findByUsername(String username) {
        log.info("Retrieving user {}", username);
        return userJpaRepository.findByUsername(username);
    }

    @Override
    public List<UserEntity> findAll() {
        log.info("Retrieving all users");
        return userJpaRepository.findAll();
    }
}
