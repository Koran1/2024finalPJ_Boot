package com.ict.finalpj.domain.admin.service;

import java.util.List;
import java.util.Map;

import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.common.vo.ReportVO;
import com.ict.finalpj.domain.add.vo.FAQVO;
import com.ict.finalpj.domain.add.vo.NoticeVO;
import com.ict.finalpj.domain.add.vo.QNAVO;
import com.ict.finalpj.domain.admin.vo.AdminVO;
import com.ict.finalpj.domain.admin.vo.FAQListVO;
import com.ict.finalpj.domain.admin.vo.NoticeListVO;
import com.ict.finalpj.domain.admin.vo.UserListVO;
import com.ict.finalpj.domain.camp.vo.CampSearchVO;
import com.ict.finalpj.domain.camp.vo.CampVO;
import com.ict.finalpj.domain.camplog.vo.CampLogCommentVO;
import com.ict.finalpj.domain.camplog.vo.CampLogListVO;
import com.ict.finalpj.domain.camplog.vo.CampLogVO;
import com.ict.finalpj.domain.deal.vo.DealVO;
import com.ict.finalpj.domain.user.vo.UserVO;

public interface AdminService {
  AdminVO getAdminLogin(AdminVO avo);
  
  List<DealVO> getDealManagement();

  DealVO getDealDetail(String dealIdx);

  List<FileVo> getDealFiles(String dealIdx);

  int getFavoriteCount(String dealIdx);

  Map<String, Object> getSellerOtherDeals(String dealIdx);

  String getDealSatisSellerScore(String userIdx);

  int getAdminDealActiveUpdate(String dealIdx, int dealview);

  // 캠핑정보관리
  Map<String, Object> getCampingList(CampSearchVO campSearchVO);

  // 캠핑 정보 입력
  int insertCamp(CampVO campVO);

  // 캠핑 정보 수정
  int updateCamp(CampVO campData);

  // QNA 관리
  List<QNAVO> getQnaList();

  // QNA 상세 보기
  QNAVO getQnaDetail(String qnaIdx);

  // QNA 업데이트
  int updateQna(QNAVO formData);
  
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

  // 신고 관리 : 신고 리스트 불러오기 필터, 페이징
  Map<String, Object> getRerpotList(ReportVO rvo);
  
  // 신고 관리 : 유저 Idx 닉네임 가져오기
  List<UserVO> getUserInfo();

  // 신고 관리 : 신고 처리(승인, 반려)
  int getReportProcess(ReportVO rvo);

    // 판매자의 캠핑장 후기 조회
  List<CampLogListVO> getSellerCampLogs(String sellerIdx);

  // 캠핑로그 관리 : 로그 글 리스트 불러오기 필터, 페이징
  Map<String, Object> getLogList(CampLogVO clvo);

  // 캠핑로그 관리 : 로그 댓/답글 리스트 불러오기 필터, 페이징
  Map<String, Object> getLogCommentList(CampLogCommentVO lcvo);

  // 캠핑로그 관리 : 로그 글 가리기 업데이트
  int getInActiveLog(String logIdx);

  // 캠핑로그 관리 : 로그 댓/답글 가리기 업데이트
  int getInActiveLogComment(String logCommentIdx);
}
