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
  private String dealSatisSellerIdx, dealSatisSellerUserIdx, dealSatisSellerNick, dealSatisBuyerUserIdx, 
  dealSatisBuyerNick, dealSatisBuyerContent, dealSatisBuyerRegDate, dealSatisBuyerScore, dealSatis01, dealSatis02;
}
