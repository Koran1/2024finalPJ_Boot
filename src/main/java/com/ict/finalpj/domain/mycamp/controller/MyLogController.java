package com.ict.finalpj.domain.mycamp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.domain.mycamp.service.MyLogService;
import com.ict.finalpj.domain.mycamp.vo.MyLogVO;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RestController
@RequestMapping("/api/mycamp/mylog")
public class MyLogController {
    @Autowired
    private MyLogService myLogService;
    
    @GetMapping("/list")
    public DataVO getMyLogList(MyLogVO myLogVO) {
        DataVO dataVO = new DataVO();
        try {
            Map<String, Object> myLogList = myLogService.getMyLogList(myLogVO);
            dataVO.setSuccess(true);
            dataVO.setMessage("마이로그 리스트 조회 성공");
            dataVO.setData(myLogList);
            log.info("마이로그 리스트 조회 성공");
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("마이로그 리스트 조회 실패");
            log.info("마이로그 리스트 조회 실패", e);
        }
        return dataVO;
    }
}
