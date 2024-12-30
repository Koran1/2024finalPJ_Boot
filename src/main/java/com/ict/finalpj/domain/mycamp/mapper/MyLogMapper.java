package com.ict.finalpj.domain.mycamp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ict.finalpj.domain.mycamp.vo.MyLogVO;

@Mapper
public interface MyLogMapper {
    // mylog 리스트
    List<MyLogVO> getMyLogList(MyLogVO myLogVO);
    int getMyLogCount(MyLogVO myLogVO);
}   
