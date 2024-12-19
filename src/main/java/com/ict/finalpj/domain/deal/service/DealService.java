package com.ict.finalpj.domain.deal.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.domain.deal.vo.DealVO;

@Service
public interface DealService {
  List<DealVO> getDealMainList();
  DealVO getDealDetail(String dealIdx);
  int getDealUpdate(DealVO dealVO);
  @Transactional
  int getDealWrite(DealVO dealVO);
  void insertFileInfo(FileVo fileVo);
}
