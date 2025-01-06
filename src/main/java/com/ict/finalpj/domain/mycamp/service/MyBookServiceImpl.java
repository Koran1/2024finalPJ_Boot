package com.ict.finalpj.domain.mycamp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalpj.domain.mycamp.mapper.MyBookMapper;
import com.ict.finalpj.domain.mycamp.vo.MyBookVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MyBookServiceImpl implements MyBookService {
    @Autowired
    private MyBookMapper myBookMapper;

    @Override
    public List<MyBookVO> getMyBookList(MyBookVO myBookVO) {
        return myBookMapper.getMyBookList(myBookVO);
    }
}
