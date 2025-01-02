package com.ict.finalpj.domain.deal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ict.finalpj.domain.deal.vo.DealSatisfactionVO;
import java.math.BigDecimal;
import java.util.Map;

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
} 