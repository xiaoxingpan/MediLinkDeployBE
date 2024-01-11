package com.fsd08.MediLink.registration;

public record RegistrationRequest(
        String username,
        String password,
        String email,
        String name,
        String authority,
        String confirmPassword

) {
}
