package com.ict.finalpj.domain.deal.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ict.finalpj.domain.deal.vo.DealSatisfactionVO;

@Mapper
public interface DealSatisfactionMapper {
    // 만족도 등록
    int getDealSatisfactionInsert(DealSatisfactionVO satisfactionVO);
    
    // 판매자의 평점 조회
    String getDealSatisSellerScore(String dealSellerUserIdx);
    
    // 판매자 평점 통계 조회
    Map<String, Object> getDealSatisSellerScoreStats(String dealSellerUserIdx);
    
    // 판매자 평점 업데이트
    int updateDealSatisSellerScore(@Param("dealSellerUserIdx") String dealSellerUserIdx, 
                                  @Param("dealSatisAverage") BigDecimal dealSatisAverage);

    
    List<DealSatisfactionVO> getDealSatisfactionList(String userIdx);
} 