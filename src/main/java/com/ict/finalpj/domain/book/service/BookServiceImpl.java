package com.ict.finalpj.domain.book.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalpj.domain.book.mapper.BookMapper;
import com.ict.finalpj.domain.book.vo.BookVO;
import com.ict.finalpj.domain.camp.vo.CampVO;

@Service
public class BookServiceImpl implements BookService{
    @Autowired
    private BookMapper bookMapper;

    @Override
    public CampVO goBookPage(String campIdx) {
        return bookMapper.goBookPage(campIdx);
    }

    @Override
    public int getBookWrite(BookVO bvo) {
        return bookMapper.getBookWrite(bvo);
    }

    @Override
    public BookVO getBookInfo(String bookIdx) {
        return bookMapper.getBookInfo(bookIdx);
    }

    @Override
    public int getBookCancel(String bookIdx) {
        return bookMapper.getBookCancel(bookIdx);
    }
}
