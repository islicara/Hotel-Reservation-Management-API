package de.lhind.internship.mini.project.controller;

import de.lhind.internship.mini.project.dto.auth.AuthResponse;
import de.lhind.internship.mini.project.dto.auth.LoginRequest;
import de.lhind.internship.mini.project.dto.auth.RegisterRequest;
import de.lhind.internship.mini.project.entity.User;
import de.lhind.internship.mini.project.entity.Role;
import de.lhind.internship.mini.project.repository.UserRepository;
import de.lhind.internship.mini.project.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger log = LogManager.getLogger(AuthController.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.trace("POST /api/auth/register -> username={}", request.username());

        if (userRepository.existsByUsername(request.username())) {
            log.warn("Registration failed, username already exists — username={}", request.username());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role() != null ? request.role() : Role.USER)
                .build();

        userRepository.save(user);
        log.debug("User persisted — username={}, role={}", user.getUsername(), user.getRole());

        String token = jwtService.generateToken(user);
        log.info("User registered successfully — username={}, role={}", user.getUsername(), user.getRole());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(token, user.getUsername(), user.getRole().name()));

        /* --Before Log4J
        if (userRepository.existsByUsername(request.username())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role() != null ? request.role() : Role.USER)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(token, user.getUsername(), user.getRole().name()));
        */
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.trace("POST /api/auth/login -> username={}", request.username());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> {
                    log.error("Login failed, user not found after authentication — username={}", request.username());
                    return new RuntimeException("User not found");
                });

        String token = jwtService.generateToken(user);
        log.info("User logged in successfully — username={}", user.getUsername());
        return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getRole().name()));

        /*
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = userRepository.findByUsername(request.username())
                .orElseThrow();

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getRole().name()));

         */
    }
}