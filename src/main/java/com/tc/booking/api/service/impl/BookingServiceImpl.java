package com.tc.booking.api.service.impl;

import com.tc.booking.api.exception.BookingException;
import com.tc.booking.api.service.BookingService;
import com.tc.booking.model.entity.Booking;
import com.tc.booking.repo.BookingRepository;
import com.tc.booking.repo.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, HotelRepository hotelRepository) {
        this.bookingRepository = bookingRepository;
        this.hotelRepository = hotelRepository;
    }

    @Override
    public Booking addBooking(Booking booking) {
        // Validate if the booking contains a valid room and hotel
        if (booking.getRoom() == null || booking.getRoom().getHotel() == null) {
          try {
            throw new BookingException("Invalid room or hotel details provided for the booking.");
          } catch (BookingException ex) {
            Logger.getLogger(BookingServiceImpl.class.getName())
                .log(Level.SEVERE, null, ex);
          }
        }

      try {
        // Check if the associated hotel exists
        hotelRepository.findById(booking.getRoom().getHotel().getId())
            .orElseThrow(() -> new BookingException("Hotel not found for the booking."));
      } catch (BookingException ex) {
        Logger.getLogger(BookingServiceImpl.class.getName())
            .log(Level.SEVERE, null, ex);
      }

        // Save and return the booking
        return bookingRepository.save(booking);
    }

    @Override
    public Booking findBookingById(int bookingId){
      try {
        // Fetch the booking by ID
        return bookingRepository.findById(bookingId)
            .orElseThrow(() -> new BookingException("Booking with ID " + bookingId + " not found."));
      } catch (BookingException ex) {
        Logger.getLogger(BookingServiceImpl.class.getName())
            .log(Level.SEVERE, null, ex);
      }
      return null;
    }}


