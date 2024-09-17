package com.tc.booking.api.controller;

import com.tc.booking.api.service.PaymentService;
import com.tc.booking.api.service.BookingService;
import com.tc.booking.model.entity.Payment;
import com.tc.booking.api.exception.BookingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final BookingService bookingService;

    @Autowired
    public PaymentController(PaymentService paymentService, BookingService bookingService) {
        this.paymentService = paymentService;
        this.bookingService = bookingService;
    }

    @PostMapping("/make-payment")
    public ResponseEntity<?> makePayment(@RequestParam int bookingId, @RequestParam double amount) {
        try {
            Payment payment = paymentService.makePayment(bookingId, amount);
            return ResponseEntity.ok(payment);
        } catch (BookingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
