package com.ict.finalpj.domain.mycamp.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanVO {
    private String planIdx, userIdx, planTitle, planBeginDate, planEndDate, campIdx, planStartingPoint, planIsBooking, planUpdateDate, planRegDate;
}