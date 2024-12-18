package com.ict.finalpj.domain.deal.service;

import java.util.List;

import com.ict.finalpj.domain.deal.vo.DealVO;


public interface DealService {
  List<DealVO> getDealMainList();
  DealVO getDealDetail(String dealIdx);
  int getDealUpdate(DealVO dealVO);
  int getDealWrite(DealVO dealVO);
}
