package com.example.Spring_E_Commerce.controller;

import com.example.Spring_E_Commerce.config.Razorpay_Config;
import com.example.Spring_E_Commerce.service.Payment_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private Razorpay_Config razorpayConfig;

    @Autowired
    private Payment_Service paymentService;

    @GetMapping("/config")
    public ResponseEntity<Map<String, Object>> testConfig() {
        Map<String, Object> result = new HashMap<>();
        
        // Test Razorpay configuration
        boolean keyConfigured = razorpayConfig.getKey() != null && !razorpayConfig.getKey().trim().isEmpty();
        boolean secretConfigured = razorpayConfig.getSecret() != null && !razorpayConfig.getSecret().trim().isEmpty();
        
        result.put("razorpayKeyConfigured", keyConfigured);
        result.put("razorpaySecretConfigured", secretConfigured);
        result.put("razorpayKeyPreview", keyConfigured ? 
            razorpayConfig.getKey().substring(0, Math.min(10, razorpayConfig.getKey().length())) + "..." : "NOT_SET");
        
        // Test payment service
        try {
            if (keyConfigured && secretConfigured) {
                // Test with minimal amount
                Map<String, Object> testOrder = paymentService.createRazorpayOrder(100, "INR", "TEST_ORDER");
                result.put("paymentServiceTest", "SUCCESS");
                result.put("testOrderId", testOrder.get("id"));
            } else {
                result.put("paymentServiceTest", "FAILED - Missing API keys");
            }
        } catch (Exception e) {
            result.put("paymentServiceTest", "FAILED - " + e.getMessage());
        }
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> result = new HashMap<>();
        result.put("status", "OK");
        result.put("message", "Spring E-commerce application is running");
        result.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(result);
    }
}
