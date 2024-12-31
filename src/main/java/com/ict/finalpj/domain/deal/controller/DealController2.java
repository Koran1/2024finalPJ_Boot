package com.ict.finalpj.domain.deal.controller;

import java.util.List;

import javax.xml.crypto.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.domain.deal.service.DealService;
import com.ict.finalpj.domain.deal.vo.DealFavoriteVO;
import com.ict.finalpj.domain.deal.vo.DealVO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RestController
@RequestMapping("/api/deal")
public class DealController2 {
  
  @Autowired
  private DealService dealService;

  @GetMapping("/management/{userIdx}")
  public DataVO getDealManagement(@PathVariable("userIdx") String userIdx) {
      DataVO dataVO = new DataVO();
      try {
          if (userIdx == null || userIdx.isEmpty()) {
              dataVO.setSuccess(false);
              dataVO.setMessage("유효하지 않은 사용자 ID입니다.");
              return dataVO;
          }
  
          // 거래 관리 정보를 가져옵니다.
          List<DealVO> list = dealService.getDealManagement(userIdx);
          log.info(userIdx);
          log.info("deal VO 저장");

  
          if (list == null) {
              dataVO.setSuccess(false);
              dataVO.setMessage("거래 관리 정보를 찾을 수 없습니다.");
          } else {
              dataVO.setSuccess(true);
              dataVO.setMessage("거래 관리 정보를 성공적으로 가져왔습니다.");
              dataVO.setData(list);
          }
      } catch (Exception e) {
          dataVO.setSuccess(false);
          dataVO.setMessage("거래 관리 정보 조회 중 오류가 발생했습니다.");
          // 예외 정보 로깅
          e.printStackTrace(); // 또는 log.error("Error: ", e);
      }
      return dataVO;
  }
  
  @GetMapping("/dealMainSearch")
  public DataVO getMethodName(@RequestParam("searchKeyword") String searchKeyword) {
      DataVO dvo = new DataVO();
      try {
        List<DealVO> searchList = dealService.getDealMainSearch(searchKeyword);
        log.info("searchList : " + searchList);
        dvo.setData(searchList);
        dvo.setSuccess(true);
        dvo.setMessage("검색 성공");
      } catch (Exception e) {
        dvo.setSuccess(false);
        dvo.setMessage("검색 실패");
        e.printStackTrace();
      }
      return dvo;
  }

  // 찜하기 등록 (Create)
  @GetMapping("/dealMainfavorite")
  public DataVO getFavorite(
    @RequestParam("userIdx") String userIdx , @RequestParam("dealIdx") String dealIdx) {
      DataVO dvo = new DataVO();
      try {
        log.info("userIdx(등록) : " + userIdx);
        log.info("dealIdx : " + dealIdx);

        DealFavoriteVO dfvo = new DealFavoriteVO();
        dfvo.setUserIdx(userIdx);
        dfvo.setDealIdx(dealIdx);

        dealService.getFavorite(dfvo);
        dvo.setSuccess(true);
        dvo.setMessage("찜 성공");
      } catch (Exception e) {
        dvo.setSuccess(false);
        dvo.setMessage("찜 실패");
        e.printStackTrace();
      }
      return dvo;
  }
 
  // 찜한 목록들 불러오기 (Read)
  @GetMapping("/getFavoriteList")
  public DataVO getFavoriteList(
    @RequestParam("userIdx") String userIdx
    ) {
      DataVO dvo = new DataVO();
      try {
        log.info("userIdx(찜 목록 가져오기) : " + userIdx);
        List<DealVO> favList = dealService.getFavoriteList(userIdx);
        dvo.setData(favList);
        dvo.setSuccess(true);
        dvo.setMessage("찜 목록 가져오기 성공");
      } catch (Exception e) {
        e.printStackTrace();
        dvo.setSuccess(false);
        dvo.setMessage("찜 목록 가져오기 실패!");
      }
      return dvo;
  }
  
  // 찜한 목록 삭제
  @DeleteMapping("/deleteFavorite")
  public DataVO deleteFavorite(
    @RequestParam("dealIdx") String dealIdx,
    @RequestParam("userIdx") String userIdx
  ){
    DataVO dvo = new DataVO();
    try {
      log.info("dealIdx(삭제) : " + dealIdx);
      log.info("userIdx : " + userIdx);
      DealFavoriteVO dfvo = new DealFavoriteVO();
      dfvo.setUserIdx(userIdx);
      dfvo.setDealIdx(dealIdx);
      dealService.deleteFavorite(dfvo);

      dvo.setSuccess(true);
      dvo.setMessage("삭제 성공");

    } catch (Exception e) {
      e.printStackTrace();
        dvo.setSuccess(false);
        dvo.setMessage("삭제 실패!");
    }
    return dvo;
  }
  

  
}