package com.ict.finalpj.domain.book.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookVO {
    private String bookIdx, campIdx, userIdx, bookCheckInDate, bookCheckOutDate, bookSelectedZone, 
        bookAdultCount , bookYouthCount, bookChildCount, bookCarCount, bookTotalPrice, bookUserName,
        bookUserPhone, bookCar1, bookCar2, bookRequest, orderId, planIdx;
}
