package org.example.crud_with_redux_postgresql_backend.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.crud_with_redux_postgresql_backend.entity.User;
import org.example.crud_with_redux_postgresql_backend.repository.RoleRepository;
import org.example.crud_with_redux_postgresql_backend.repository.UserRepository;
import org.example.crud_with_redux_postgresql_backend.role.Role;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initAdminUser() {

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN")));

        String superAdminPhone = "1056";
        if (userRepository.findByPhone(superAdminPhone).isEmpty()) {
            User superAdmin = new User();
            superAdmin.setName("Admin");
            superAdmin.setPhone(superAdminPhone);
            superAdmin.setPassword(passwordEncoder.encode("1056"));
            superAdmin.setRoles(List.of(adminRole));

            userRepository.save(superAdmin);
            System.out.println("Admin user created with phone '1056' and password '1056'");
        }

    }
}

