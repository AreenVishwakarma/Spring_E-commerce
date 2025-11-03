package com.example.Spring_E_Commerce.controller;

import com.example.Spring_E_Commerce.model.Details_Model;
import com.example.Spring_E_Commerce.repository.Detailrepo;
import com.example.Spring_E_Commerce.service.Payment_Service;
import com.example.Spring_E_Commerce.config.Razorpay_Config;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller endpoints:
 * - POST /payment/create/{orderId}  -> returns Razorpay order info for client checkout
 * - POST /payment/verify            -> verify payment signature from client and update your order
 */
@RestController
@RequestMapping("/payment")
public class Payment_Controller {

    private final Payment_Service paymentService;
    private final Razorpay_Config razorpayConfig;
    private final Detailrepo detailrepo;

    public Payment_Controller(Payment_Service paymentService, Razorpay_Config razorpayConfig, Detailrepo detailrepo) {
        this.paymentService = paymentService;
        this.razorpayConfig = razorpayConfig;
        this.detailrepo = detailrepo;
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "Payment Controller is working!");
        response.put("razorpayKeyConfigured", razorpayConfig.getKey() != null && !razorpayConfig.getKey().trim().isEmpty() ? "YES" : "NO");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create/{orderId}")
    public ResponseEntity<?> createOrder(@PathVariable String orderId) {
        try {
            // Lookup order header to get the total
            Details_Model order = detailrepo.findByOrderID(orderId);
            if (order == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Order not found");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            double total = order.getTotalPrice() == null ? 0.0 : order.getTotalPrice();
            int amountPaise = (int) Math.round(total * 100); // convert INR to paise

            var razorpayOrder = paymentService.createRazorpayOrder(amountPaise, "INR", orderId);

            Map<String, Object> resp = new HashMap<>();
            resp.put("orderId", razorpayOrder.get("id"));
            resp.put("amount", razorpayOrder.get("amount"));
            resp.put("currency", razorpayOrder.get("currency"));
            resp.put("key", razorpayConfig.getKey()); // send key_id to client
            resp.put("receipt", razorpayOrder.get("receipt"));

            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * Verify payment signature sent by client after a successful checkout.
     * Client should POST:
     * { razorpay_payment_id: "...", razorpay_order_id: "...", razorpay_signature: "...", orderId: "..." }
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> body) {
        String paymentId = body.get("razorpay_payment_id");
        String razorpayOrderId = body.get("razorpay_order_id");
        String signature = body.get("razorpay_signature");
        String orderId = body.get("orderId"); // your internal order id/receipt

        if (paymentId == null || razorpayOrderId == null || signature == null || orderId == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Missing parameters");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // Compute expected signature: HMAC_SHA256(order_id + "|" + payment_id, secret)
        String payload = razorpayOrderId + "|" + paymentId;
        String expectedSignature = HmacUtils.hmacSha256Hex(razorpayConfig.getSecret(), payload);

        if (!expectedSignature.equals(signature)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("verified", false);
            errorResponse.put("error", "Invalid signature");
            return ResponseEntity.status(400).body(errorResponse);
        }

        // Signature valid — update order status (do NOT store card data)
        Details_Model order = detailrepo.findByOrderID(orderId);
        if (order == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Order not found");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        order.setPaymentStatus("PAID");
        order.setTransactionId(paymentId);
        order.setPaymentMethod("razorpay");
        detailrepo.save(order);

        Map<String, Boolean> successResponse = new HashMap<>();
        successResponse.put("verified", true);
        return ResponseEntity.ok(successResponse);
    }
}