package com.tc.booking.api.service;

import com.tc.booking.model.entity.Hotel;
import com.tc.booking.model.entity.Room;
import java.util.List;

public interface HotelService {
    Hotel addHotelWithRooms(Hotel hotel);
    Hotel updateHotel(int id, Hotel hotelDetails);
    void deleteHotel(int id);
    List<Hotel> getAllHotels();
    List<Room> getRoomsByHotelId(int hotelId);
     Hotel getHotelById(int id);

}
