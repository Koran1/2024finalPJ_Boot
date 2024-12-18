package com.ict.finalpj.domain.deal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.domain.deal.mapper.DealMapper;
import com.ict.finalpj.domain.deal.vo.DealVO;

@Service
public class DealServiceImpl implements DealService {

  @Autowired
  private DealMapper dealMapper;

  @Override
  public List<DealVO> getDealMainList() {
    return dealMapper.getDealMainList();
  }

  @Override
  public DealVO getDealDetail(String dealIdx) {
    return dealMapper.getDealDetail(dealIdx);
  }

  @Override
  public int getDealUpdate(DealVO dealVO) {
    return dealMapper.getDealUpdate(dealVO);
  }

  @Override
  public int getDealWrite(DealVO dealVO) {
    return dealMapper.getDealWrite(dealVO);
  }

}
