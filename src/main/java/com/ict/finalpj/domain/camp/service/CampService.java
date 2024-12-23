package com.ict.finalpj.domain.camp.service;

import java.util.List;
import java.util.Map;

import com.ict.finalpj.domain.camp.vo.CampSearchVO;

public interface CampService {
    // 캠핑장 정보 list
    // List<CampVO> getCampingList();
    Map<String, Object> getCampingList(CampSearchVO campSearchVO );
    

    // 캠핑장 시도 list
    List<String> getDoNmList();

    // 캠핑장 시군구 list
    List<String> getSigunguList(String doNm2);
}
