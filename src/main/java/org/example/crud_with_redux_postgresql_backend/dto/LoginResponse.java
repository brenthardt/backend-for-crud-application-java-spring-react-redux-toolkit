package org.example.crud_with_redux_postgresql_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class LoginResponse {
    private UUID id;
    private String name;
    private String phone;
    private String role;
    private String token;
    private String refreshToken;
}

