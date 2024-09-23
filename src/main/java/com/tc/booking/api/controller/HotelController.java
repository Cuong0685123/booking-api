package com.tc.booking.api.controller;

import com.tc.booking.model.entity.Hotel;
import com.tc.booking.api.service.impl.HotelServiceImpl;
import com.tc.booking.api.JwtHelper;
import com.tc.booking.api.exception.ApiException;
import com.tc.booking.api.exception.BookingException;
import com.tc.booking.model.entity.Room;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;

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
        String username = claims.getSubject();
        return "admin".equals(username);
    }

   @PostMapping("/add")
@CrossOrigin(origins = "http://localhost:3000")
public ResponseEntity<String> addHotelWithRooms(@RequestBody Hotel hotel, HttpServletRequest request) throws ApiException {
    Logger.getLogger(HotelController.class.getName()).log(Level.INFO, "Adding hotel: " + hotel);
    
    if (!isAdmin(request)) {
        return ResponseEntity.status(403).body("Access Denied. Admins only.");
    }

    try {
        hotelService.addHotelWithRooms(hotel);
        return ResponseEntity.ok("Hotel added successfully.");
    } catch (Exception e) {
        Logger.getLogger(HotelController.class.getName()).log(Level.SEVERE, "Error adding hotel", e);
        return ResponseEntity.status(500).body("Error adding hotel: " + e.getMessage());
    }
}


    @PutMapping("/update/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> updateHotel(@PathVariable int id, @RequestBody Hotel updatedHotel, HttpServletRequest request) throws ApiException {
        if (!isAdmin(request)) {
            return ResponseEntity.status(403).body("Access Denied. Admins only.");
        }

        hotelService.updateHotel(id, updatedHotel);
        return ResponseEntity.ok("Hotel updated successfully.");
    }

    @DeleteMapping("/delete/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> deleteHotel(@PathVariable int id, HttpServletRequest request) throws ApiException {
        if (!isAdmin(request)) {
            return ResponseEntity.status(403).body("Access Denied. Admins only.");
        }

        hotelService.deleteHotel(id);
        return ResponseEntity.ok("Hotel deleted successfully.");
    }
    
@GetMapping("/all")
    @CrossOrigin(origins = "*") // Để cho phép tất cả các origin
    public ResponseEntity<?> getAllHotels() {
        try {
            List<Hotel> hotels = hotelService.getAllHotels();
            return ResponseEntity.ok(hotels);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi lấy danh sách khách sạn: " + e.getMessage());
        }
    }

    
    @GetMapping("/{id}/rooms")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<List<Room>> getRoomsByHotelId(@PathVariable int id) throws BookingException {
        List<Room> rooms = hotelService.getRoomsByHotelId(id);
        return ResponseEntity.ok(rooms);
    }
    
     @GetMapping("/{id}")
      @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Hotel> getHotelById(@PathVariable int id) {
        Hotel hotel = hotelService.getHotelById(id);
        if (hotel == null) {
            return ResponseEntity.notFound().build(); // Return 404 if hotel not found
        }
        return ResponseEntity.ok(hotel); // Return hotel if found
    }
}

