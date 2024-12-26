package com.ict.finalpj.domain.deal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.domain.deal.vo.DealFavoriteVO;
import com.ict.finalpj.domain.deal.vo.DealVO;

@Mapper
public interface DealMapper {
  List<DealVO> getDealMainList();
  FileVo getDealFileOne(String fileTableIdx);

  DealVO getDealDetail(String dealIdx);
  List<FileVo> getDealFileDetail(String fileTableIdx);

  int getDealWrite(DealVO dealVO);
  int getIDealFileInsert(FileVo fileVo);
  
  int getDealFileDelete(FileVo fileVo);
  int getDealFileUpdate(FileVo fileVo);
  int getDealUpdate(DealVO dealVO);
  
  List<DealVO> getDealManagement(String userIdx);
  List<DealFavoriteVO> getDealinterest(String userIdx);
}
