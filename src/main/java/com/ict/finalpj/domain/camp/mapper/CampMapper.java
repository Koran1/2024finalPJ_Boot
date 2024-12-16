package com.ict.finalpj.domain.camp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ict.finalpj.domain.camp.vo.CampVO;

@Mapper
public interface CampMapper {
    // 캠핑장 정보 list
    List<CampVO> getCampingList();

    // 캠핑장 시도 list
    List<String> getDoNmList();

    // 캠핑장 시군구 list
    List<String> getSigunguList(String doNm2);
}
