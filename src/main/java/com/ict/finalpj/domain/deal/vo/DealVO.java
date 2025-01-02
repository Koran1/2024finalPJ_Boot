package com.ict.finalpj.domain.deal.vo;

import java.util.List;

import com.ict.finalpj.common.vo.FileVo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private String deal01;
    private String deal02;
    private String deal03;
    private int dealview;
    
    // FileVO에서 처리하는 것이 더 적절할 수 있지만,
    // DealVO가 파일 정보를 포함해야 하는 비즈니스 요구사항이 있다면 현재 구조를 유지하는 것이 좋습니다.
    // 특히 DealController에서 메인 파일과 파일 목록을 함께 처리하는 로직이 있어
    // 현재는 이 구조가 더 효율적일 수 있습니다.
    private FileVo mainFile;
    private List<FileVo> fileList;
}