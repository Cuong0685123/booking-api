package com.tc.booking.api.controller;

import com.tc.booking.model.entity.Hotel;
import com.tc.booking.api.service.impl.HotelServiceImpl;
import com.tc.booking.api.JwtHelper;
import com.tc.booking.api.exception.ApiException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private HotelServiceImpl hotelService;

    @Autowired
    private JwtHelper jwtHelper;

    // Helper method to check if user is admin
    private boolean isAdmin(HttpServletRequest request) throws ApiException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ApiException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7); // Remove "Bearer " prefix
        Claims claims = jwtHelper.parseJwtToken(token);

        // Check if the username in the token is 'admin'
        String username = claims.getSubject();
        return "admin".equals(username);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addHotelWithRooms(@RequestBody Hotel hotel, HttpServletRequest request) throws ApiException {
        if (!isAdmin(request)) {
            return ResponseEntity.status(403).body("Access Denied. Admins only.");
        }

        hotelService.addHotelWithRooms(hotel);
        return ResponseEntity.ok("Hotel added successfully.");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateHotel(@PathVariable int id, @RequestBody Hotel updatedHotel, HttpServletRequest request) throws ApiException {
        if (!isAdmin(request)) {
            return ResponseEntity.status(403).body("Access Denied. Admins only.");
        }

        hotelService.updateHotel(id, updatedHotel);
        return ResponseEntity.ok("Hotel updated successfully.");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteHotel(@PathVariable int id, HttpServletRequest request) throws ApiException {
        if (!isAdmin(request)) {
            return ResponseEntity.status(403).body("Access Denied. Admins only.");
        }

        hotelService.deleteHotel(id);
        return ResponseEntity.ok("Hotel deleted successfully.");
    }
}
