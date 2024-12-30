package br.com.barbosa.config;

import br.com.barbosa.entities.Role;
import br.com.barbosa.entities.User;
import br.com.barbosa.repositories.RoleRepository;
import br.com.barbosa.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initUsersAndRoles(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            if (roleRepository.count() == 0) {
                Role adminRole = roleRepository.save(new Role("ROLE_ADMIN"));
                Role userRole = roleRepository.save(new Role("ROLE_USER"));

                if (userRepository.count() == 0) {
                    User admin = new User();
                    admin.setName("SysAdmin");
                    admin.setEmail("sysadmin@domain.com");
                    admin.setPassword(passwordEncoder.encode("admin123"));
                    admin.getRoles().add(adminRole);
                    userRepository.save(admin);
                }
            }
        };
    }
}
