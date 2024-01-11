package com.fsd08.MediLink.controller;

import com.fsd08.MediLink.MediLinkApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class Oauth2Controller {
    private static final Logger logger = LoggerFactory.getLogger(MediLinkApplication.class);


    @GetMapping("/oauth")
    public ResponseEntity<String > hello(){
        logger.info("inside oauth");

        return ResponseEntity.ok("hello from security end point");
    }
}
