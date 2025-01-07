package com.ict.finalpj.domain.admin.service;

import java.util.List;
import java.util.Map;

import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.domain.deal.vo.DealVO;

public interface AdminService {
  List<DealVO> getDealManagement();
  DealVO getDealDetail(String dealIdx);
  List<FileVo> getDealFiles(String dealIdx);
  int getFavoriteCount(String dealIdx);
  Map<String, Object> getSellerOtherDeals(String dealIdx);
  String getDealSatisSellerScore(String userIdx);
  int getAdminDealActiveUpdate(String dealIdx, int dealview);
}
