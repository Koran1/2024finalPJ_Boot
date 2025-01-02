package com.ict.finalpj.domain.mycamp.service;

import java.util.List;
import java.util.Map;

public interface MyPlanService {
    List<Map<String, Object>> getPlanList(String userIdx);

    void planListDelete(List<String> planIdxList);
}
