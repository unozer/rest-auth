package it.gp.rest_auth.controller;

import it.gp.rest_auth.model.RoleEntity;
import it.gp.rest_auth.model.UserEntity;
import it.gp.rest_auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<?> addRoleToUser(@PathVariable String username, @RequestBody RoleEntity role) {
        UserEntity user = userService.addRoleToUser(username, role.getName());
        return ResponseEntity.ok(user);
    }
}
