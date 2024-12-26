package com.ict.finalpj.domain.deal.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.domain.deal.vo.DealVO;


public interface DealService {
  List<DealVO> getDealMainList();
  DealVO getDealDetail(String dealIdx);
  int getDealUpdate(DealVO dealVO);
  DataVO getDealWrite(DealVO dealVO, MultipartFile[] files);
  List<FileVo> listFile(String fileTableIdx);
  int saveFile(FileVo fileVo);
  int updateFile(FileVo fileVo);
  int deleteFile(String fileIdx);
  DealVO getDealDetailWithFiles(String dealIdx);
  List<FileVo> getPjFileByDealIdx(String dealIdx);
  List<DealVO> getDealManagement(String userIdx);
  void updateFileInfo(FileVo fileVo);

  DataVO getDealFileDelete(String fileTableIdx);
}
