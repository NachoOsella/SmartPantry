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

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final SmartPantry.demo.configs.JwtUtil jwtUtil;

    /**
     * TO DO: Implement registration logic.
     * 1. Validate if username/email already exists using userRepository.
     * 2. Encrypt the password (when Security is added).
     * 3. Map RegisterRequest to User entity.
     * 4. Save the user.
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
     * TO DO: Implement login logic.
     * 1. Find user by username.
     * 2. Verify password.
     * 3. Generate JWT token (future).
     * 4. Map to AuthResponse.
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
