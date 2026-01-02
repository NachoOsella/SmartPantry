package SmartPantry.demo.controllers;

import SmartPantry.demo.dtos.requests.LoginRequest;
import SmartPantry.demo.dtos.requests.RegisterRequest;
import SmartPantry.demo.dtos.responses.AuthResponse;
import SmartPantry.demo.services.interfaces.IAuthService;
import SmartPantry.demo.configs.UserContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken() {
        Long currentUserId = UserContext.getCurrentUserId();
        String currentUsername = UserContext.getCurrentUsername();
        return ResponseEntity.ok(authService.refreshToken(currentUserId, currentUsername));
    }
}
