package com.ict.finalpj.domain.mycamp.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPlanVO {
    private String planIdx, // 계획 글 번호(idx)
            userIdx, // 회원 번호
            planTitle, // 계획 제목
            planBeginDate, // 캠핑 출발 날짜(달력)
            planEndDate, // 캠핑 종료 날짜(달력)
            campIdx, // 캠핑장 고유번호
            planStartingPoint, // 출발 위치
            planIsBooking, // 예약 여부
            planUpdateDate, // 계획 수정일
            planRegDate // 계획 작성일
    ;
}
