package com.ict.finalpj.domain.mycamp.vo;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyBookVO {
    private String 
        bookIdx, // 예약 고유번호
        campIdx, // 캠핑장 고유번호
        facltNm, // 캠핑장명
        firstImageUrl, // 캠핑장 대표사진
        campImg2, // 캠핑장 두 번째 사진
        userIdx, // 사용자 고유번호
        // bookCheckInDate, // 예약기간 시작날짜
        // bookCheckOutDate, // 예약기간 끝날짜
        bookSelectedZone, // 선택된 구역
        // bookAdultCount, // 성인 인원 수
        // bookYouthCount, // 청소년 인원 수
        // bookChildCount, // 미취학 아동 인원 수
        // bookCarCount, // 예약 차량 수
        planIdx, // 계획 글 고유번호
        planBeginDate, // 캠핑 출발 날짜
        planEndDate, // 캠핑 끝 날짜
        planTitle, // 계획 제목
        planRegDate, // 계획 작성일
        planUpdateDate, // 계획 수정일

        // 날씨 및 길찾기 정보
        regionCode,
        wthrTMin, wthrTMax, wthrSKY_PTY, wthrPOP,
        wthrSunrise, wthrSunset, wthrMoonrise, wthrMoonset, wthrLunAge,
        addr1,
        mapY,
        mapX
        ;

    private int 
        bookAdultCount, // 성인 인원 수
        bookYouthCount, // 청소년 인원 수
        bookChildCount, // 미취학 아동 인원 수
        bookCarCount // 예약 차량 수
    ;

    private LocalDate bookCheckInDate; // 예약기간 시작날짜
    private LocalDate bookCheckOutDate; // 예약기간 끝날짜
}
