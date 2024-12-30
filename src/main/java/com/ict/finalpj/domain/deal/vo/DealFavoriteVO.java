package com.ict.finalpj.domain.deal.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DealFavoriteVO {
  private String dealFavoriteIdx, userIdx, dealIdx;
}
