package SmartPantry.demo.services.interfaces;

import SmartPantry.demo.dtos.responses.UserResponse;
import SmartPantry.demo.entities.User;

public interface IUserService {
    UserResponse getCurrentUserResponse();
    User getCurrentUserEntity();
}
