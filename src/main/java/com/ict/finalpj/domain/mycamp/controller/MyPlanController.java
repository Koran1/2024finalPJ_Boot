package com.ict.finalpj.domain.mycamp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.domain.mycamp.service.MyPlanService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/mycamp")
public class MyPlanController {

    @Autowired
    private MyPlanService myPlanService;

    @PostMapping("/plan/list")
    public DataVO getPlanList(@RequestBody String userIdx) {
        DataVO dataVO = new DataVO();
        try {
            // 캠핑 계획 목록 조회
            List<Map<String, Object>> planList = myPlanService.getPlanList(userIdx);
            dataVO.setSuccess(true);
            dataVO.setMessage("계획 목록 조회 성공");
            dataVO.setData(planList);
            log.info("계획 목록 조회 성공");

        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("계획 목록 조회 실패");
            log.info("계획 목록 조회 실패");
        }

        return dataVO;
    }

    @PostMapping("/plan/delete")
    public void planListDelete(@RequestBody List<String> planIdxList) {
        try {
            log.info(planIdxList.toString());
            // 캠핑 계획 목록 삭제
            myPlanService.planListDelete(planIdxList);
            log.info("계획 목록 삭제 성공");

        } catch (Exception e) {
            log.info("계획 목록 삭제 실패", e);
        }
    }

}
