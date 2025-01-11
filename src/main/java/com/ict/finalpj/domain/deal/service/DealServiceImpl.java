package com.ict.finalpj.domain.deal.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.common.vo.ReportVO;
import com.ict.finalpj.common.vo.ViewsVO;
import com.ict.finalpj.domain.camplog.vo.CampLogListVO;
import com.ict.finalpj.domain.deal.mapper.DealMapper;
import com.ict.finalpj.domain.deal.mapper.DealSatisfactionMapper;
import com.ict.finalpj.domain.deal.vo.DealSatisfactionVO;
import com.ict.finalpj.domain.deal.vo.DealVO;
import com.ict.finalpj.domain.user.vo.UserVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DealServiceImpl implements DealService {

    @Autowired
    private DealMapper dealMapper;

    @Autowired
    private DealSatisfactionMapper dealSatisfactionMapper;

    @Override
    public List<DealVO> getDealMainList() {
        return dealMapper.getDealMainList();
    }
    
    @Override
    public FileVo getDealFileOne(String fileTableIdx) {
        return dealMapper.getDealFileOne(fileTableIdx);
    }

    @Override
    public DealVO getDealDetail(String dealIdx) {
        log.info("getDealDetail 호출됨: dealIdx={}", dealIdx); // 로그 추가
        DealVO deal = dealMapper.getDealDetail(dealIdx);
        if (deal == null) {
            log.warn("해당 dealIdx에 대한 DealVO를 찾을 수 없습니다: {}", dealIdx);
        } else {
            log.info("DealVO 조회 성공: {}", deal);
        }
        return deal;
    }
    
    @Override
    public List<FileVo> getDealFileDetail(String fileTableIdx) {
        return dealMapper.getDealFileDetail(fileTableIdx);
    }

    @Override
    public int getDealWrite(DealVO dealVO) {
        return dealMapper.getDealWrite(dealVO);
    }

    @Override
    public int getIDealFileInsert(FileVo fileVo) {
        return dealMapper.getIDealFileInsert(fileVo);
    }

    @Override
    public int getDealUpdate(DealVO dealVO) {
        return dealMapper.getDealUpdate(dealVO);
    }

    @Override
    public int getDealFileUpdate(FileVo fileVo) {
        return dealMapper.getDealFileUpdate(fileVo);
    }
    
    @Override
    public int getDealFileNameDelete(String fileTableIdx, String fileName) {
        return dealMapper.getDealFileNameDelete(fileTableIdx, fileName);
    }

    @Override
    public int getDealFileOrder(FileVo fileVo) {
        return dealMapper.getDealFileOrder(fileVo);
    }

    // 좋아요 상태 확인
    @Override
    public boolean isLiked(String userIdx, String dealIdx) {
        return dealMapper.isLiked(userIdx, dealIdx);
    }

    // 좋아요 추가
    @Override
    public int likeDeal(String userIdx, String dealIdx) {
        return dealMapper.likeDeal(userIdx, dealIdx);
    }

    // 좋아요 삭제
    @Override
    public int unlikeDeal(String userIdx, String dealIdx) {
        return dealMapper.unlikeDeal(userIdx, dealIdx);
    }
    
    @Override
    public int getFavoriteCount(String dealIdx) {
        return dealMapper.getFavoriteCount(dealIdx);
    }

    // 조회수 관련 메서드 구현
    @Override
    public ViewsVO getViewCount(String userIdx, String dealIdx) {
        return dealMapper.getViewCount(userIdx, dealIdx);
    }

    @Override
    public int insertViewCount(String userIdx, String dealIdx) {
        return dealMapper.insertViewCount(userIdx, dealIdx);
    }

    @Override
    public int updateViewCount(String userIdx, String dealIdx) {
        // 조회수 정보 확인
        ViewsVO viewTable = dealMapper.getViewCount(userIdx, dealIdx);

        if (viewTable == null) {
            // 조회수 정보가 없으면 새로 추가
            return dealMapper.insertViewCount(userIdx, dealIdx);
        } else {
            // viewRegTime을 String에서 Date로 변환
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date viewRegTime = null;
            try {
                viewRegTime = sdf.parse(viewTable.getViewRegTime());
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }

            // 24시간 경과 체크
            if (viewRegTime != null) {
                long diffInMillis = System.currentTimeMillis() - viewRegTime.getTime();
                long diffInHours = diffInMillis / (60 * 60 * 1000);

                if (diffInHours >= 24) {
                    // 24시간 이상 경과시 조회수 증가 및 시간 업데이트
                    return dealMapper.updateViewCount(userIdx, dealIdx);
                }
            }
        }
        return 0;
    }

    @Override
    public UserVO getUserInfoByIdx(String userIdx) {
        return dealMapper.getUserInfoByIdx(userIdx);
    }

    @Override
    public int getTotalViewCount(String dealIdx) {
        return dealMapper.getTotalViewCount(dealIdx);
    }

    @Override
    public int getDealStatusUpdate(DealVO dealvo) {
        return dealMapper.getDealStatusUpdate(dealvo);
    }

    @Override
    public List<DealVO> getSellerOtherDeals(String dealSellerUserIdx, String dealIdx) {
        return dealMapper.getSellerOtherDeals(dealSellerUserIdx, dealIdx);
    }
    
    @Override
    public int getDealSatisfactionInsert(DealSatisfactionVO satisfactionVO) {
        return dealMapper.getDealSatisfactionInsert(satisfactionVO);
    }

    @Override
    public boolean chkSatisfaction(String dealSatis01) {
        return dealMapper.chkSatisfaction(dealSatis01);
    }

    @Override
    public String getDealSatisSellerScore(String dealSellerUserIdx) {
        return dealSatisfactionMapper.getDealSatisSellerScore(dealSellerUserIdx);
    }

    @Override
    public void getDealSatisSellerScoreUpdate(String dealSellerUserIdx) {
        try {
            // 판매자 평점 통계 조회
            Map<String, Object> stats = dealSatisfactionMapper.getDealSatisSellerScoreStats(dealSellerUserIdx);
            
            long count = ((Number) stats.get("dealSatisSellerCount")).longValue();
            BigDecimal sum = new BigDecimal(stats.get("dealSatisSellerScoreSum").toString());
            
            // 평점이 없는 경우 기본값 5.0 설정
            BigDecimal average = count > 0 
                ? sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP)
                : new BigDecimal("5.00");
                
            // 평점 업데이트
            dealSatisfactionMapper.updateDealSatisSellerScore(dealSellerUserIdx, average);
            
            log.info("판매자 평점 업데이트 완료 - dealSellerUserIdx: {}, 평균평점: {}", 
                    dealSellerUserIdx, average);
                    
        } catch (Exception e) {
            log.error("판매자 평점 업데이트 실패 - dealSellerUserIdx: {}", dealSellerUserIdx, e);
            throw new RuntimeException("판매자 평점 업데이트 실패", e);
        }
    }

    @Override
    public int getDealActiveUpdate(String dealIdx, int dealview) {
        return dealMapper.getDealActiveUpdate(dealIdx, dealview);
    }

    @Override
    public List<DealVO> getDealManagement(String userIdx) {
        return dealMapper.getDealManagement(userIdx);
    } 

    @Override
    public List<DealVO> getDealMainSearch(String searchKeyword) {
        return dealMapper.getDealMainSearch(searchKeyword);
    }

    @Override
    public List<DealVO> getFavoriteList(String userIdx) {
        return dealMapper.getFavoriteList(userIdx);
    }

    @Override
    public List<DealVO> getPurchaseList(String userIdx) {
        return dealMapper.getPurchaseList(userIdx);
    }

    @Override
    public List<DealSatisfactionVO> getDealSatisfactionList(String userIdx) {
        return dealSatisfactionMapper.getDealSatisfactionList(userIdx);
    }

    @Override
    public int getDealReportInsert(ReportVO reportVO) {
        return dealMapper.getDealReportInsert(reportVO);
    }

    @Override
    public int updateReportStatus(String dealIdx) {
        return dealMapper.updateReportStatus(dealIdx);
    }

    @Override
    public List<DealSatisfactionVO> getSellerSatisfactions(String userIdx) {
        return dealMapper.getSellerSatisfactions(userIdx);
    }

    @Override
    public List<CampLogListVO> getSellerCampLogs(String sellerIdx) {
        return dealMapper.getSellerCampLogs(sellerIdx);
    }

    @Override
    public int updateDealStatus(String dealIdx, String status) {
        return dealMapper.updateDealStatus(dealIdx, status);
    }

}