package com.ict.finalpj.domain.add.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalpj.domain.add.mapper.AddMapper;
import com.ict.finalpj.domain.add.vo.FAQVO;
import com.ict.finalpj.domain.add.vo.NoticeVO;
import com.ict.finalpj.domain.add.vo.QNAVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        NoticeVO noticePrev = addMapper.getNoticePrev(noticeIdx);
        NoticeVO noticeDetail = addMapper.getNoticeDetail(noticeIdx);
        NoticeVO noticeAfter = addMapper.getNoticeAfter(noticeIdx);

        List<NoticeVO> list = new ArrayList<>();
        list.add(noticePrev);
        list.add(noticeDetail);
        list.add(noticeAfter);

        return list;
    }

    @Override
    public List<NoticeVO> getNoticeLv1Details(String noticeIdx) {
        NoticeVO noticePrevLv1 = addMapper.getNoticePrevLv1(noticeIdx);
        NoticeVO noticeDetailLv1 = addMapper.getNoticeDetailLv1(noticeIdx);
        NoticeVO noticeAfterLv1 = addMapper.getNoticeAfterLv1(noticeIdx);

        List<NoticeVO> list = new ArrayList<>();
        list.add(noticePrevLv1);
        list.add(noticeDetailLv1);
        list.add(noticeAfterLv1);

        return list;
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
