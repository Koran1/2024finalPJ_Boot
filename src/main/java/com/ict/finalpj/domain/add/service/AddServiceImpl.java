package com.ict.finalpj.domain.add.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalpj.domain.add.mapper.AddMapper;
import com.ict.finalpj.domain.add.vo.FAQVO;
import com.ict.finalpj.domain.add.vo.NoticeVO;
import com.ict.finalpj.domain.add.vo.QNAVO;

@Service
public class AddServiceImpl implements AddService {

    @Autowired
    private AddMapper addMapper;

    @Override
    public List<NoticeVO> getNoticeList(Map<String, Object> noticeMap) {
        return addMapper.getNoticeList(noticeMap);
    }

    @Override
    public List<NoticeVO> getNoticeListLv1() {
        return addMapper.getNoticeListLv1();
    }

    @Override
    public int getTotalNoticeCount(Map<String, Object> noticeMap) {
        return addMapper.getTotalNoticeCount(noticeMap);
    }

    @Override
    public List<NoticeVO> getNoticeDetails(String noticeIdx) {
        return addMapper.getNoticeDetails(noticeIdx);
    }

    @Override
    public List<NoticeVO> getNoticeLv1Details(String noticeIdx) {
        return addMapper.getNoticeLv1Details(noticeIdx);
    }

    @Override
    public List<FAQVO> getFaqs() {
        return addMapper.getFaqs();
    }

    @Override
    public List<QNAVO> getQnas(String userIdx) {
        return addMapper.getQnas(userIdx);
    }

    @Override
    public QNAVO getQnaDetail(String qnaIdx) {
        return addMapper.getQnaDetail(qnaIdx);
    }

    @Override
    public int writeQna(QNAVO qnaVO) {
        return addMapper.writeQna(qnaVO);
    }

}
