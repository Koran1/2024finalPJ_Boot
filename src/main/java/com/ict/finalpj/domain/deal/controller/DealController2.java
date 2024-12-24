package com.ict.finalpj.domain.deal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.domain.deal.service.DealService;
import com.ict.finalpj.domain.deal.vo.DealVO;

import lombok.extern.slf4j.Slf4j;

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
          DealVO list = dealService.getDealManagement(userIdx);
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
  

}