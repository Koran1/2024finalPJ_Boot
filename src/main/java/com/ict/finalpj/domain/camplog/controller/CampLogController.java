package com.ict.finalpj.domain.camplog.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.domain.camplog.service.CampLogService;
import com.ict.finalpj.domain.camplog.vo.CampLogListVO;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RestController
@RequestMapping("/api/camplog")
public class CampLogController {
    @Autowired
    private CampLogService campLogService;

    @GetMapping("/list")
    public DataVO getCamplogList(CampLogListVO campLogListVO) {
        DataVO dataVO = new DataVO();
        try {
            Map<String, Object> camplogList = campLogService.getCamplogList(campLogListVO);
            dataVO.setSuccess(true);
            dataVO.setMessage("캠핑로그 리스트 조회 성공");
            dataVO.setData(camplogList);
            log.info("캠핑로그 리스트 조회 성공");
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("캠핑로그 리스트 조회 실패");
            log.info("캠핑로그 리스트 조회 실패", e);
        }
        return dataVO;
    }
}
