package com.ict.finalpj.domain.camp.service;

import java.util.List;

import com.ict.finalpj.domain.camp.vo.CampVO;

public interface CampService {
    // 캠핑장 정보 list
    List<CampVO> getCampingList();

    // 캠핑장 시도 list
    List<String> getDoNmList();

    // 캠핑장 시군구 list
    List<String> getSigunguList(String doNm2);
}
