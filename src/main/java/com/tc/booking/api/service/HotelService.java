package com.tc.booking.api.service;

import com.tc.booking.model.entity.Hotel;

public interface HotelService {
    Hotel addHotelWithRooms(Hotel hotel);
    Hotel updateHotel(int id, Hotel hotelDetails);
    void deleteHotel(int id);
}
