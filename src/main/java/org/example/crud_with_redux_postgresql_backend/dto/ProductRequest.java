package org.example.crud_with_redux_postgresql_backend.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductRequest {
    private String title;
    private Integer price;
    private List<MultipartFile> images;
}

