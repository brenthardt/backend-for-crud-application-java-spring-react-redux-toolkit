package org.example.crud_with_redux_postgresql_backend.service;



import org.example.crud_with_redux_postgresql_backend.dto.ProductDto;
import org.example.crud_with_redux_postgresql_backend.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public interface ProductService {
    List<ProductDto> findAll();
    Product save(String title, Integer price, List<MultipartFile> images);
    void delete(UUID id);
    Product update(UUID id, String title, Integer price, List<MultipartFile> images);
}
