package SmartPantry.demo.services;

import SmartPantry.demo.dtos.responses.UserResponse;
import SmartPantry.demo.entities.User;
import SmartPantry.demo.repositories.UserRepository;
import SmartPantry.demo.services.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserResponse getCurrentUserResponse() {
        Long userId = SmartPantry.demo.configs.UserContext.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public User getCurrentUserEntity() {
        Long userId = SmartPantry.demo.configs.UserContext.getCurrentUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}