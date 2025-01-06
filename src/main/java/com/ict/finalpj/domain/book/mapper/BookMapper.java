package com.ict.finalpj.domain.book.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ict.finalpj.domain.book.vo.BookVO;
import com.ict.finalpj.domain.camp.vo.CampVO;

@Mapper
public interface BookMapper {
    public CampVO goBookPage(String campIdx);
    public int getBookWrite(BookVO bvo);
    public BookVO getBookInfo(String bookIdx);
    public int getBookCancel(String bookIdx);
}