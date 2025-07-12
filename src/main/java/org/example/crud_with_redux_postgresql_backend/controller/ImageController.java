package org.example.crud_with_redux_postgresql_backend.controller;

import lombok.RequiredArgsConstructor;

import org.example.crud_with_redux_postgresql_backend.entity.Image;
import org.example.crud_with_redux_postgresql_backend.service.ImageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping
    public List<Image> findAll() {
        return imageService.findAll();
    }

}
