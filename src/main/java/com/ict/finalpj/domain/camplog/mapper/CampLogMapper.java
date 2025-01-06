package com.ict.finalpj.domain.camplog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ict.finalpj.domain.camplog.vo.CampLogListVO;

@Mapper
public interface CampLogMapper {
    // 캠핑로그 list
    List<CampLogListVO> getCamplogList(CampLogListVO campLogListVO);
    int getCampLogCount(CampLogListVO campLogListVO);
}
