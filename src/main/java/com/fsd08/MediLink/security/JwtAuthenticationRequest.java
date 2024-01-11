package com.fsd08.MediLink.security;

import com.fsd08.MediLink.MediLinkApplication;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Setter
@Getter
@RequiredArgsConstructor
public class JwtAuthenticationRequest {
    private static final Logger logger = LoggerFactory.getLogger(MediLinkApplication.class);

    private String username;

    public JwtAuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    private String password;


}
