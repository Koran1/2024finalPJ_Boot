package com.ict.finalpj.domain.mycamp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.domain.mycamp.service.MyBookService;
import com.ict.finalpj.domain.mycamp.vo.MyBookVO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;


@Slf4j
@RestController
@RequestMapping("/api/book")
public class MyBookController {
    @Autowired
    private MyBookService myBookService;

    @GetMapping("/list")
    public DataVO getMyBookList(MyBookVO myBookVO) {
        DataVO dataVO = new DataVO();
        try {
            List<MyBookVO> myBookList = myBookService.getMyBookList(myBookVO);
            dataVO.setSuccess(true);
            dataVO.setMessage("나의 예약 리스트 조회 성공");
            dataVO.setData(myBookList);
            log.info("나의 예약 리스트 조회 성공");
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("나의 예약 리스트 조회 실패");
            log.info("나의 예약 리스트 조회 실패", e);
        }
        return dataVO;
    }
}
