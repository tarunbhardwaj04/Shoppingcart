package com.example.shop.controller.api;

import com.example.shop.dto.PaymentRequest;
import com.example.shop.dto.PaymentResponse;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PaymentApiController {

    @PostMapping("/pay")
    public PaymentResponse processPayment(@RequestBody PaymentRequest request) {

        // Simulate network latency (1.5 seconds)
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String method = request.getPaymentMethod();

        // 1. COD is always successful
        if ("cod".equalsIgnoreCase(method)) {
            return new PaymentResponse(true, "Order placed with Cash on Delivery", generateTransactionId());
        }

        // 2. Validate Card
        if ("card".equalsIgnoreCase(method)) {
            if (request.getCardNumber() == null || request.getCardNumber().length() < 13) {
                return new PaymentResponse(false, "Invalid Card Number", null);
            }
            // Mock Failure: excessive amount or specific card number "0000..."
            if (request.getCardNumber().startsWith("0000")) {
                return new PaymentResponse(false, "Card Declined by Bank", null);
            }
            return new PaymentResponse(true, "Payment Successful via Card", generateTransactionId());
        }

        // 3. Validate UPI
        if ("upi".equalsIgnoreCase(method)) {
            if (request.getUpiId() == null || !request.getUpiId().contains("@")) {
                return new PaymentResponse(false, "Invalid UPI ID", null);
            }
            return new PaymentResponse(true, "Payment Successful via UPI", generateTransactionId());
        }

        // 4. NetBanking
        if ("netbanking".equalsIgnoreCase(method)) {
            return new PaymentResponse(true, "Payment Successful via Net Banking", generateTransactionId());
        }

        return new PaymentResponse(false, "Unknown Payment Method", null);
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
