package org.example.crud_with_redux_postgresql_backend.service;

import org.example.crud_with_redux_postgresql_backend.entity.Image;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ImageService {
    List<Image> findAll();
}
