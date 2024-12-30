package com.ict.finalpj.domain.camp.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampCmtVO {
    private String campCmtIdx, campIdx, userIdx, campCmtUpcnt, campCmtRegdate, campCmtContent, cmtActive;
}
