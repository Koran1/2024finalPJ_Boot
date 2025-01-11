package com.ict.finalpj.domain.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.common.vo.ReportVO;
import com.ict.finalpj.domain.add.vo.FAQVO;
import com.ict.finalpj.domain.add.vo.NoticeVO;
import com.ict.finalpj.domain.add.vo.QNAVO;
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
@Mapper
public interface AdminMapper {
  List<DealVO> getDealManagement();

  DealVO getDealDetail(String dealIdx);

  List<FileVo> getDealFiles(String dealIdx);

  int getFavoriteCount(String dealIdx);

  List<DealVO> getSellerOtherDeals(String dealIdx);

  List<FileVo> getSellerOtherFiles(String dealIdx);

  String getDealSatisSellerScore(String userIdx);

  int getAdminDealActiveUpdate(String dealIdx, int dealview);

  // 캠핑정보 관리
  List<CampVO> getSearchList(CampSearchVO campSearchVOearchVO);

  // 캠핑 정보 입력
  int insertCamp(CampVO campVO);

  // 캠핑 정보 수정
  int updateCamp(CampVO campData);
  
  // 캠핑정보 관리 페이징
  int getSearchCount(CampSearchVO campSearchVOsearchVO);

  // QNA 관리
  List<QNAVO> getQnaList();

  // QNA 상세 보기
  QNAVO getQnaDetail(String qnaIdx);

    // QNA 업데이트
  int updateQna(QNAVO formData);
  
  // 관리자 회원 정보 리스트
  List<UserListVO> getAdminUserList(UserListVO userListVO);
  int getUserListCount(UserListVO userListVO);

  // 관리자 회원 정보 업데이트
  int getAdminUpdateUser(UserListVO userListVO); 

  // 관리자 공지사항 리스트
  List<NoticeListVO> getAdminNoticeList(NoticeListVO noticeListVO);
  int getNoticeListCount(NoticeListVO noticeListVO);

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
  List<FAQListVO> getAdminFAQList(FAQListVO faqListVO);
  int getFAQListCount(FAQListVO faqListVO);

  // 관리자 FAQ faqStatus 업데이트
  int getUpdateFAQStatus(String faqIdx);

  // 관리자 FAQ 정보 업데이트(모달)
  int getAdminUpdateFAQModal(FAQVO faqvo);

  // 관리자 FAQ 쓰기
  int getAdminFAQListWrite(FAQVO faqvo);

  // 관리자 FAQ 상세
  FAQVO getFAQListIdx(String faqIdx);

  // 관리자 FAQ 수정
  int getAdminFAQListUpdate(FAQVO faqvo);

  
  // 신고 관리
  List<ReportVO> getReportList(ReportVO rvo);

  // 신고 관리 페이징
  int getReportCount(ReportVO rvo);

  // 신고 관리 : 유저 Idx 닉네임 가져오기
  List<UserVO> getUserInfo();

  // 신고 관리 : 신고 처리(승인, 반려)
  // 신고 처리 여부가 반려인 경우(status == 2) 반려한 reportIdx만 reportStatus == 2 처리
  int getReportProcessReturn(ReportVO rvo);
  // 신고 처리 여부가 승인인 경우(status == 1) 해당 신고 테이블 종류 && 신고 테이블 Idx의 reportStatus == 1 (reportStatus == 0인 것만)일괄 처리
  int getReportProcessApprove(ReportVO rvo);
  // 신고 테이블 종류가 1(거래 상품 신고)인 경우
  int getReportProcessDeal(ReportVO rvo);
  // 신고 테이블 종류가 2(캠핑장 상세보기 댓글 신고)인 경우
  // int getReportProcessCampComm(ReportVO rvo);
  // 신고 테이블 종류가 3(후기 글 신고)인 경우
  int getReportProcessLog(ReportVO rvo);
  // 신고 테이블 종류가 4(후기 댓/답글 신고)인 경우
  int getReportProcessLogComm(ReportVO rvo);

  // 판매자의 캠핑장 후기 조회
  List<CampLogListVO> getSellerCampLogs(String sellerIdx);

  // 캠핑로그 관리 : 로그 글 리스트 불러오기 필터, 페이징
  List<CampLogVO> getLogList(CampLogVO clvo);

  // 캠핑로그 관리 : 로그 댓/답 페이징 개수
  int getLogCount(CampLogVO clvo);

  // 캠핑로그 관리 : 로그 댓/답글 리스트 불러오기 필터, 페이징
  List<CampLogCommentVO> getLogCommentList(CampLogCommentVO clvo);

  // 캠핑로그 관리 : 로그 댓/답글 페이징 개수
  int getLogCommentCount(CampLogCommentVO clvo);

  // 캠핑로그 관리 : 로그 글 가리기 업데이트
  int getInActiveLog(String logIdx);

  // 캠핑로그 관리 : 로그 댓/답글 가리기 업데이트
  int getInActiveLogComment(String logCommentIdx);
}
