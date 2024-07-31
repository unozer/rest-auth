package it.gp.rest_auth.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import it.gp.rest_auth.model.UserEntity;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface UserService {

    UserEntity save(UserEntity user);
    UserEntity addRoleToUser(String username, String roleName);
    UserEntity findByUsername(String username);
    List<UserEntity> findAll();
    Map<String, String> refreshToken(String authHeader, String issuer) throws BadJOSEException, ParseException, JOSEException;
}
