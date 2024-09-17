package com.tc.booking.api.service;

import com.tc.booking.model.entity.Payment;
import com.tc.booking.api.exception.BookingException;

public interface PaymentService {
    Payment makePayment(int bookingId, double amount) throws BookingException;
}
