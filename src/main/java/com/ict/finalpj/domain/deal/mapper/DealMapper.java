package com.ict.finalpj.domain.deal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.domain.deal.vo.DealVO;

@Mapper
public interface DealMapper {
  List<DealVO> getDealMainList();
  FileVo getFileVO(String dealIdx);

  DealVO getDealDetail(String dealIdx);
  List<FileVo> getPjFileByDealIdx(String dealIdx);

  int updateDeal(DealVO dealVO);
  int getDealWrite(DealVO dealVO);
  void insertFile(FileVo fileVO);
  void updateFile(FileVo fileVO);
  void insertFileInfo(FileVo fileVo);
  List<DealVO> getDealManagement(String userIdx);
}
