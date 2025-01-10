package com.ict.finalpj.domain.admin.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.common.vo.ReportVO;
import com.ict.finalpj.domain.add.vo.FAQVO;
import com.ict.finalpj.domain.add.vo.NoticeVO;
import com.ict.finalpj.domain.add.vo.QNAVO;
import com.ict.finalpj.domain.admin.service.AdminService;
import com.ict.finalpj.domain.admin.vo.FAQListVO;
import com.ict.finalpj.domain.admin.vo.NoticeListVO;
import com.ict.finalpj.domain.admin.vo.UserListVO;
import com.ict.finalpj.domain.camp.vo.CampSearchVO;
import com.ict.finalpj.domain.camp.vo.CampVO;
import com.ict.finalpj.domain.camplog.vo.CampLogListVO;
import com.ict.finalpj.domain.deal.vo.DealVO;
import com.ict.finalpj.domain.user.vo.UserVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/dealList")
    public DataVO getDealManagement() {
        DataVO dataVO = new DataVO();
        try {
            List<DealVO> list = adminService.getDealManagement();
            log.info("거래 목록 조회 성공: {}", list);
            log.info("이미지 데이터 확인: {}", list.stream().map(DealVO::getDeal01).toList());
            dataVO.setSuccess(true);
            dataVO.setMessage("캠핑거래관리 목록 조회 성공");
            dataVO.setData(list);
        } catch (Exception e) {
            log.error("거래 목록 조회 실패", e);
            dataVO.setSuccess(false);
            dataVO.setMessage("캠핑거래관리 목록 조회 실패");
            dataVO.setData(null);
        }
        return dataVO;
    }

    @GetMapping("/dealList/dealDetail/{dealIdx}")
    public DataVO getDealDetail(@PathVariable String dealIdx) {
        DataVO dataVO = new DataVO();
        try {
            DealVO detail = adminService.getDealDetail(dealIdx);
            if (detail == null) {
                dataVO.setSuccess(false);
                dataVO.setMessage("상품을 찾을 수 없습니다");
                return dataVO;
            }
            List<FileVo> files = adminService.getDealFiles(dealIdx);
            detail.setFileList(files);
            dataVO.setSuccess(true);
            dataVO.setMessage("상세 정보 조회 성공");
            dataVO.setData(detail);
        } catch (Exception e) {
            log.error("상세 정보 조회 실패", e);
            dataVO.setSuccess(false);
            dataVO.setMessage("상세 정보 조회 실패");
        }
        return dataVO;
    }

    @GetMapping("/dealList/dealDetail/favorite-count/{dealIdx}")
    public DataVO getFavoriteCount(@PathVariable String dealIdx) {
        DataVO dataVO = new DataVO();
        try {
            int count = adminService.getFavoriteCount(dealIdx);
            dataVO.setSuccess(true);
            dataVO.setMessage("찜하기 개수 조회 성공");
            dataVO.setData(count);
        } catch (Exception e) {
            log.error("찜하기 개수 조회 실패", e);
            dataVO.setSuccess(false);
            dataVO.setMessage("찜하기 개수 조회 실패");
        }
        return dataVO;
    }

    @GetMapping("/dealList/dealDetail/seller-other-deals/{dealIdx}")
    public DataVO getSellerOtherDeals(@PathVariable String dealIdx) {
        DataVO dataVO = new DataVO();
        try {
            Map<String, Object> result = adminService.getSellerOtherDeals(dealIdx);
            dataVO.setSuccess(true);
            dataVO.setMessage("판매자의 다른 상품 조회 성공");
            dataVO.setData(result);
        } catch (Exception e) {
            log.error("판매자의 다른 상품 조회 실패", e);
            dataVO.setSuccess(false);
            dataVO.setMessage("판매자의 다른 상품 조회 실패");
        }
        return dataVO;
    }

    @GetMapping("/dealList/dealDetail/seller-score/{userIdx}")
    public DataVO getSellerScore(@PathVariable String userIdx) {
        DataVO dataVO = new DataVO();
        try {
            String score = adminService.getDealSatisSellerScore(userIdx);
            dataVO.setSuccess(true);
            dataVO.setMessage("판매자 평점 조회 성공");
            dataVO.setData(score);
        } catch (Exception e) {
            log.error("판매자 평점 조회 실패", e);
            dataVO.setSuccess(false);
            dataVO.setMessage("판매자 평점 조회 실패");
        }
        return dataVO;
    }

    @PutMapping("/dealList/{dealIdx}")
    public DataVO updateDealView(@PathVariable("dealIdx") String dealIdx, @RequestBody Map<String, Integer> payload) {
        DataVO dataVO = new DataVO();
        try {
            int dealview = payload.get("dealview");
            int result = adminService.getAdminDealActiveUpdate(dealIdx, dealview);

            if (result > 0) {
                dataVO.setSuccess(true);
                dataVO.setMessage("상품 상태가 변경되었습니다.");
            } else {
                dataVO.setSuccess(false);
                dataVO.setMessage("상품 상태 변경에 실패했습니다.");
            }
        } catch (Exception e) {
            log.error("상품 상태 변경 실패", e);
            dataVO.setSuccess(false);
            dataVO.setMessage("상품 상태 변경에 실패했습니다.");
        }
        return dataVO;
    }

    // 캠핑정보관리
    @PostMapping("/campList")
    public DataVO getCampingList(@RequestBody CampSearchVO campSearchVO) {
        log.info("Sort Option: {}", campSearchVO);
        DataVO dataVO = new DataVO();
        try {
            Map<String, Object> campingList = adminService.getCampingList(campSearchVO);
            dataVO.setSuccess(true);
            dataVO.setMessage("캠핑장 리스트 조회 성공");
            dataVO.setData(campingList);
            log.info("캠핑장 리스트 조회 성공");

        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("캠핑장 리스트 조회 실패");
            log.info("캠핑장 리스트 조회 실패", e);
        }
        return dataVO;
    }

    // 캠핑 신규 등록
    @PostMapping("/insertCamp")
    public DataVO insertCamp(@RequestBody CampVO campData) {
        log.info("CampVO" + campData);
        DataVO dataVO = new DataVO();
        try {
            int result = adminService.insertCamp(campData);
            
            dataVO.setSuccess(true);
            dataVO.setMessage("캠핑장 정보 삽입 성공");
            log.info("캠핑장 리스트 조회 성공");

        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("캠핑장 정보 삽입 실패");
            log.info("캠핑장 리스트 조회 실패", e);
        }
        return dataVO;
    }

    // 캠핑 정보 업데이트
    @PutMapping("/updateCamp")
    public DataVO updateCamp(@RequestBody CampVO campData) {
        log.info("CampVO" + campData);
        DataVO dataVO = new DataVO();
        try {
            adminService.updateCamp(campData);
            dataVO.setSuccess(true);
            dataVO.setMessage("캠핑장 정보 수정 성공");
            log.info("캠핑장 리스트 조회 성공");

        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("캠핑장 정보 수정 실패");
            log.info("캠핑장 리스트 조회 실패", e);
        }
        return dataVO;
    }

    // QnA 관리
    @GetMapping("/qnaList")
    public DataVO getQnaList() {
        DataVO dataVO = new DataVO();
        try {
            List<QNAVO> qnaList = adminService.getQnaList();
            dataVO.setSuccess(true);
            dataVO.setMessage("QNA 리스트 조회 성공");
            dataVO.setData(qnaList);
            log.info("QNA 리스트 조회 성공");
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("QNA 리스트 조회 실패");
            log.info("QNA 리스트 조회 실패", e);
        }

        return dataVO;
    }
    
    // QNA 상세보기
    @GetMapping("/getQnaDetail")
    public DataVO getQnaDetail(
        @RequestParam("qnaIdx") String qnaIdx
    ) {
        DataVO dataVO = new DataVO();
        try {
            QNAVO qna = adminService.getQnaDetail(qnaIdx);

            dataVO.setSuccess(true);
            dataVO.setMessage("QNA 리스트 조회 성공");
            dataVO.setData(qna);
            log.info("QNA 리스트 조회 성공");
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("QNA 리스트 조회 실패");
            log.info("QNA 리스트 조회 실패", e);
        }

        return dataVO;
    }

    // QNA 업데이트
    @PutMapping("/updateQna")
    public DataVO updateQna(@RequestBody QNAVO formData) {
        log.info("CampVO" + formData);
        DataVO dataVO = new DataVO();
        try {
            int result = adminService.updateQna(formData);
            dataVO.setSuccess(true);
            dataVO.setMessage("QNA 완료");
            log.info("캠핑장 리스트 조회 성공");

        } catch (Exception e) {
            dataVO.setSuccess(false);
           dataVO.setMessage("QNA 실패");
            log.info("캠핑장 리스트 조회 실패", e);
        }
        return dataVO;
    }
    
    // 회원정보 리스트
    @GetMapping("/userList")
    public DataVO getAdminUserList(UserListVO userListVO) {
        DataVO dataVO = new DataVO();
        try {
            Map<String, Object> adminUserList = adminService.getAdminUserList(userListVO);
            dataVO.setSuccess(true);
            dataVO.setMessage("관리자 사용자 리스트 조회 성공");
            dataVO.setData(adminUserList);
            log.info("관리자 사용자 리스트 조회 성공");
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("관리자 사용자 리스트 조회 실패");
            log.info("관리자 사용자 리스트 조회 실패", e);
        }
        return dataVO;
    }

    // 회원 정보 업데이트(userLevel)
    @PutMapping("/getAdminUpdateUser")
    public DataVO getAdminUpdateUser(@RequestBody UserListVO userListVO) {
        // String userIdx = requestData.get("userIdx");
        // String userLevel = requestData.get("userLevel");

        DataVO dataVO = new DataVO();
        try {
            int result = adminService.getAdminUpdateUser(userListVO);
            if (result == 0) {
                dataVO.setSuccess(false);
                dataVO.setMessage("회원 정보 업데이트 실패");
            } else {
                dataVO.setSuccess(true);
                dataVO.setMessage("회원 정보 업데이트 성공");
            }
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("회원 정보 업데이트 중 오류 발생");
            e.printStackTrace();
        }
        return dataVO;
    }

    // 공지사항 리스트
    @GetMapping("/noticeList")
    public DataVO getAdminNoticeList(NoticeListVO noticeListVO) {
        DataVO dataVO = new DataVO();
        try {
            Map<String, Object> adminNoticeList = adminService.getAdminNoticeList(noticeListVO);
            dataVO.setSuccess(true);
            dataVO.setMessage("관리자 공지사항 리스트 조회 성공");
            dataVO.setData(adminNoticeList);
            log.info("관리자 공지사항 리스트 조회 성공");
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("관리자 공지사항 리스트 조회 실패");
            log.info("관리자 공지사항 리스트 조회 실패", e);
        }
        return dataVO;
    }

    // 관리자 공지사항 noticeStatus 업데이트
    @PutMapping("/updateNoticeStatus")
    public DataVO getUpdateNoticeStatus(@RequestParam("selectedNotices") String[] selectedNotices) {
        DataVO dataVO = new DataVO();
        try {

            for (String k : selectedNotices) {
                adminService.getUpdateNoticeStatus(k);
            }
            dataVO.setSuccess(true);
            dataVO.setMessage("공지사항 게시글 보이기 수정 성공");
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("공지사항 게시글 보이기 수정 실패");
            e.printStackTrace();
        }
        return dataVO;
    }

    // 공지사항 정보 업데이트(모달)
    @PutMapping("/getAdminUpdateNoticeModal")
    public DataVO getAdminUpdateNoticeModal(@RequestBody NoticeListVO noticeListVO) {
        DataVO dataVO = new DataVO();
        try {
            int result = adminService.getAdminUpdateNoticeModal(noticeListVO);
            if (result == 0) {
                dataVO.setSuccess(false);
                dataVO.setMessage("공지사항 정보 업데이트 실패");
            } else {
                dataVO.setSuccess(true);
                dataVO.setMessage("공지사항 정보 업데이트 성공");
            }
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("공지사항 정보 업데이트 중 오류 발생");
            e.printStackTrace();
        }
        return dataVO;
    }

    // 공지사항 작성
    @PostMapping("/noticeList/write")
    public DataVO getAdminNoticeListWrite(@ModelAttribute("data") NoticeVO noticeVO) {
        DataVO dataVO = new DataVO();
        try {
            MultipartFile noticeMultipartFile = noticeVO.getNoticeMultipartFile();

            if (noticeMultipartFile != null && !noticeMultipartFile.isEmpty()) {
                // 파일이 있는 경우
                UUID uuid = UUID.randomUUID();
                String noticeM_file = uuid.toString() + "_" + noticeMultipartFile.getOriginalFilename();
                noticeVO.setNoticeFile(noticeM_file);

                String path = new File("D:\\upload\\notice").getAbsolutePath();
                noticeMultipartFile.transferTo(new File(path, noticeM_file));
            } else {
                // 파일이 없는 경우
                noticeVO.setNoticeFile("");
            }

            int result = adminService.getAdminNoticeListWrite(noticeVO);
            if (result == 0) {
                dataVO.setSuccess(false);
                dataVO.setMessage("공지사항 쓰기 실패");
                return dataVO;
            }
            dataVO.setSuccess(true);
            dataVO.setMessage("공지사항 쓰기 성공");
            log.info("공지사항 쓰기 성공");
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("공지사항 쓰기 실패");
            e.printStackTrace();
        }
        return dataVO;
    }

    // 공지사항 상세
    @GetMapping("/noticeList/detail/{noticeIdx}")
    public DataVO getNoticeListDetail(@PathVariable("noticeIdx") String noticeIdx) {
        DataVO dataVO = new DataVO();
        try {
            NoticeVO noticeVO = adminService.getNoticeListIdx(noticeIdx);
            if (noticeVO == null) {
                dataVO.setSuccess(false);
                dataVO.setMessage("공지사항 상세보기 실패");
                return dataVO;
            }
            dataVO.setSuccess(true);
            dataVO.setMessage("공지사항 상세보기 성공");
            dataVO.setData(noticeVO);
            log.info("공지사항 상세보기 성공");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return dataVO;
    }

    // 공지사항 수정
    @PutMapping("/noticeList/update/{noticeIdx}")
    public DataVO getAdminNoticeListUpdate(
            @PathVariable("noticeIdx") String noticeIdx,
            @ModelAttribute NoticeVO noticeVO,
            @RequestParam(value = "noticeMultipartFile", required = false) MultipartFile noticeMultipartFile) {
        DataVO dataVO = new DataVO();
        try {
            noticeVO.setNoticeIdx(noticeIdx);

            // 파일 처리
            if (noticeMultipartFile != null && !noticeMultipartFile.isEmpty()) {
                UUID uuid = UUID.randomUUID();
                String noticeM_file = uuid.toString() + "_" + noticeMultipartFile.getOriginalFilename();
                noticeVO.setNoticeFile(noticeM_file);

                String path = new File("D:\\upload\\notice").getAbsolutePath();
                File saveFile = new File(path, noticeM_file);
                noticeMultipartFile.transferTo(saveFile);
            } else {
                if (noticeVO.getNoticeFile() == null || noticeVO.getNoticeFile().isEmpty()) {
                    noticeVO.setNoticeFile("");
                }
            }

            int result = adminService.getAdminNoticeListUpdate(noticeVO);
            if (result == 0) {
                dataVO.setSuccess(false);
                dataVO.setMessage("공지사항 수정 실패");
                return dataVO;
            }
            dataVO.setSuccess(true);
            dataVO.setMessage("공지사항 수정 성공");
            log.info("공지사항 수정 성공");
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("공지사항 수정 실패");
            e.printStackTrace();
        }
        return dataVO;
    }

    // FAQ 리스트
    @GetMapping("/faqList")
    public DataVO getAdminFAQList(FAQListVO faqListVO) {
        DataVO dataVO = new DataVO();
        try {
            Map<String, Object> adminFAQList = adminService.getAdminFAQList(faqListVO);
            dataVO.setSuccess(true);
            dataVO.setMessage("관리자 FAQ 리스트 조회 성공");
            dataVO.setData(adminFAQList);
            log.info("관리자 FAQ 리스트 조회 성공");
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("관리자 FAQ 리스트 조회 실패");
            log.info("관리자 FAQ 리스트 조회 실패", e);
        }
        return dataVO;
    }

    // 관리자 FAQ faqIdx 업데이트
    @PutMapping("/updateFAQStatus")
    public DataVO getUpdateFAQStatus(@RequestParam("selectedFAQs") String[] selectedFAQs) {
        DataVO dataVO = new DataVO();
        try {

            for (String k : selectedFAQs) {
                adminService.getUpdateFAQStatus(k);
            }
            dataVO.setSuccess(true);
            dataVO.setMessage("FAQ 게시글 보이기 수정 성공");
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("FAQ 게시글 보이기 수정 실패");
            e.printStackTrace();
        }
        return dataVO;
    }

    // FAQ 정보 업데이트(모달)
    @PutMapping("/getAdminUpdateFAQModal")
    public DataVO getAdminUpdateFAQModal(@RequestBody FAQVO faqvo) {
        DataVO dataVO = new DataVO();
        try {
            int result = adminService.getAdminUpdateFAQModal(faqvo);
            if (result == 0) {
                dataVO.setSuccess(false);
                dataVO.setMessage("FAQ 정보 업데이트 실패");
                log.info("FAQ 정보 업데이트 실패");
                log.info("FAQ 정보 업데이트 실패: 업데이트된 행이 없습니다. FAQVO: {}", faqvo);
            } else {
                dataVO.setSuccess(true);
                dataVO.setMessage("FAQ 정보 업데이트 성공");
            }
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("FAQ 정보 업데이트 중 오류 발생");
            e.printStackTrace();
        }
        return dataVO;
    }

    // FAQ 작성
    @PostMapping("/faqList/write")
    public DataVO getAdminFAQListWrite(@ModelAttribute("data") FAQVO faqvo) {
        DataVO dataVO = new DataVO();
        try {
            int result = adminService.getAdminFAQListWrite(faqvo);
            if (result == 0) {
                dataVO.setSuccess(false);
                dataVO.setMessage("FAQ 쓰기 실패");
                return dataVO;
            }
            dataVO.setSuccess(true);
            dataVO.setMessage("FAQ 쓰기 성공");
            log.info("FAQ 쓰기 성공");
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("FAQ 쓰기 실패");
            e.printStackTrace();
        }
        return dataVO;
    }

    // FAQ 상세
    @GetMapping("/faqList/detail/{faqIdx}")
    public DataVO getFAQListDetail(@PathVariable("faqIdx") String faqIdx) {
        DataVO dataVO = new DataVO();
        try {
            FAQVO faqvo = adminService.getFAQListIdx(faqIdx);
            if (faqvo == null) {
                dataVO.setSuccess(false);
                dataVO.setMessage("FAQ 상세보기 실패");
                return dataVO;
            }
            dataVO.setSuccess(true);
            dataVO.setMessage("FAQ 상세보기 성공");
            dataVO.setData(faqvo);
            log.info("FAQ 상세보기 성공");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return dataVO;
    }

    // FAQ 수정
    @PutMapping("/faqList/update/{faqIdx}")
    public DataVO getAdminFAQListUpdate(
            @PathVariable("faqIdx") String faqIdx,
            @RequestBody FAQVO faqvo) {
        DataVO dataVO = new DataVO();
        try {
            log.info("FAQVO 데이터: {}", faqvo); // FAQVO 값 확인
            int result = adminService.getAdminFAQListUpdate(faqvo);
            if (result == 0) {
                dataVO.setSuccess(false);
                dataVO.setMessage("FAQ 정보 업데이트 실패");
                log.info("FAQ 정보 업데이트 실패");
            } else {
                dataVO.setSuccess(true);
                dataVO.setMessage("FAQ 정보 업데이트 성공");
            }
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("FAQ 정보 업데이트 중 오류 발생");
            e.printStackTrace();
        }
        return dataVO;
    }

    // 신고 관리 : 신고 리스트 불러오기 필터, 페이징
    @PostMapping("/getReportList")
    public DataVO getReportList(@RequestBody ReportVO rvo) {
        log.info("Sort Option: {}", rvo);
        DataVO dataVO = new DataVO();
        try {
            Map<String, Object> reportList = adminService.getRerpotList(rvo);
            
            // log.info("rvo : " + rvo);

            List<UserVO> uvo = adminService.getUserInfo();
            // userIdx와 userNickname을 매핑
            Map<String, String> userNicknameMap = uvo.stream()
                .collect(Collectors.toMap(UserVO::getUserIdx, UserVO::getUserNickname));

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("reportList", reportList);
            resultMap.put("userNicknameMap", userNicknameMap);
            log.info("resultMap : " + resultMap);
            dataVO.setData(resultMap);
            dataVO.setSuccess(true);
            dataVO.setMessage("신고 리스트 조회 성공");
            log.info("신고 리스트 조회 성공");

        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("신고 리스트 조회 오류");
            log.info("신고 리스트 조회 오류", e);
        }
        return dataVO;
    }

    @PostMapping("/reportProcess")
    public DataVO getReportProcess(@RequestBody ReportVO rvo) {
        DataVO dataVO = new DataVO();
        try {
            log.info("rvo1 : " + rvo);
            
            int result = adminService.getReportProcess(rvo);
            log.info("rvo2 : " + rvo);
            
            if(result > 0){
                log.info("rvo3 : " + rvo);
                dataVO.setSuccess(true);
                dataVO.setMessage("신고 처리 성공");
                log.info("신고 처리 성공");
            } else{
                dataVO.setSuccess(false);
                dataVO.setMessage("신고 처리 실패");
                log.info("신고 처리 실패");
            }
        } catch (Exception e) {
            e.printStackTrace();
            dataVO.setSuccess(false);
            dataVO.setMessage("신고 처리 오류");
            log.info("신고 처리 오류", e);
        }

        return dataVO;
    }
    
    @GetMapping("/dealList/dealDetail/seller-camplogs/{sellerIdx}")
    public DataVO getSellerCampLogs(@PathVariable String sellerIdx) {
        DataVO dataVO = new DataVO();
        try {
            List<CampLogListVO> campLogs = adminService.getSellerCampLogs(sellerIdx);
            dataVO.setSuccess(true);
            dataVO.setMessage("판매자의 캠핑장 후기 조회 성공");
            dataVO.setData(campLogs);
        } catch (Exception e) {
            log.error("판매자의 캠핑장 후기 조회 실패", e);
            dataVO.setSuccess(false);
            dataVO.setMessage("판매자의 캠핑장 후기 조회 실패");
        }
        return dataVO;
    }
}
