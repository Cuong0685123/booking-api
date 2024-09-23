package com.tc.booking.api.service.impl;

import com.tc.booking.api.service.HotelService;
import com.tc.booking.model.entity.Hotel;
import com.tc.booking.api.exception.BookingException;
import com.tc.booking.model.entity.Room;
import com.tc.booking.repo.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    @Autowired
    public HotelServiceImpl(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

@Override
@Transactional
public Hotel addHotelWithRooms(Hotel hotel) {
    // Thiết lập mối quan hệ cho mỗi phòng
    if (hotel.getRooms() != null) {
        for (Room room : hotel.getRooms()) {
            room.setHotel(hotel); // Thiết lập hotel cho mỗi room
        }
    }
    return hotelRepository.save(hotel); // Lưu hotel (cùng với rooms)
}





    @Override
    @Transactional
    public Hotel updateHotel(int id, Hotel hotelDetails) {
        Hotel hotel = null;
      try {
        hotel
            = hotelRepository.findById(id)
                .orElseThrow(() -> new BookingException("Hotel not found"));
      } catch (BookingException ex) {
        Logger.getLogger(HotelServiceImpl.class.getName())
            .log(Level.SEVERE, null, ex);
      }

        // Update basic hotel information
        hotel.setName(hotelDetails.getName());
        hotel.setAddress(hotelDetails.getAddress());
        hotel.setRating(hotelDetails.getRating());
        hotel.setImage(hotelDetails.getImage());

        // Update room list
        hotel.getRooms().clear(); // Clear current room list
        for (Room room : hotelDetails.getRooms()) {
            room.setHotel(hotel); // Set hotel reference
            hotel.getRooms().add(room); // Add new rooms
        }

        return hotelRepository.save(hotel); // Save hotel with updated rooms
    }

    @Override
    @Transactional
    public void deleteHotel(int id) {
        Hotel hotel = null;
      try {
        hotel
            = hotelRepository.findById(id)
                .orElseThrow(() -> new BookingException("Hotel not found"));
      } catch (BookingException ex) {
        Logger.getLogger(HotelServiceImpl.class.getName())
            .log(Level.SEVERE, null, ex);
      }

        hotelRepository.delete(hotel);
    }

   @Override
@Transactional(readOnly = true)
public List<Hotel> getAllHotels() {
    List<Hotel> hotels = hotelRepository.findAll();
    // Khởi tạo danh sách rooms cho từng hotel
    hotels.forEach(hotel -> hotel.getRooms().size());
    return hotels;
}


    @Override
    @Transactional(readOnly = true)
    public List<Room> getRoomsByHotelId(int hotelId) {
        Hotel hotel = null;
      try {
        hotel
            = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new BookingException("Hotel not found"));
      } catch (BookingException ex) {
        Logger.getLogger(HotelServiceImpl.class.getName())
            .log(Level.SEVERE, null, ex);
      }
        return hotel.getRooms();
    }
    
     @Override
    public Hotel getHotelById(int id) {
        return hotelRepository.findById(id).orElse(null); // Return hotel if found, otherwise return null
    }
}
