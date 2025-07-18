package org.example.crud_with_redux_postgresql_backend.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

import org.example.crud_with_redux_postgresql_backend.dto.LoginDto;
import org.example.crud_with_redux_postgresql_backend.dto.LoginResponse;
import org.example.crud_with_redux_postgresql_backend.dto.UserDto;
import org.example.crud_with_redux_postgresql_backend.entity.User;
import org.example.crud_with_redux_postgresql_backend.jwt.JwtUtil;
import org.example.crud_with_redux_postgresql_backend.repository.RoleRepository;
import org.example.crud_with_redux_postgresql_backend.repository.UserRepository;
import org.example.crud_with_redux_postgresql_backend.role.Role;
import org.example.crud_with_redux_postgresql_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public ResponseEntity<?> findAll() {
       return ResponseEntity.ok( userRepository.findAll());
    }

    @Override
    public User save(User user) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
            user.setRoles(List.of(userRole));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public ResponseEntity<?> delete(UUID id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok("User deleted");
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<?> update(UUID id, User user) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setPassword(user.getPassword());
                    existingUser.setPhone(user.getPhone());
                    existingUser.setRoles(user.getRoles());

                    return ResponseEntity.ok(userRepository.save(existingUser));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<?> signUp(UserDto userDto) {
        if (userRepository.existsByPhone(userDto.getPhone())) {
            return ResponseEntity.badRequest().body("User already exists with this phone");
        }

        User user = new User();
        user.setName(userDto.getName());
        user.setPhone(userDto.getPhone());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));



        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
        user.setRoles(List.of(userRole));

        User savedUser = userRepository.save(user);

        String token = jwtUtil.generateToken(savedUser.getPhone(), "ROLE_USER");
        String refreshToken = jwtUtil.generateRefreshToken(savedUser.getPhone(), "ROLE_USER");

        savedUser.setRefreshToken(refreshToken);
        userRepository.save(savedUser);

        LoginResponse response = new LoginResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getPhone(),
                "ROLE_USER",
                token,
                refreshToken
        );

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> login(LoginDto loginDTO) {
        return userRepository.findByPhone(loginDTO.getPhone())
                .map(user -> {
                    if (passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
                        String mainRole = user.getRoles().stream()
                                .findFirst()
                                .map(Role::getName)
                                .orElse("UNKNOWN");

                        String token = jwtUtil.generateToken(user.getPhone(), mainRole);
                        String refreshToken = jwtUtil.generateRefreshToken(user.getPhone(), mainRole);



                        user.setRefreshToken(refreshToken);
                        userRepository.save(user);

                        LoginResponse response = new LoginResponse(
                                user.getId(),
                                user.getName(),
                                user.getPhone(),
                                mainRole,
                                token,
                                refreshToken
                        );

                        return ResponseEntity.ok(response);
                    } else {
                        return ResponseEntity.badRequest().body("Invalid password");
                    }
                })
                .orElse(ResponseEntity.badRequest().body("User not found"));
    }

    @Override
    public ResponseEntity<?> deleteByPhone(String phone) {
        return userRepository.findByPhone(phone)
                .map(user -> {
                    userRepository.delete(user);
                    return ResponseEntity.ok(" User eliminated");
                })
                .orElseGet(() -> ResponseEntity.badRequest().body("User not found"));
    }

    @Override
    public ResponseEntity<?> logout(String phone) {
        return ResponseEntity.ok("User logged out");
    }

    @Override
    public ResponseEntity<?> refreshToken(String refreshToken) {
        try {
            Claims claims = jwtUtil.getClaims(refreshToken);
            String phone = claims.getSubject();
            String role = claims.get("role", String.class);

            User user = userRepository.findByRefreshToken(refreshToken)
                    .orElseThrow(() -> new RuntimeException("Invalid refresh token (not found in DB)"));

            if (!user.getPhone().equals(phone)) {
                return ResponseEntity.status(401).body("Token subject mismatch");
            }

            String newAccessToken = jwtUtil.generateToken(phone, role);

            String newRefreshToken = jwtUtil.generateRefreshToken(phone, role);
            user.setRefreshToken(newRefreshToken);
            userRepository.save(user);

            System.out.println("New tokens generated and saved");

            return ResponseEntity.ok(
                    new LoginResponse(
                            user.getId(),
                            user.getName(),
                            user.getPhone(),
                            role,
                            newAccessToken,
                            newRefreshToken
                    )
            );

        } catch (Exception e) {
            System.out.println(" Exception during refresh: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(403).body("Refresh token expired or invalid");
        }
    }


}
