package com.fsd08.MediLink.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fsd08.MediLink.service.AppointmentsService;
import com.fsd08.MediLink.service.JwtService;
import com.fsd08.MediLink.service.UserService;
import com.fsd08.MediLink.service.VideoChatService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("videochat")
public class VideoChatController {
    // private JwtService jwtService;
    private VideoChatService videoChatService;

    public VideoChatController(JwtService jwtService, VideoChatService videoChatService) {
        // this.jwtService = jwtService;
        this.videoChatService = videoChatService;
    }

    @CrossOrigin
    @GetMapping("/auth")
    public ResponseEntity<Map<String, String>> getVideoAuth(@RequestParam("appointmentId") int appointmentId,
            @RequestHeader("Authorization") String accessToken) {

        Map<String, String> returnData = new HashMap<>();
        // todo: validate user from token and appointment record
        if (!JwtService.validateToken(accessToken)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String userName = JwtService.extractUsernameFromToken(accessToken);
        if (videoChatService.getPatientName(appointmentId).equals(userName)) {
            returnData.put("Role", "PATIENT");
            return ResponseEntity.ok(returnData);
        }
        if (videoChatService.getDoctorName(appointmentId).equals(userName)) {
            returnData.put("Role", "DOCTOR");
            return ResponseEntity.ok(returnData);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        // returnData.put("Role", "DOCTOR");
        // return ResponseEntity.ok(returnData);

    }
}
