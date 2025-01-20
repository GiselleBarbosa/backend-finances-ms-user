package br.com.barbosa.config;

import br.com.barbosa.entities.Role;
import br.com.barbosa.entities.User;
import br.com.barbosa.repositories.RoleRepository;
import br.com.barbosa.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Configuration
public class DataInitializer {

    @Value("${admin-credentials.email}")
    private String adminEmail;

    @Value("${admin-credentials.password}")
    private String adminPassword;

    Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner initUsersAndRoles(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        logger.info("adminEmail: " + adminEmail);
        logger.info("adminPassword: " + adminPassword);

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