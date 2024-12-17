package com.ict.finalpj.domain.camp.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ict.finalpj.domain.camp.vo.CampVO;

@Mapper
public interface CampMapper2 {
    // 캠핑장 상세 정보
    CampVO getCampDetail(String campIdx);

}
