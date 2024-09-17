package com.tc.booking.api.service.impl;

import com.tc.booking.api.service.PaymentService;
import com.tc.booking.api.service.BookingService;
import com.tc.booking.model.entity.Payment;
import com.tc.booking.model.entity.Booking;
import com.tc.booking.repo.PaymentRepository;
import com.tc.booking.api.exception.BookingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingService bookingService;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, BookingService bookingService) {
        this.paymentRepository = paymentRepository;
        this.bookingService = bookingService;
    }

    @Override
    @Transactional
    public Payment makePayment(int bookingId, double amount) throws BookingException {
        // Find the booking using the booking ID
        Booking booking = bookingService.findBookingById(bookingId);

        // If booking is not found, throw an exception
        if (booking == null) {
            throw new BookingException("Booking not found");
        }

        // Retrieve the room price from the booking
        double roomPrice = booking.getRoom().getPrice();

        // Validate if the payment amount is less than the room price
        if (amount < roomPrice) {
            throw new BookingException("Payment amount is less than the room price");
        }

        // Create a new payment record
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setPaymentDate(new Date());  // Set current date for payment
        payment.setBooking(booking);         // Link the payment to the booking
        payment.setStatus("Completed");      // Set status to completed or whatever status is required

        // Save the payment to the repository
        return paymentRepository.save(payment);
    }
}
