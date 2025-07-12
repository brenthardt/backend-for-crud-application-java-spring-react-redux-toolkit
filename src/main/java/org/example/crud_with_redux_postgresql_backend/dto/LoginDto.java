package org.example.crud_with_redux_postgresql_backend.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String phone;
    private String password;
}

