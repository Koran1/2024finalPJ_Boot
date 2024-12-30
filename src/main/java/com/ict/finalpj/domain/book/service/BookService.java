package com.ict.finalpj.domain.book.service;

import com.ict.finalpj.domain.book.vo.BookVO;
import com.ict.finalpj.domain.camp.vo.CampVO;

public interface BookService {
    public CampVO goBookPage(String campIdx);
    public int getBookWrite(BookVO bvo);
    public BookVO getBookInfo(String bookIdx);
    public int getBookCancel(String bookIdx);
}