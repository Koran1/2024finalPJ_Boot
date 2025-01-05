package com.ict.finalpj.domain.deal.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DealVO {
    private String dealIdx;
    private String dealSellerUserIdx;
    private String dealSellerNick;
    private String dealBuyerUserIdx;
    private String dealBuyerNick;
    private String dealTitle;
    private String dealCategory;
    private String dealStatus;
    private String dealDescription;
    private String dealPrice;
    private String dealPackage;
    private String dealDirect;
    private String dealDirectContent;
    private String dealCount;
    private String dealRegDate;
    private String dealRegDateUpdate;
    private String dealUserFavorCount;
    private String dealUserViewCount;
}
