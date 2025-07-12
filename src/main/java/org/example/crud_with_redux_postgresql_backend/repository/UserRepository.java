package org.example.crud_with_redux_postgresql_backend.repository;

import org.example.crud_with_redux_postgresql_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

boolean existsByName(String name);
    Optional<User> findByPhone(String phone);
    boolean existsByPhone(String phone);
    Optional<User> findByRefreshToken(String refreshToken);

}
