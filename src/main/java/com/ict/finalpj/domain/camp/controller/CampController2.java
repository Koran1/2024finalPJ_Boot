package com.ict.finalpj.domain.camp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.domain.camp.service.CampService2;
import com.ict.finalpj.domain.camp.vo.CampVO;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RestController
@RequestMapping("/api/camp")
public class CampController2 {
    @Autowired
    private CampService2 campService2;

    @GetMapping("/detail/{campIdx}")
    public DataVO getCampDetail(@PathVariable String campIdx) {
        DataVO dataVO = new DataVO();
        try {
            CampVO campDetail = campService2.getCampDetail(campIdx);
            dataVO.setSuccess(true);
            dataVO.setMessage("캠핑장 상세보기 성공");
            dataVO.setData(campDetail);
            log.info("캠핑장 상세보기 성공");
        } catch (Exception e) {
            dataVO.setSuccess(true);
            dataVO.setMessage("캠핑장 상세보기 실패");
            log.info("캠핑장 상세보기 실패");
        }
        return dataVO;
    }

    @GetMapping("/fav/{campIdx}")
    public DataVO getFavCamp(@RequestParam String campIdx, Authentication authentication) {
        DataVO dataVO = new DataVO();
        try {
            // 로그인 여부 확인
            if (authentication == null) {
                dataVO.setSuccess(false);
                dataVO.setMessage("로그인이 필요합니다.");
                return dataVO;
            }
            // int result = campService2.getFavCamp(campIdx);
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("게스트북 삭제 오류 발생");
        }
        return dataVO;
    }

}
