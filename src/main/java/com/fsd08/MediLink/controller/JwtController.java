package com.fsd08.MediLink.controller;

import com.fsd08.MediLink.MediLinkApplication;
import com.fsd08.MediLink.entity.User;
import com.fsd08.MediLink.security.JwtAuthenticationRequest;
import com.fsd08.MediLink.service.JwtService;
import com.fsd08.MediLink.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class JwtController {
    private static final Logger logger = LoggerFactory.getLogger(MediLinkApplication.class);

    private  final JwtService jwtService;
    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<Object> getTokenForAuthenticatedUser(@RequestBody JwtAuthenticationRequest authRequest) {
        Map<String, Object> result = new HashMap<>();
        logger.info("jwtcontroller");
        //if user exist but is not enabled
        Optional<User> user = userService.findUserByUsername(authRequest.getUsername());
        if (user.isPresent()) {
            if (!user.get().isEnabled()) {
                result.put("msg", "Please verify your account first.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
        }
        //user exist and the password is correct
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            logger.info("is authenticate");
            result.put("msg", "Login successfully");
            result.put("token", jwtService.getGeneratedToken(authRequest.getUsername()));
            result.put("user", user);
//            result.put("user", (User) authentication.getPrincipal());
            return ResponseEntity.ok(result);

        }
        //user exist but the password is wrong
        result.put("msg", "User name or password is wrong.");
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
       //throw new UsernameNotFoundException("Invalid user credentials");
    }

    // for test
    @PostMapping("/testValidateToken")
    public boolean validateTokenInHeader(@RequestHeader("Authorization") String accessToken) {
        return jwtService.validateToken(accessToken);
    }
}

