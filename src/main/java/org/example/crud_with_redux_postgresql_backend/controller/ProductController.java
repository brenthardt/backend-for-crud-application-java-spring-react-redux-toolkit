package org.example.crud_with_redux_postgresql_backend.controller;

import lombok.RequiredArgsConstructor;

import org.example.crud_with_redux_postgresql_backend.dto.ProductDto;
import org.example.crud_with_redux_postgresql_backend.entity.Product;
import org.example.crud_with_redux_postgresql_backend.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDto> findAll() {
        return productService.findAll();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<ProductDto> create(
            @RequestParam String title,
            @RequestParam Integer price,
            @RequestParam List<MultipartFile> images
    ) {
        productService.save(title, price, images);
        return productService.findAll();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<ProductDto> update(
            @PathVariable UUID id,
            @RequestParam String title,
            @RequestParam Integer price,
            @RequestParam(required = false) List<MultipartFile> images
    ) {
        productService.update(id, title, price, images);
        return productService.findAll();
    }

    @DeleteMapping("/{id}")
    public List<ProductDto> delete(@PathVariable UUID id) {
        productService.delete(id);
        return productService.findAll();
    }

}
