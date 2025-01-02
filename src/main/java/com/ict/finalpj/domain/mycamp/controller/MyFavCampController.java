package com.ict.finalpj.domain.mycamp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.domain.camp.vo.CampVO;
import com.ict.finalpj.domain.mycamp.service.MyFavCampService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RestController
@RequestMapping("/api/mycamp")
public class MyFavCampController {

    @Autowired
    private MyFavCampService myFavCampService;

    @GetMapping("/favCampList")
    public DataVO getFavCampList(@RequestParam("userIdx") String userIdx) {
        DataVO dataVO = new DataVO();
        try {
            // 즐겨찾기 캠핑장 목록 조회
            List<CampVO> favCampList = myFavCampService.getFavCampList(userIdx);

            dataVO.setSuccess(true);
            dataVO.setMessage("즐겨찾기 캠핑장 목록 조회 성공");
            dataVO.setData(favCampList);
            log.info("즐겨찾기 캠핑장 목록 조회 성공");
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("즐겨찾기 캠핑장 목록 조회 실패");
            log.info("즐겨찾기 캠핑장 목록 조회 실패");
        }
        return dataVO;
    }

}
