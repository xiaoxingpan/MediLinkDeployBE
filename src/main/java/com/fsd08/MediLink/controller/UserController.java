package com.fsd08.MediLink.controller;

import com.fsd08.MediLink.MediLinkApplication;
import com.fsd08.MediLink.dto.UserDto;
import com.fsd08.MediLink.entity.Authority;
import com.fsd08.MediLink.entity.User;
import com.fsd08.MediLink.entity.VerificationToken;
import com.fsd08.MediLink.event.RegistrationCompleteEvent;
import com.fsd08.MediLink.event.listener.RegistrationCompleteEventListener;
import com.fsd08.MediLink.registration.RegistrationRequest;
import com.fsd08.MediLink.registration.password.PasswordResetRequest;
import com.fsd08.MediLink.repository.AuthorityRepository;
import com.fsd08.MediLink.repository.UserRepository;
import com.fsd08.MediLink.repository.VerificationTokenRepository;
import com.fsd08.MediLink.service.JwtService;
import com.fsd08.MediLink.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.*;

@RestController
//@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.DELETE,RequestMethod.PATCH, RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST})
@RequiredArgsConstructor
public class UserController {
    // private static final Gson gson = new Gson();

    private static final Logger logger = LoggerFactory.getLogger(MediLinkApplication.class);

    private final UserService userService;

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository tokenRepository;
    private final RegistrationCompleteEventListener eventListener;


    @PutMapping("/getUserById/updatePut/{id}")
    public ResponseEntity<Object> updateUserPut(@RequestBody User updatedItem) {
        try{
            User result = userRepository.save(updatedItem);
            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PatchMapping("/getUserById/updatePatch/{id}")
    public ResponseEntity<Object> updateUserPatch(@PathVariable int id, @RequestBody Map<String, Object> updates) {
           System.out.println("updatePatch() image =" );
        try{
            User result = userService.updateUserPatch(id, updates);
            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {

        try {

            User result = userRepository.findById(id);

            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {

        List<User> results = userRepository.findAll();
        return results;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request){
        System.out.println("registerUser");
        Map<String, Object> result = new HashMap<>();
        // Check if username exists in the DB
        if (userRepository.existsByUsername(registrationRequest.username())) {
            result.put("msg", "Username is already taken!");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        // Check if email exists in the DB
        if (userRepository.existsByEmail(registrationRequest.email())) {
            result.put("msg", "Email is already taken!");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        // check if the password and empty
        if (registrationRequest.password() == null || registrationRequest.password().isEmpty()) {
            result.put("msg", "Failed to register user: password empty.");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        // check if the password and confirm password is the same
        else if (!registrationRequest.password().equals(registrationRequest.confirmPassword())) {
            result.put("msg", "Failed to register user: Passwords do not match.");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }else {
            User user = userService.registerUser(registrationRequest);
            // create user authority
            Authority authority = new Authority(user.getUsername(), user.getAuthority());
            authorityRepository.save(authority);
            publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
            result.put("msg", "Registration Success, please check your email for registration confirmation");
            result.put("user", user);
            return ResponseEntity.ok(result);
        }
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"
                +request.getServerPort()+request.getContextPath();
    }

    @PostMapping("/password-reset-request")
    public String resetPasswordRequest(@RequestBody PasswordResetRequest passwordResetRequest,
                                       final HttpServletRequest servletRequest)
            throws MessagingException, UnsupportedEncodingException {

        Optional<User> user = userService.findByEmail(passwordResetRequest.getEmail());
        String passwordResetUrl = "";
        String passwordResetToken="";
        if (user.isPresent()) {
            passwordResetToken = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user.get(), passwordResetToken);
            passwordResetUrl = passwordResetEmailLink(user.get(), passwordResetToken);
            return passwordResetToken;

        }else{
            throw new UsernameNotFoundException("User does not exist.");
        }
//        return passwordResetUrl;
    }


    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody PasswordResetRequest passwordResetRequest,
                                @RequestParam("token") String token){
        String tokenVerificationResult = userService.validatePasswordResetToken(token);
        if (!tokenVerificationResult.equalsIgnoreCase("valid")) {
            return "Invalid token password reset token";
        }
        Optional<User> theUser = Optional.ofNullable(userService.findUserByPasswordToken(token));
        if (theUser.isPresent()) {
            userService.resetPassword(theUser.get(), passwordResetRequest.getNewPassword());
            userService.deleteByPasswordToken(token);
            return "Password has been reset successfully";
        }
        return "Invalid password reset token";
    }

    private String passwordResetEmailLink(User user, String passwordToken) throws MessagingException, UnsupportedEncodingException {
        String url = "http://localhost:3000/ResetPassword/"+passwordToken;
        eventListener.sendPasswordResetVerificationEmail(url,user);
        logger.info("Click the link to reset your password :  {}", url);
        return url;
    }
    @GetMapping("/verifyEmail")
    public String sendVerificationToken(@RequestParam("token") String token){
        VerificationToken theToken = tokenRepository.findByToken(token);
        if (theToken.getUser().isEnabled()){
            return "This account has already been verified, please login.";
        }
        String verificationResult = userService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")){
            return "<h1>Email verified successfully. Now you can <a href='http://localhost:3000/login'>login</a> to your account<h1>";
        }
        return "Invalid verification link";
    }

    @GetMapping("/getAllDoctors")
    public List<UserDto> getAllDoctorsWithDepart() {
        List<UserDto> users = userService.findAllDoctors();

        return users;
    }
}
