package org.example.crud_with_redux_postgresql_backend.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.crud_with_redux_postgresql_backend.repository.RoleRepository;
import org.example.crud_with_redux_postgresql_backend.role.Role;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RoleInitializer {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void initRoles() {
        List<String> roles = List.of("ROLE_USER", "ROLE_ADMIN");

        for (String roleName : roles) {
            roleRepository.findByName(roleName)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName(roleName);
                        return roleRepository.save(role);
                    });
        }

        System.out.println("✅ Roles initialized: ROLE_ADMIN, ROLE_USER");
    }
}

