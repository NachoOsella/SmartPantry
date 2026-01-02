package SmartPantry.demo.services;

import SmartPantry.demo.configs.UserContext;
import SmartPantry.demo.dtos.responses.UserResponse;
import SmartPantry.demo.entities.User;
import SmartPantry.demo.repositories.UserRepository;
import SmartPantry.demo.services.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * Service implementation for managing user-related operations and retrieving current user context.
 */
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    /**
     * Retrieves the profile information of the currently authenticated user.
     *
     * @return a {@link UserResponse} containing user profile details
     * @throws IllegalArgumentException if the user is not found in the database
     */
    @Override
    public UserResponse getCurrentUserResponse() {
        Long userId = UserContext.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet()))
                .build();
    }

    /**
     * Retrieves the {@link User} entity of the currently authenticated user.
     * Useful for internal service operations that require the user entity.
     *
     * @return the {@link User} entity of the authenticated user
     * @throws IllegalArgumentException if the user is not found in the database
     */
    @Override
    public User getCurrentUserEntity() {
        Long userId = UserContext.getCurrentUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
