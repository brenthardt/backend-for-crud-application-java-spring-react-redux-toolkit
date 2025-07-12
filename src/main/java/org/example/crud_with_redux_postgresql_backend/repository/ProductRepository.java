package org.example.crud_with_redux_postgresql_backend.repository;

import org.example.crud_with_redux_postgresql_backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
}
