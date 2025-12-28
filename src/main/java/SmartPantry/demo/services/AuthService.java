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