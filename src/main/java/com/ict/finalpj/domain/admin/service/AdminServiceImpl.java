package com.ict.finalpj.domain.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.common.vo.ReportVO;
import com.ict.finalpj.domain.admin.mapper.AdminMapper;
import com.ict.finalpj.domain.admin.vo.FAQListVO;
import com.ict.finalpj.domain.admin.vo.NoticeListVO;
import com.ict.finalpj.domain.admin.vo.UserListVO;
import com.ict.finalpj.domain.deal.vo.DealVO;
import com.ict.finalpj.domain.user.vo.UserVO;
import com.ict.finalpj.domain.add.vo.FAQVO;
import com.ict.finalpj.domain.add.vo.NoticeVO;
import com.ict.finalpj.domain.add.vo.QNAVO;
import com.ict.finalpj.domain.camp.vo.CampSearchVO;
import com.ict.finalpj.domain.camp.vo.CampVO;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public List<DealVO> getDealManagement() {
        return adminMapper.getDealManagement();
    }

    @Override
    public DealVO getDealDetail(String dealIdx) {
        return adminMapper.getDealDetail(dealIdx);
    }

    @Override
    public List<FileVo> getDealFiles(String dealIdx) {
        return adminMapper.getDealFiles(dealIdx);
    }

    @Override
    public int getFavoriteCount(String dealIdx) {
        return adminMapper.getFavoriteCount(dealIdx);
    }

    @Override
    public Map<String, Object> getSellerOtherDeals(String dealIdx) {
        Map<String, Object> result = new HashMap<>();
        result.put("deals", adminMapper.getSellerOtherDeals(dealIdx));
        result.put("files", adminMapper.getSellerOtherFiles(dealIdx));
        return result;
    }

    @Override
    public String getDealSatisSellerScore(String userIdx) {
        return adminMapper.getDealSatisSellerScore(userIdx);
    }

    @Override
    public int getAdminDealActiveUpdate(String dealIdx, int dealview) {
        return adminMapper.getAdminDealActiveUpdate(dealIdx, dealview);
    }

    // 캠핑정보 관리
    @Override
    public Map<String, Object> getCampingList(CampSearchVO campSearchVO) {
        int offset = (campSearchVO.getPage() - 1) * campSearchVO.getSize();
        campSearchVO.setOffset(offset);

        List<CampVO> campList = adminMapper.getSearchList(campSearchVO);
        int totalCount = adminMapper.getSearchCount(campSearchVO);

        Map<String, Object> result = new HashMap<>();
        result.put("data", campList);
        result.put("totalCount", totalCount);
        result.put("totalPages", (int) Math.ceil((double) totalCount / campSearchVO.getSize()));
        return result;
    }

    // 캠핑 정보 입력
    @Override
    public int insertCamp(CampVO campVO) {
        return adminMapper.insertCamp(campVO);
    }

    // QNA 관리
    @Override
    public List<QNAVO> getQnaList() {
        return adminMapper.getQnaList();
    }

    // 관리자 회원 정보 리스트
    @Override
    public Map<String, Object> getAdminUserList(UserListVO userListVO) {
        int offset = (userListVO.getPage() - 1) * userListVO.getSize();
        userListVO.setOffset(offset);

        List<UserListVO> adminUserList = adminMapper.getAdminUserList(userListVO);
        int totalCount = adminMapper.getUserListCount(userListVO);

        Map<String, Object> result = new HashMap<>();
        result.put("data", adminUserList);
        result.put("totalCount", totalCount);
        result.put("totalPages", (int) Math.ceil((double) totalCount / userListVO.getSize()));
        return result;
    }

    // 관리자 회원 정보 업데이트
    @Override
    public int getAdminUpdateUser(UserListVO userListVO) {
        return adminMapper.getAdminUpdateUser(userListVO);
    }

    // 관리자 공지사항 리스트
    @Override
    public Map<String, Object> getAdminNoticeList(NoticeListVO noticeListVO) {
        int offset = (noticeListVO.getPage() - 1) * noticeListVO.getSize();
        noticeListVO.setOffset(offset);

        List<NoticeListVO> adminNoticeList = adminMapper.getAdminNoticeList(noticeListVO);
        int totalCount = adminMapper.getNoticeListCount(noticeListVO);

        Map<String, Object> result = new HashMap<>();
        result.put("data", adminNoticeList);
        result.put("totalCount", totalCount);
        result.put("totalPages", (int) Math.ceil((double) totalCount / noticeListVO.getSize()));
        return result;
    }

    // 관리자 공지사항 noticeStatus 업데이트
    @Override
    public int getUpdateNoticeStatus(String noticeIdx) {
        return adminMapper.getUpdateNoticeStatus(noticeIdx);
    }

    // 관리자 공지사항 정보 업데이트(모달)
    @Override
    public int getAdminUpdateNoticeModal(NoticeListVO noticeListVO) {
        return adminMapper.getAdminUpdateNoticeModal(noticeListVO);
    }

    // 관리자 공지사항 쓰기
    @Override
    public int getAdminNoticeListWrite(NoticeVO noticeVO) {
        return adminMapper.getAdminNoticeListWrite(noticeVO);
    }
    
    // 관리자 공지사항 상세
    @Override
    public NoticeVO getNoticeListIdx(String noticeIdx) {
        return adminMapper.getNoticeListIdx(noticeIdx);
    }

    // 관리자 공지사항 수정
    @Override
    public int getAdminNoticeListUpdate(NoticeVO noticeVO) {
        return adminMapper.getAdminNoticeListUpdate(noticeVO);
    }

    // 관리자 FAQ 리스트
    public Map<String, Object> getAdminFAQList(FAQListVO faqListVO) {
        int offset = (faqListVO.getPage() - 1) * faqListVO.getSize();
        faqListVO.setOffset(offset);

        List<FAQListVO> adminFAQList = adminMapper.getAdminFAQList(faqListVO);
        int totalCount = adminMapper.getFAQListCount(faqListVO);

        Map<String, Object> result = new HashMap<>();
        result.put("data", adminFAQList);
        result.put("totalCount", totalCount);
        result.put("totalPages", (int) Math.ceil((double) totalCount / faqListVO.getSize()));
        return result;
    }

    // 관리자 FAQ faqStatus 업데이트
    @Override
    public int getUpdateFAQStatus(String faqIdx) {
        return adminMapper.getUpdateFAQStatus(faqIdx);
    }

    // 관리자 FAQ 정보 업데이트(모달)
    @Override
    public int getAdminUpdateFAQModal(FAQVO faqvo) {
        return adminMapper.getAdminUpdateFAQModal(faqvo);
    }

    // 관리자 FAQ 쓰기
    @Override
    public int getAdminFAQListWrite(FAQVO faqvo) {
        return adminMapper.getAdminFAQListWrite(faqvo);
    }

    // 관리자 FAQ 상세
    @Override
    public FAQVO getFAQListIdx(String faqIdx) {
        return adminMapper.getFAQListIdx(faqIdx);
    }
    
    // 관리자 FAQ 수정
    @Override
    public int getAdminFAQListUpdate(FAQVO faqvo) {
        return adminMapper.getAdminFAQListUpdate(faqvo);
    }

    // 신고 관리 : 신고 리스트 불러오기 필터, 페이징
    @Override
    public Map<String, Object> getRerpotList(ReportVO rvo) {
        int offset = (rvo.getPage() - 1) * rvo.getSize();
        rvo.setOffset(offset);

        List<ReportVO> reportList = adminMapper.getReportList(rvo);
        int totalCount = adminMapper.getReportCount(rvo);

        Map<String, Object> result = new HashMap<>();
        result.put("data", reportList);
        result.put("totalCount", totalCount);
        result.put("totalPages", (int) Math.ceil((double) totalCount / rvo.getSize()));
        return result;
    }

    // 신고 관리 : userIdx, userNickname 가져오기(신고한 유저, 신고된 유저 닉네임 출력용)
    @Override
    public List<UserVO> getUserInfo() {
        return adminMapper.getUserInfo();
    }

    // 신고 관리 : 신고 처리(승인, 반려)
    @Override
    public int getReportProcess(ReportVO rvo) {
        int result = 0;
        // 신고 처리 여부가 반려인 경우(status == 2) 반려한 reportIdx만 reportStatus == 2 처리
        if(rvo.getReportStatus().equals("2")){
            result = adminMapper.getReportProcessReturn(rvo);
        }

        // 신고 처리 여부가 승인인 경우(status == 1) 해당 신고 테이블 종류 && 신고 테이블 Idx의 reportStatus == 1 (reportStatus == 0인 것만)일괄 처리
        if(rvo.getReportStatus().equals("1")){
            result = adminMapper.getReportProcessApprove(rvo);
            switch (rvo.getReportTableType()) {
                // 신고 테이블 종류가 1(거래 상품 신고)인 경우
                case "1": result = adminMapper.getReportProcessDeal(rvo); break;
                // 신고 테이블 종류가 2(캠핑장 상세보기 댓글 신고)인 경우
                // case "2": return adminMapper.getReportProcessCampComm(rvo);
                // 신고 테이블 종류가 3(후기 글 신고)인 경우
                case "3": result = adminMapper.getReportProcessLog(rvo); break;
                // 신고 테이블 종류가 4(후기 댓/답글 신고)인 경우
                case "4": result = adminMapper.getReportProcessLogComm(rvo); break;
            }
        }

        return result;
    }

    @Override
    public int updateCamp(CampVO campData) {
        return adminMapper.updateCamp(campData);
    }

    @Override
    public QNAVO getQnaDetail(String qnaIdx) {
        return adminMapper.getQnaDetail(qnaIdx);
    }

    @Override
    public int updateQna(QNAVO formData) {
        return adminMapper.updateQna(formData);
    }

    
}
