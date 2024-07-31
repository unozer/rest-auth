package it.gp.rest_auth.service;

import com.fasterxml.jackson.databind.deser.impl.CreatorCollector;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import it.gp.rest_auth.model.RoleEntity;
import it.gp.rest_auth.model.UserEntity;
import it.gp.rest_auth.repository.RoleJpaRepository;
import it.gp.rest_auth.repository.UserJpaRepository;
import it.gp.rest_auth.utils.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

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

    @Override
    public Map<String, String> refreshToken(String authHeader, String issuer) throws BadJOSEException, ParseException, JOSEException {
        String refreshToken = authHeader.substring("Bearer ".length());
        UsernamePasswordAuthenticationToken authenticationToken = JwtUtil.parseToken(refreshToken);
        String username = authenticationToken.getName();
        UserEntity userEntity = findByUsername(username);
        List<String> roles = userEntity.getRoles().stream().map(RoleEntity::getName).toList();
        String accessToken = JwtUtil.createAccessToken(username, issuer, roles);
        return Map.of("access_token", accessToken, "refresh_token", refreshToken);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userJpaRepository.findByUsername(username);
        if (user == null) {
            String message = String.format("User not found with username: %s", username);
            log.error(message);
            throw new UsernameNotFoundException(message);
        } else {
            log.debug("User found in database: {}", username);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            user.getRoles().forEach(role ->
                    authorities.add(new SimpleGrantedAuthority(role.getName()))
            );
            return new User(user.getUsername(), user.getPassword(), authorities);
        }
    }
}
