package SmartPantry.demo.services;

import SmartPantry.demo.dtos.requests.LoginRequest;
import SmartPantry.demo.dtos.requests.RegisterRequest;
import SmartPantry.demo.dtos.responses.AuthResponse;
import SmartPantry.demo.entities.User;
import SmartPantry.demo.repositories.UserRepository;
import SmartPantry.demo.services.interfaces.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Service implementation for handling user authentication and registration.
 */
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final SmartPantry.demo.configs.JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user in system with password hashing.
     *
     * @param request registration details including username, email, and password
     * @throws IllegalArgumentException if username or email is already taken
     */
    @Override
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
    }

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param request login credentials (username and password)
     * @return an {@link AuthResponse} containing JWT token, username, and roles
     * @throws IllegalArgumentException if username or password is invalid
     */
    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .roles(Set.copyOf(user.getRoles().stream().map(Enum::name).toList()))
                .build();
    }

    /**
     * Generates a new JWT token for an authenticated user.
     *
     * @param userId the ID of the authenticated user
     * @param username the username of the authenticated user
     * @return an {@link AuthResponse} containing the new JWT token
     */
    @Override
    public AuthResponse refreshToken(Long userId, String username) {
        String token = jwtUtil.generateToken(userId, username);
        return AuthResponse.builder()
                .token(token)
                .username(username)
                .roles(Set.copyOf(userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("User not found"))
                        .getRoles()
                        .stream()
                        .map(Enum::name)
                        .toList()))
                .build();
    }
}
