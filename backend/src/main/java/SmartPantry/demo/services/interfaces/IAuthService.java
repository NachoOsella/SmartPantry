package SmartPantry.demo.services.interfaces;

import SmartPantry.demo.dtos.requests.LoginRequest;
import SmartPantry.demo.dtos.requests.RegisterRequest;
import SmartPantry.demo.dtos.responses.AuthResponse;

public interface IAuthService {
    void register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(Long userId, String username);
}
