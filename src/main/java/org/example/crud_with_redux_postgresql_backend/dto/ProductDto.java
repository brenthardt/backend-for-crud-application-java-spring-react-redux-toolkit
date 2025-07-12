package org.example.crud_with_redux_postgresql_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private UUID id;
    private String title;
    private Integer price;
    private List<ImageDto> images;
}

