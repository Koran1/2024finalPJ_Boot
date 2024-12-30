package com.ict.finalpj.domain.deal.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DealVO {
  private String dealIdx, 
  dealSellerUserIdx, 
  dealSellerNick, 
  dealBuyerUserIdx, 
  dealBuyerNick, 
  dealTitle, 
  dealCategory, 
  dealStatus, 
  dealDescription, 
  dealPrice, 
  dealPackage, 
  dealDirect, 
  dealCount, 
  dealRegDate, 
  dealRegDateUpdate, 
  dealUserFavorCount, 
  dealUserViewCount, 
  dealView, 
  deal01, 
  deal02, 
  deal03;
}

