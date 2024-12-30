package com.ict.finalpj.domain.deal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ict.finalpj.domain.deal.vo.DealVO;

@Mapper
public interface DealMapper {
  List<DealVO> getDealMainList();
  DealVO getDealDetail(String dealIdx);
  int getDealUpdate(DealVO dealVO);
  int getDealWrite(DealVO dealVO);
}
