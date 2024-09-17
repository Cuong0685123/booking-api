package com.tc.booking.api.controller;

import com.tc.booking.api.service.BookingService;
import com.tc.booking.api.web.UserInfoDetails;
import com.tc.booking.model.entity.Booking;
import com.tc.booking.model.entity.Customer;
import com.tc.booking.model.entity.Room;
import com.tc.booking.model.entity.Hotel;
import com.tc.booking.repo.CustomerRepository;
import com.tc.booking.repo.RoomRepository;
import com.tc.booking.repo.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    @Autowired
    public BookingController(BookingService bookingService, CustomerRepository customerRepository, 
                             RoomRepository roomRepository, HotelRepository hotelRepository) {
        this.bookingService = bookingService;
        this.customerRepository = customerRepository;
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
    }

   @PostMapping
public ResponseEntity<?> createBooking(
        @RequestParam String checkInDate,
        @RequestParam String checkOutDate,
        @RequestParam Integer roomId,
        @RequestParam Integer hotelId,
        HttpServletRequest request) {

    Date checkIn;
    Date checkOut;
    try {
        checkIn = new SimpleDateFormat("yyyy-MM-dd").parse(checkInDate);
        checkOut = new SimpleDateFormat("yyyy-MM-dd").parse(checkOutDate);
    } catch (ParseException e) {
        return new ResponseEntity<>("Invalid date format. Please use yyyy-MM-dd.", HttpStatus.BAD_REQUEST);
    }

    // Get the principal from the security context
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    // Check if the principal is an instance of UserInfoDetails
    if (principal instanceof UserInfoDetails) {
        UserInfoDetails userInfo = (UserInfoDetails) principal;
        Customer customer = userInfo.getCustomer(); // Assuming this method exists

        Optional<Room> roomOpt = roomRepository.findById(roomId);
        if (roomOpt.isEmpty()) {
            return new ResponseEntity<>("Room not found", HttpStatus.BAD_REQUEST);
        }
        Room room = roomOpt.get();

        Optional<Hotel> hotelOpt = hotelRepository.findById(hotelId);
        if (hotelOpt.isEmpty()) {
            return new ResponseEntity<>("Hotel not found", HttpStatus.BAD_REQUEST);
        }
        Hotel hotel = hotelOpt.get();

        // Force initialization of the rooms collection
        hotel.getRooms().size();

        Booking booking = new Booking();
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking.setRoom(room);
        booking.setCustomer(customer);
        booking.setHotel(hotel);

        Booking createdBooking = bookingService.addBooking(booking);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    } else {
        return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
    }
}}
