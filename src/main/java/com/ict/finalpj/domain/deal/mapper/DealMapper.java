package com.ict.finalpj.domain.deal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.domain.deal.vo.DealVO;

@Mapper
public interface DealMapper {

  // 이상모
  List<DealVO> getDealMainList();
  DealVO getDealDetail(String dealIdx);
  int getDealUpdate(DealVO dealVO);
  int getDealWrite(DealVO dealVO);
  void insertFile(FileVo fileVO);
  void updateFile(FileVo fileVO);
  void insertFileInfo(FileVo fileVo);
  List<FileVo> getPjFileByDealIdx(String dealIdx);
  int updateDeal(DealVO dealVO);
  
  List<DealVO> getDealManagement(String userIdx);
  int getDealUpdate(DealVO dealVO);
  DataVO getDealWrite(DealVO dealVO, MultipartFile[] files);
  List<FileVo> listFile(String fileTableIdx);
  int saveFile(FileVo fileVo);
  int deleteFile(String fileIdx);
  @Insert("INSERT INTO pjFile (fileName, fileTableIdx, fileTableType) VALUES (#{fileName}, #{fileTableIdx}, #{fileTableType})")
  void updateFileInfo(FileVo fileVo);
  DealVO getDealDetailWithFiles(String dealIdx);


}

