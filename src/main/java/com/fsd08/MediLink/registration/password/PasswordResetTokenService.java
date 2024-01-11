package com.fsd08.MediLink.registration.password;
import com.fsd08.MediLink.MediLinkApplication;
import com.fsd08.MediLink.entity.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {
    private static final Logger logger = LoggerFactory.getLogger(MediLinkApplication.class);

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public void createPasswordResetTokenForUser(User user, String passwordToken) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(passwordToken, user);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    public String validatePasswordResetToken(String passwordResetToken) {
        PasswordResetToken passwordToken = passwordResetTokenRepository.findByToken(passwordResetToken);
        if(passwordToken == null){
            return "Invalid verification token";
        }
        User user = passwordToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((passwordToken.getExpirationTime().getTime()-calendar.getTime().getTime())<= 0){
            return "Link already expired, resend link";
        }
        return "valid";
    }
    public Optional<User> findUserByPasswordToken(String passwordResetToken) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(passwordResetToken).getUser());
    }

    public PasswordResetToken findPasswordResetToken(String token){
        return passwordResetTokenRepository.findByToken(token);
    }

    public void DeletePasswordToken(String passwordResetToken){
        passwordResetTokenRepository.deletePasswordResetTokenByToken(passwordResetToken);
    }
}