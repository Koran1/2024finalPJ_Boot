package com.ict.finalpj.domain.add.service;

import java.util.List;
import java.util.Map;

import com.ict.finalpj.domain.add.vo.NoticeVO;

public interface AddService {
    public List<NoticeVO> getNoticeList(Map<String, Object> noticeMap);
    public List<NoticeVO> getNoticeListLv1();
    public int getTotalNoticeCount(Map<String, Object> noticeMap);
    
    List<NoticeVO> getNoticeDetails(String noticeIdx);
    List<NoticeVO> getNoticeLv1Details(String noticeIdx);
}