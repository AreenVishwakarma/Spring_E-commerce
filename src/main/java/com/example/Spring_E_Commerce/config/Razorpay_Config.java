package com.example.Spring_E_Commerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import jakarta.annotation.PostConstruct;

/**
 * Reads Razorpay credentials from application properties or environment variables.
 * Use property keys "razorpay.key" and "razorpay.secret" (these are already referenced
 * in application.properties as environment-backed values).
 *
 * Important: do NOT hardcode actual API keys into @Value(...) — use property keys.
 */
@Configuration
public class Razorpay_Config {

    // Use property placeholders (with empty default) so missing env vars won't cause a placeholder resolution error.
    @Value("${razorpay.key:}")
    private String razorpayKey;

    @Value("${razorpay.secret:}")
    private String razorpaySecret;

    @Value("${razorpay.webhook.secret:}")
    private String razorpayWebhookSecret;

    @PostConstruct
    public void validateConfiguration() {
        if (razorpayKey == null || razorpayKey.trim().isEmpty()) {
            System.err.println("WARNING: Razorpay Key is not configured!");
        } else {
            System.out.println("Razorpay Key configured: " + razorpayKey.substring(0, Math.min(10, razorpayKey.length())) + "...");
        }
        
        if (razorpaySecret == null || razorpaySecret.trim().isEmpty()) {
            System.err.println("WARNING: Razorpay Secret is not configured!");
        } else {
            System.out.println("Razorpay Secret configured: " + razorpaySecret.substring(0, Math.min(10, razorpaySecret.length())) + "...");
        }
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String getKey() {
        return razorpayKey;
    }

    public String getSecret() {
        return razorpaySecret;
    }

    public String getWebhookSecret() {
        return razorpayWebhookSecret;
    }
}