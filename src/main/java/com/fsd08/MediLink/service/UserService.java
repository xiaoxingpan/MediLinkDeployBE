package com.fsd08.MediLink.service;


import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;

import com.fsd08.MediLink.dto.UserDto;
import com.fsd08.MediLink.entity.Department;
import com.fsd08.MediLink.entity.User;
import com.fsd08.MediLink.entity.VerificationToken;
import com.fsd08.MediLink.exception.UserAlreadyExistsException;
import com.fsd08.MediLink.registration.RegistrationRequest;
import com.fsd08.MediLink.registration.password.PasswordResetTokenService;
import com.fsd08.MediLink.repository.AuthorityRepository;
import com.fsd08.MediLink.repository.DepartmentRepository;
import com.fsd08.MediLink.repository.UserRepository;
import com.fsd08.MediLink.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.ion.Decimal;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository ;
    private final AuthorityRepository authorityRepository;

    private final PasswordEncoder passwordEncoder;

    private final VerificationTokenRepository tokenRepository;
    private final PasswordResetTokenService passwordResetTokenService;
    private final DepartmentRepository departmentRepository;


    public User getUser(String username) {
        return userRepository.findById(username).orElse(null);
    }

    public  Optional<User> findUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public User updateUserPatch(int id, Map<String, Object> updates) {
        System.out.println("updateUserPatch updates = " + updates);

        User user = userRepository.findById(id);

        try {
            updates.forEach((key, value) -> {
                switch (key) {
                    case "email":
                        user.setEmail((String) value);
                        break;
                    case "name":
                        user.setName((String) value);
                        break;
                    case "telephone":
                        user.setTelephone((String) value);
                        break;
                    case "address":
                        if (value != null) user.setAddress((String) value);
                        break;
                    case "imageUrl":
                        user.setAvatar((String) value);
                        break;
                    case "description":
                        user.setDescription((String) value);
                        break;
                    case "postal":
                        user.setPostal((String) value);
                        break;
                    case "rate":
                        System.out.println("1rate = " + value);
                        BigDecimal rateValue = null;
                        if (value != null) {
                            if (value instanceof BigDecimal) {
                                rateValue = (BigDecimal) value;
                            } else if (value instanceof String) {
                                rateValue = new BigDecimal((String) value);
                            } else if (value instanceof Number) {
                                rateValue = BigDecimal.valueOf(((Number) value).doubleValue());
                            } else {
                                throw new IllegalArgumentException("Invalid rate value type");
                            }
                        }
                        user.setDefault_price(rateValue);
                        System.out.println("2rate = " + rateValue);
                        break;
                }
            });
        } catch (Exception e) {
            System.err.println("Error during update: " + e.getMessage());
            throw e; // Or handle it based on your application's needs
        }

        return userRepository.save(user); // Saving the user entity, not the Optional
    }
    public List<User> getUsers(){
        return userRepository.findAll();

    }
    public void saveUserVerificationToken(User theUser, String token) {
        var verificationToken = new VerificationToken(token, theUser);
        tokenRepository.save(verificationToken);
    }

    public String validateToken(String theToken) {
        VerificationToken token = tokenRepository.findByToken(theToken);
        if(token == null){
            return "Invalid verification token";
        }
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime()-calendar.getTime().getTime())<= 0){
            //tokenRepository.delete(token);
            return "Verification link already expired," +
                    " Please, click the link below to receive a new verification link";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    public User registerUser(RegistrationRequest request) {
        Optional<User> user = userRepository.findByUsername(request.username());
        if (user.isPresent()){
            throw new UserAlreadyExistsException(
                    "User with email "+request.username() + " already exists");
        }
        var newUser = new User();
        newUser.setName(request.name());
        newUser.setUsername(request.username());
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setAuthority(request.authority());
        return userRepository.save(newUser);

    }
   public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void createPasswordResetTokenForUser(User user, String passwordResetToken) {
        passwordResetTokenService.createPasswordResetTokenForUser(user, passwordResetToken);
    }

    public String validatePasswordResetToken(String token) {
        return passwordResetTokenService.validatePasswordResetToken(token);
    }

    public User findUserByPasswordToken(String token) {
        return passwordResetTokenService.findUserByPasswordToken(token).get();
    }

    public void resetPassword(User theUser, String newPassword) {
        theUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(theUser);
    }

    public void deleteByPasswordToken(String token){
        passwordResetTokenService.DeletePasswordToken(token);

    }

    public List<UserDto> findAllDoctors(){
        List <User> users = userRepository.findAll();
        List<User> doctors = users.stream()
                .filter(user -> user.getDepartment_id() != null && user.getDepartment_id() != 0)
                .collect(Collectors.toList());


        return doctors.stream()
                .map(this::fromUserEntity)
                .collect(Collectors.toList());
    }

    private UserDto fromUserEntity(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setAuthority(user.getAuthority());
        dto.setAvatar(user.getAvatar());
        dto.setDescription(user.getDescription());
        dto.setPrice(user.getDefault_price());
        dto.setApproved(user.isApproved());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        if(user.isSuspended() == null || !user.isSuspended() ) {
            dto.setSuspended(false);
        } else {
            dto.setSuspended(true);
        }
       int departmentId = user.getDepartment_id();
        List<Department> departments = departmentRepository.getDepartmentById(departmentId);
        if (departments != null) {
            Department department = departments.get(0);
            dto.setDepartmentName(department.getDepartment_name());
        }
        return dto;
    }

}
