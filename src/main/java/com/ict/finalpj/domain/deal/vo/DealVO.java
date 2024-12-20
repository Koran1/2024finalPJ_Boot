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
  dealDirectContent, 
  dealCount, 
  dealRegDate, 
  dealRegDateUpdate, 
  dealUserFavorCount, 
  dealUserViewCount, 
  dealView, 
  deal01, 
  deal02, 
  deal03;
  private FileVo fileVO;
  private List<FileVo> fileList;

  public void setFileList(List<FileVo> fileList) {
    this.fileList = fileList;
  }

  public List<FileVo> getFileList() {
    return fileList;
  }
}

