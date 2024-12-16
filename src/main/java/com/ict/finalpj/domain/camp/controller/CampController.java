package com.ict.finalpj.domain.camp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.domain.camp.service.CampService;
import com.ict.finalpj.domain.camp.vo.CampVO;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RestController
@RequestMapping("/api/camp")
public class CampController {
    @Autowired
    private CampService campService;

    @GetMapping("/list")
    public DataVO getCampingList() {
        DataVO dataVO = new DataVO();
        try {
            List<CampVO> campingList = campService.getCampingList();
            dataVO.setSuccess(true);
            dataVO.setMessage("캠핑장 리스트 조회 성공");
            dataVO.setData(campingList);
            log.info("캠핑장 리스트 조회 성공");
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("캠핑장 리스트 조회 실패");
            log.info("캠핑장 리스트 조회 실패");
        }
        return dataVO;
    }
    
    @GetMapping("/sido")
    public DataVO getDoNmList() {
        DataVO dataVO = new DataVO();
        try {
            // log.info("시도 리스트 조회 시작");
            List<String> doNmList = campService.getDoNmList();
            dataVO.setSuccess(true);
            dataVO.setMessage("시도 리스트 조회 성공");
            dataVO.setData(doNmList);
            log.info("시도 리스트 조회 성공");
        } catch (Exception e) {
            log.error("시도 리스트 조회 실패: {}", e.getMessage());
            dataVO.setSuccess(false);
            dataVO.setMessage("시도 리스트 조회 실패");
            log.info("시도 리스트 조회 실패");
        }
        return dataVO;
    }

    @GetMapping("/sigungu")
    public DataVO getSigunguList(@RequestParam("doNm2") String doNm2) {
        DataVO dataVO = new DataVO();
        try {
            // log.info("시도 리스트 조회 시작");
            List<String> sigunguList = campService.getSigunguList(doNm2);
            dataVO.setSuccess(true);
            dataVO.setMessage("시군구 리스트 조회 성공");
            dataVO.setData(sigunguList);
            log.info("시군구 리스트 조회 성공");
        } catch (Exception e) {
            log.error("시군구 리스트 조회 실패: {}", e.getMessage());
            dataVO.setSuccess(false);
            dataVO.setMessage("시군구 리스트 조회 실패");
            log.info("시군구 리스트 조회 실패");
        }
        return dataVO;
    }
}
