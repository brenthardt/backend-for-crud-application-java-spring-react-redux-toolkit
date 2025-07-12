package org.example.crud_with_redux_postgresql_backend.impl;

import lombok.RequiredArgsConstructor;

import org.example.crud_with_redux_postgresql_backend.entity.Image;
import org.example.crud_with_redux_postgresql_backend.repository.ImageRepository;
import org.example.crud_with_redux_postgresql_backend.service.ImageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

private final ImageRepository imageRepository;

    @Override
    public List<Image> findAll() {
        return imageRepository.findAll();
    }
}
