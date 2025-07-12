package org.example.crud_with_redux_postgresql_backend.impl;

import lombok.RequiredArgsConstructor;

import org.example.crud_with_redux_postgresql_backend.dto.ImageDto;
import org.example.crud_with_redux_postgresql_backend.dto.ProductDto;
import org.example.crud_with_redux_postgresql_backend.entity.Image;
import org.example.crud_with_redux_postgresql_backend.entity.Product;
import org.example.crud_with_redux_postgresql_backend.repository.ProductRepository;
import org.example.crud_with_redux_postgresql_backend.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

private final ProductRepository productRepository;

@Override
@Transactional
    public List<ProductDto> findAll() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(product -> new ProductDto(
                product.getId(),
                product.getTitle(),
                product.getPrice(),
                product.getImages().stream()
                        .map(image -> new ImageDto(image.getId(), image.getTitle(), image.getPath()))
                        .collect(Collectors.toList())
        )).collect(Collectors.toList());
    }

    @Override
    public Product save(String title, Integer price, List<MultipartFile> images) {
        Product product = new Product();
        product.setTitle(title);
        product.setPrice(price);

        rabbit(images, product);
        System.out.println("HERE IS THE PRODUCT: "+product);
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void delete(UUID id) {

        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        for (Image image : product.getImages()) {
            try {
                Path imagePath = Paths.get(image.getPath());
                Files.deleteIfExists(imagePath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete image file", e);
            }
        }

        productRepository.delete(product);
    }

    @Override
    public Product update(UUID id, String title, Integer price, List<MultipartFile> images) {
        Product product = productRepository.findById(id).orElseThrow();

        product.setTitle(title);
        product.setPrice(price);

        if (images != null && !images.isEmpty()) {
            rabbit(images, product);
        }

        return productRepository.save(product);
    }

    public void rabbit(List<MultipartFile> images, Product product) {
        List<Image> newImages = new ArrayList<>();
        for (MultipartFile file : images) {
            String path = saveFile(file);
            Image image = new Image(file.getOriginalFilename(), path, product);
            newImages.add(image);
        }
        product.setImages(newImages);
    }

    public String saveFile(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get("files/" + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            return path.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

}
