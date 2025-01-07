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
    reportStatus;

    private String reportCount;
}
