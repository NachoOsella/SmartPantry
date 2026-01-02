package SmartPantry.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security configuration for password encoding and decoding.
 */
@Configuration
public class SecurityConfig {

    /**
     * Creates a BCrypt password encoder bean for secure password hashing.
     *
     * @return BCryptPasswordEncoder with default strength of 10
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
