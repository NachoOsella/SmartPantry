package SmartPantry.demo.services;

import SmartPantry.demo.dtos.requests.LoginRequest;
import SmartPantry.demo.dtos.requests.RegisterRequest;
import SmartPantry.demo.dtos.responses.AuthResponse;
import SmartPantry.demo.entities.User;
import SmartPantry.demo.repositories.UserRepository;
import SmartPantry.demo.services.interfaces.IAuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Service implementation for handling user authentication and registration.
 */
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final SmartPantry.demo.configs.JwtUtil jwtUtil;

    /**
     * Registers a new user in the system.
     *
     * @param request the registration details including username, email, and password
     * @throws IllegalArgumentException if the username or email is already taken
     */
    @Override
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = modelMapper.map(request, User.class);
        userRepository.save(user);
    }

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param request the login credentials (username and password)
     * @return an {@link AuthResponse} containing the JWT token, username, and roles
     * @throws IllegalArgumentException if the username or password is invalid
     */
    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .roles(Set.copyOf(user.getRoles().stream().map(Enum::name).toList()))
                .build();
    }
}
