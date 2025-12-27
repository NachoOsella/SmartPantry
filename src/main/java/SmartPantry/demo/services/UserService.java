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

    /**
     * TO DO: 1. Obtain current security context (when Security is added). 2. Find
     * user in DB. 3. Map to UserResponse.
     */
    @Override
    public UserResponse getCurrentUserResponse() {
        return null;
    }

    /**
     * TO DO: This is a helper for other services. 1. Return the User entity of the
     * logged-in user.
     */
    @Override
    public User getCurrentUserEntity() {
        return null;
    }
}
