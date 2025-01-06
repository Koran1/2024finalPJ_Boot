package com.ict.finalpj.domain.deal.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DealSatisfactionVO {
  private String dealSatisSellerIdx;
  private String dealSatisSellerUserIdx;
  private String dealSatisSellerNick;
  private String dealSatisBuyerUserIdx;
  private String dealSatisBuyerNick;
  private String dealSatisBuyerContent;
  private String dealSatisBuyerRegDate;
  private String dealSatisSellerScore;
  private String dealSatis01;
  private String dealSatis02;

}
