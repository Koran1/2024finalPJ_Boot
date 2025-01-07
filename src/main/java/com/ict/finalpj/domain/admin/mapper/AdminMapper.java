package com.ict.finalpj.domain.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.domain.deal.vo.DealVO;

@Mapper
public interface AdminMapper {
  List<DealVO> getDealManagement();
  DealVO getDealDetail(String dealIdx);
  List<FileVo> getDealFiles(String dealIdx);
  int getFavoriteCount(String dealIdx);
  List<DealVO> getSellerOtherDeals(String dealIdx);
  List<FileVo> getSellerOtherFiles(String dealIdx);
  String getDealSatisSellerScore(String userIdx);
  int getAdminDealActiveUpdate(String dealIdx, int dealview);
}
