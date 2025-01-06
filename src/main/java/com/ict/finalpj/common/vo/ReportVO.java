package com.ict.finalpj.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportVO {

  private String reportIdx;
  private String userIdx;
  private String reportedUserIdx;
  private String reportCategory;
  private String reportTableType;
  private String reportTableIdx;
  private String reportContent;
  private String reportRegDate;
  private String reportStatus;

}
