package com.tc.booking.api.service;

import com.tc.booking.model.entity.Booking;

public interface BookingService {

  public Booking findBookingById(int bookingId);
    Booking addBooking(Booking booking);
    // Define other methods as needed
}
