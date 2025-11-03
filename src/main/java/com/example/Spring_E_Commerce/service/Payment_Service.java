package com.example.Spring_E_Commerce.service;

import com.example.Spring_E_Commerce.config.Razorpay_Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class Payment_Service {

    private final Razorpay_Config razorpayConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Payment_Service(Razorpay_Config razorpayConfig, RestTemplate restTemplate) {
        this.razorpayConfig = razorpayConfig;
        this.restTemplate = restTemplate;
    }

    /**
     * Create a Razorpay order (server -> Razorpay).
     * amountInPaise: integer (example: ₹100.50 => 10050)
     * currency: "INR"
     * receipt: any unique id (e.g. your orderId)
     */
    public Map<String, Object> createRazorpayOrder(int amountInPaise, String currency, String receipt) throws Exception {
        String url = "https://api.razorpay.com/v1/orders";

        // Validate configuration
        if (razorpayConfig.getKey() == null || razorpayConfig.getKey().trim().isEmpty()) {
            throw new RuntimeException("Razorpay Key is not configured!");
        }
        if (razorpayConfig.getSecret() == null || razorpayConfig.getSecret().trim().isEmpty()) {
            throw new RuntimeException("Razorpay Secret is not configured!");
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("amount", amountInPaise);
        payload.put("currency", currency);
        payload.put("receipt", receipt);
        payload.put("payment_capture", 1); // 1 = auto-capture, 0 = manual capture

        String auth = razorpayConfig.getKey() + ":" + razorpayConfig.getSecret();
        String basicAuth = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", basicAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to create razorpay order: " + response.getBody());
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> data = objectMapper.readValue(response.getBody(), Map.class);
        return data;
    }
}