package com.ict.finalpj.domain.deal.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ict.finalpj.domain.deal.vo.DealSatisfactionVO;

@Mapper
public interface DealSatisfactionMapper {
    // 만족도 등록
    int getDealSatisfactionInsert(DealSatisfactionVO satisfactionVO);
    
    // 판매자의 평점 조회
    String getDealSatisSellerScore(String dealSellerUserIdx);
} 