package com.ict.finalpj.domain.mycamp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ict.finalpj.domain.mycamp.vo.MyBookVO;

@Mapper
public interface MyBookMapper {
    // 나의 예약 list
    List<MyBookVO> getMyBookList(MyBookVO myBookVO);
}
