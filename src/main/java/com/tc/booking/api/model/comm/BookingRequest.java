package com.tc.booking.api.model.comm;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BookingRequest {
    private int customerId;
    private int roomId;
    private Date checkInDate;
    private Date checkOutDate;
}
