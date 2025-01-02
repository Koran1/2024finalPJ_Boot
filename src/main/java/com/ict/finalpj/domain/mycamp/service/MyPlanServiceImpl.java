package com.ict.finalpj.domain.mycamp.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalpj.domain.mycamp.mapper.MyPlanMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MyPlanServiceImpl implements MyPlanService {

    @Autowired
    private MyPlanMapper myPlanMapper;

    @Override
    public List<Map<String, Object>> getPlanList(String userIdx) {
        return myPlanMapper.getPlanList(userIdx);
    }

    @Override
    public void planListDelete(List<String> planIdxList) {
        myPlanMapper.planListDelete(planIdxList);
    }

}
