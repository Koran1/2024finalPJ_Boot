package com.ict.finalpj.domain.camp.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampSearchVO {
    private String keyword, doNm2, sigunguNm, sortOption;
    private int page, size, offset;
}
