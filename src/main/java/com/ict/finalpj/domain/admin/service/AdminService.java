package com.ict.finalpj.domain.admin.service;

import java.util.List;
import java.util.Map;

import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.domain.deal.vo.DealVO;
import com.ict.finalpj.domain.add.vo.QNAVO;
import com.ict.finalpj.domain.camp.vo.CampSearchVO;

public interface AdminService {
  List<DealVO> getDealManagement();

  DealVO getDealDetail(String dealIdx);

  List<FileVo> getDealFiles(String dealIdx);

  int getFavoriteCount(String dealIdx);

  Map<String, Object> getSellerOtherDeals(String dealIdx);

  String getDealSatisSellerScore(String userIdx);

  int getAdminDealActiveUpdate(String dealIdx, int dealview);

  // 캠핑정보관리
  Map<String, Object> getCampingList(CampSearchVO campSearchVO);

  // QNA 관리
  List<QNAVO> getQnaList();
}
