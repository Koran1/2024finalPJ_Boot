package com.ict.finalpj.domain.deal.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.domain.deal.vo.DealVO;

@Service
public interface DealService {
  List<DealVO> getDealMainList();
  FileVo getDealFileOne(String dealIdx);

  DealVO getDealDetail(String dealIdx);
  List<FileVo> getDealFileDetail(String dealIdx);

  int getDealWrite(DealVO dealVO);
  void getIDealFileInsert(FileVo fileVo);
  
  void getDealFileDelete(FileVo fileVo);
  void getDealFileUpdate(String dealIdx);
  DataVO getDealUpdate(DealVO dealVO, MultipartFile[] files);
  
  List<DealVO> getDealManagement(String userIdx);
}