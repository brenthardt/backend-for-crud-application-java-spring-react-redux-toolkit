package org.example.crud_with_redux_postgresql_backend.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long chatId;
    private String name;
    private String password;
    private String phone;

}
