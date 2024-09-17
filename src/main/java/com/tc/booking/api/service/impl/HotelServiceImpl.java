package com.tc.booking.api.service.impl;

import com.tc.booking.api.service.HotelService;
import com.tc.booking.model.entity.Hotel;
import com.tc.booking.api.exception.BookingException;
import com.tc.booking.model.entity.Room;
import com.tc.booking.repo.HotelRepository;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        for (Room room : hotel.getRooms()) {
            room.setHotel(hotel);
        }
        return hotelRepository.save(hotel);
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

        // Cập nhật thông tin cơ bản của khách sạn
        hotel.setName(hotelDetails.getName());
        hotel.setAddress(hotelDetails.getAddress());
        hotel.setRating(hotelDetails.getRating());

        // Cập nhật danh sách phòng
        hotel.getRooms().clear(); // Xóa danh sách phòng hiện tại
        for (Room room : hotelDetails.getRooms()) {
            room.setHotel(hotel); // Thiết lập quan hệ với hotel
            hotel.getRooms().add(room); // Thêm lại các phòng vào danh sách
        }

        return hotelRepository.save(hotel); // Lưu thông tin khách sạn cùng phòng mới
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
        
        // Nếu muốn xóa luôn các bản ghi booking liên quan, bạn có thể cần thực hiện xóa thủ công ở đây

        hotelRepository.delete(hotel);
    }
}
