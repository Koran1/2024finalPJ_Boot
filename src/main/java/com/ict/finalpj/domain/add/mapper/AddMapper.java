package com.ict.finalpj.domain.add.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;


import com.ict.finalpj.domain.add.vo.FAQVO;
import com.ict.finalpj.domain.add.vo.NoticeVO;
import com.ict.finalpj.domain.add.vo.QNAVO;

@Mapper
public interface AddMapper {
    List<NoticeVO> getNoticeList(Map<String, Object> noticeMap);
    List<NoticeVO> getNoticeListLv1();
    int getTotalNoticeCount(Map<String, Object> noticeMap);

    List<NoticeVO> getNoticeDetails(String noticeIdx);
    List<NoticeVO> getNoticeLv1Details(String noticeIdx);

    List<FAQVO> getFaqs();

    List<QNAVO> getQnas(String userIdx);
    QNAVO getQnaDetail(String qnaIdx);
    int writeQna(QNAVO qnaVO);
}
