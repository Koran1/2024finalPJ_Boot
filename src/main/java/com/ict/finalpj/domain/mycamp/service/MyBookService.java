package com.ict.finalpj.domain.mycamp.service;

import java.util.List;
import java.util.Map;

import com.ict.finalpj.domain.mycamp.vo.MyBookVO;

public interface MyBookService {
    // 나의 예약 list
    List<MyBookVO> getMyBookList(MyBookVO myBookVO);
}
