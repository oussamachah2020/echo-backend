package com.example.Echo.auth.services;

import com.example.Echo.auth.models.User;
import com.example.Echo.auth.models.dto.AuthDto;
import com.example.Echo.auth.models.dto.MailDto;
import com.example.Echo.auth.models.dto.UserInfoDto;
import com.example.Echo.auth.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private MailServiceImp emailService;

    public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public String register(User req) {
        Optional<User> existingUser = repository.findByUsername(req.getUsername());

        if (existingUser.isPresent()) {
            return "exist";
        }

        User user = new User();
        user.setEmail(req.getEmail());
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        user = repository.save(user);

        // Prepare email details
        MailDto mailDetails = new MailDto();
        mailDetails.setRecipient(user.getEmail());
        mailDetails.setSubject("Welcome to Our Platform");
        mailDetails.setMsgBody("Hello " + user.getUsername() + ",\n\nThank you for registering!");

        // Send email
        emailService.sendHtmlMail(mailDetails);

        return jwtService.generateAccessToken(user);
    }

    public String login(AuthDto req) {
        User user = repository.findByUsername(req.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No User with this username exists"));
        System.out.print(req.getPassword() + user.getPassword());

        if(!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            return "InvalidCredentials";
        }

        return jwtService.generateAccessToken(user);
    }

    public ResponseEntity<?> getUser(String token) {
        if (token.isEmpty()) {
            return ResponseEntity.status(400).body("Token required");
        }

        if (jwtService.isTokenExpired(token)) {
            return ResponseEntity.status(400).body("Expired Token");
        }

        String username = jwtService.extractUsername(token);

        User existingUser = repository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No User with this username exists"));

        UserInfoDto userInfoDto = new UserInfoDto(
                existingUser.getId(),
                existingUser.getUsername(),
                existingUser.getEmail(),
                existingUser.getVerified(),
                existingUser.getPhotoUrl()
        );

        return ResponseEntity.ok(userInfoDto);
    }

    @Transactional
    public String verifyUser(String token) {

        if (jwtService.isTokenExpired(token)) {
            return "ExpiredToken";
        }

        String username = jwtService.extractUsername(token);
        User existingUser = repository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No User with this username exists"));

        existingUser.setVerified(true);

        repository.save(existingUser);

        return "AccountVerified";
    }
}
