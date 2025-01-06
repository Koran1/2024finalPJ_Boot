package com.ict.finalpj.domain.mycamp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyPlanMapper {
    List<Map<String, Object>> getPlanList(String userIdx);

    void planListDelete(List<String> planIdxList);
}
