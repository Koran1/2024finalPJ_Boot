package com.ict.finalpj.domain.deal.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.domain.deal.vo.DealVO;

@Service
public interface DealService {
  List<DealVO> getDealMainList();
  FileVo getFileVO(String dealIdx);

  DealVO getDealDetail(String dealIdx);
  DataVO updateDeal(DealVO dealVO, MultipartFile[] files);
  @Transactional
  DataVO getDealWrite(DealVO dealVO, MultipartFile[] files);
  void insertFileInfo(FileVo fileVo);
  List<FileVo> getPjFileByDealIdx(String dealIdx);
  List<DealVO> getDealManagement(String userIdx);
  void updateFileInfo(FileVo fileVo);

  DataVO getDealFileDelete(String fileTableIdx);
}