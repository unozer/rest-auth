package it.gp.rest_auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gp.rest_auth.model.RoleDTO;
import it.gp.rest_auth.model.UserEntity;
import it.gp.rest_auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserEntity>> findAll() {
        return ResponseEntity.ok().body(userService.findAll());
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserEntity> findByUsername(@PathVariable String username) {
        UserEntity user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(user);
    }

    @PostMapping
    public ResponseEntity<UserEntity> save(@RequestBody UserEntity user) {
        UserEntity savedUser = userService.save(user);
        return ResponseEntity.ok().body(savedUser);
    }

    @PostMapping("/{username}/roles")
    public ResponseEntity<?> addRoleToUser(@PathVariable String username, @RequestBody RoleDTO role) {
        UserEntity user = userService.addRoleToUser(username, role.getRoleName());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                Map<String, String> tokenMap = userService.refreshToken(authHeader, request.getRequestURL().toString());
                response.addHeader("access_token", tokenMap.get("access_token"));
                response.addHeader("refresh_token", tokenMap.get("refresh_token"));
            } catch (Exception e) {
                log.error(String.format("Error refresh token: %s"), e);
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("errorMessage", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}
