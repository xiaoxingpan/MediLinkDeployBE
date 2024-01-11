package com.fsd08.MediLink.registration.password;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String email;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
