package com.ict.finalpj.domain.camp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.domain.camp.service.CampService2;
import com.ict.finalpj.domain.camp.vo.CampVO;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

}
