package br.com.barbosa.config;

import br.com.barbosa.entities.Role;
import br.com.barbosa.entities.User;
import br.com.barbosa.repositories.RoleRepository;
import br.com.barbosa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Configuration
public class DataInitializer {

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Bean
    public CommandLineRunner initUsersAndRoles(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {

        return args -> {
            if (roleRepository.count() == 0) {
                roleRepository.save(new Role("ROLE_ADMIN"));
                roleRepository.save(new Role("ROLE_USER"));
            }

            if (!userRepository.existsByEmail(adminEmail)) {
                Optional<Role> adminRole = roleRepository.findByRoleName("ROLE_ADMIN");
                if (adminRole.isPresent()) {
                    User admin = new User();
                    admin.setName("SysAdmin");
                    admin.setEmail(adminEmail);
                    if (adminPassword.length() < 8 || adminPassword.length() > 50) {
                        throw new IllegalArgumentException("A senha deve ter entre 8 e 50 caracteres.");
                    }
                    admin.setPassword(passwordEncoder.encode(adminPassword));
                    admin.getRoles().add(adminRole.get());

                    userRepository.save(admin);
                } else {
                    throw new RuntimeException("Função de administrador não encontrada.");
                }
            }
        };
    }
}