package com.ict.finalpj.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportVO {
    private String reportIdx,
    userIdx,
    reportedUserIdx,
    reportCategory,
    reportTableType,
    reportTableIdx,
    reportContent,
    reportRegDate,
    reportStatus,
    isActive;

    private String reportCount;

    // 어드민 페이지에서 사용할 페이징, 검색용
    private String keyword, sortOption;
    private int page, size, offset;

}
