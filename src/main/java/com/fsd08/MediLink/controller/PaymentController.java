package com.fsd08.MediLink.controller;

import com.fsd08.MediLink.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("payment")
public class PaymentController {
    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @CrossOrigin
    @GetMapping("/payment-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestParam("appointmentId") int appointmentId) {

        Map<String, String> returnData = new HashMap<>();

        int price = paymentService.getPrice(appointmentId);
        if (price <= 0) {
            returnData.put("message", "price not found");
            return ResponseEntity.status(500).body(returnData);
        }
        int count = paymentService.getPaymentIntentsSucceededCount(appointmentId);
        if (count != 0) {
            returnData.put("message", "already paid");
            return ResponseEntity.status(400).body(returnData);
        }
        PaymentIntent paymentIntent = null;
        try {
            paymentIntent = paymentService.createPaymentIntent(price, "cad", "description", appointmentId);
        } catch (StripeException e) {
            e.printStackTrace();
        }
        if (paymentIntent != null) {
            returnData.put("clientSecret", paymentIntent.getClientSecret());
            String priceStr = String.format("%.2f", (double) price / 100);
            returnData.put("price", priceStr);
            return ResponseEntity.ok(returnData);
        } else {
            returnData.put("message", "create payment failed");
            return ResponseEntity.status(500).body(returnData);
        }

    }

    @CrossOrigin
    @GetMapping("/payment-status")
    public ResponseEntity<Map<String, String>> getPaymentStatus(@RequestParam("appointmentId") int appointmentId) {
        Map<String, String> returnData = new HashMap<>();
        int count = paymentService.getPaymentIntentsSucceededCount(appointmentId);
        if (count == 0) {
            returnData.put("message", "No payments have succeeded.");
            return ResponseEntity.status(200).body(returnData);
        } else if (count == 1) {
            returnData.put("message", "Payment succeeded.");
            return ResponseEntity.status(201).body(returnData);
        } else {
            returnData.put("message", "Multiple payments detected. Please contact an administrator for assistance.");
            return ResponseEntity.status(202).body(returnData);
        }
    }

}
