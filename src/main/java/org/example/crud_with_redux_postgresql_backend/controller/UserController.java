package org.example.crud_with_redux_postgresql_backend.controller;

import lombok.RequiredArgsConstructor;

import org.example.crud_with_redux_postgresql_backend.dto.LoginDto;
import org.example.crud_with_redux_postgresql_backend.dto.UserDto;
import org.example.crud_with_redux_postgresql_backend.entity.User;
import org.example.crud_with_redux_postgresql_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User save(@RequestBody User user) {
        return userService.save(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        return userService.delete(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id,@RequestBody User user) {
        return userService.update(id, user);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        return userService.signUp(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto LoginDto) {
        return userService.login(LoginDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> req) {
        return userService.logout(req.get("phone"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> req) {
        return userService.refreshToken(req.get("refreshToken"));
    }



}
