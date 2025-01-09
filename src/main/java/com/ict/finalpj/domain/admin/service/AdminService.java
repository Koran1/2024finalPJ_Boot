package com.ict.finalpj.domain.admin.service;

import java.util.List;
import java.util.Map;

import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.domain.deal.vo.DealVO;
import com.ict.finalpj.domain.add.vo.FAQVO;
import com.ict.finalpj.domain.add.vo.NoticeVO;
import com.ict.finalpj.domain.add.vo.QNAVO;
import com.ict.finalpj.domain.admin.vo.FAQListVO;
import com.ict.finalpj.domain.admin.vo.NoticeListVO;
import com.ict.finalpj.domain.admin.vo.UserListVO;
import com.ict.finalpj.domain.camp.vo.CampSearchVO;

public interface AdminService {
  List<DealVO> getDealManagement();

  DealVO getDealDetail(String dealIdx);

  List<FileVo> getDealFiles(String dealIdx);

  int getFavoriteCount(String dealIdx);

  Map<String, Object> getSellerOtherDeals(String dealIdx);

  String getDealSatisSellerScore(String userIdx);

  int getAdminDealActiveUpdate(String dealIdx, int dealview);

  // 캠핑정보관리
  Map<String, Object> getCampingList(CampSearchVO campSearchVO);

  // QNA 관리
  List<QNAVO> getQnaList();

  // 관리자 회원 정보 리스트
    Map<String, Object> getAdminUserList(UserListVO userListVO);

    // 관리자 회원 정보 업데이트
    int getAdminUpdateUser(UserListVO userListVO); 

    // 관리자 공지사항 리스트
    Map<String, Object> getAdminNoticeList(NoticeListVO noticeListVO);

    // 관리자 공지사항 noticeStatus 업데이트
    int getUpdateNoticeStatus(String noticeIdx);

    // 관리자 공지사항 정보 업데이트(모달)
    int getAdminUpdateNoticeModal(NoticeListVO noticeListVO);

    // 관리자 공지사항 쓰기
    int getAdminNoticeListWrite(NoticeVO noticeVO);

    // 관리자 공지사항 상세
    NoticeVO getNoticeListIdx(String noticeIdx);

    // 관리자 공지사항 수정
    int getAdminNoticeListUpdate(NoticeVO noticeVO);

    // 관리자 FAQ 리스트
    Map<String, Object> getAdminFAQList(FAQListVO faqListVO);

    // 관리자 FAQ faqStatus 업데이트
    int getUpdateFAQStatus(String faqIdx);

    // 관리자 FAQ 정보 업데이트(모달)
    int getAdminUpdateFAQModal(FAQVO faqvo);

    // 관리자 FAQ 쓰기
    int getAdminFAQListWrite(FAQVO faqvo);

    // 관리자 FAQ 상세
    FAQVO getFAQListIdx(String faqIdx);

    // 관리자 FAQ 업데이트
    int getAdminFAQListUpdate(FAQVO faqvo);
}
