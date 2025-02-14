package com.example.Echo.auth.controller;

import com.example.Echo.auth.models.User;
import com.example.Echo.auth.models.dto.AuthDto;
import com.example.Echo.auth.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User req) {
        String response = authenticationService.register(req);
        if(Objects.equals(response, "exist")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with this username already exists");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthDto req) {

        String response = authenticationService.login(req);

        if(response.equals("NotFound")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No User with this username exists");
        }else if(response.equals("InvalidCredentials")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Username or Password");
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            return authenticationService.getUser(token);
        }else {
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }
    }

    @PutMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestHeader("Authorization") String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            String response = authenticationService.verifyUser(token);

            if(response.equals("TokenExpired")) {
                return ResponseEntity.status(400).body("Token Expired");
            }

            return ResponseEntity.status(200).body("Account verified successfully !");

        }else {
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }
    }
}
