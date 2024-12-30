package com.ict.finalpj.domain.mycamp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ict.finalpj.domain.camp.vo.CampVO;

@Mapper
public interface MyFavCampMapper {
    List<CampVO> getFavCampList(String userIdx);
}
