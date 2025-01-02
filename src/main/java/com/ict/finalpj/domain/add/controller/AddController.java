package com.ict.finalpj.domain.add.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.domain.add.service.AddService;
import com.ict.finalpj.domain.add.vo.FAQVO;
import com.ict.finalpj.domain.add.vo.NoticeVO;
import com.ict.finalpj.domain.add.vo.QNAVO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@RestController
@RequestMapping("/api/add")
public class AddController {
    
    @Autowired
    private AddService addService;

    @GetMapping("/notice/getNotice")
    public DataVO getNoticeList(@RequestParam String currentPage,
    @RequestParam String searchKeyword ) {
        DataVO dvo = new DataVO();
        try {
            log.info("cPage : " + currentPage);
            log.info("searchKeyword : " + searchKeyword);

            int itemPerPage = 10;

            Map<String, Object> noticeMap = new HashMap<>();
            noticeMap.put("offset", (Integer.parseInt(currentPage)-1)*itemPerPage+1);
            noticeMap.put("limit", itemPerPage);
            noticeMap.put("searchKeyword", searchKeyword);
            
            int totalNoticeCount = addService.getTotalNoticeCount(noticeMap);

            List<NoticeVO> noticeList = addService.getNoticeList(noticeMap);
            
            List<NoticeVO> noticeListLv1 = addService.getNoticeListLv1();

            Map<String, Object> resultNotice = new HashMap<>();
            resultNotice.put("totalNoticeCount", totalNoticeCount%itemPerPage == 0 ? 
            totalNoticeCount/itemPerPage : totalNoticeCount/itemPerPage+1);
            resultNotice.put("noticeList", noticeList);
            resultNotice.put("noticeListLv1", noticeListLv1);

            dvo.setData(resultNotice);
            dvo.setSuccess(true);
            dvo.setMessage("Notice 조회 성공");
        } catch (Exception e) {
            dvo.setSuccess(false);
            dvo.setMessage("Notice 조회 실패");
            e.printStackTrace();
        }
        return dvo;
    }

    @GetMapping("/notice/getNoticeDetail")
    public DataVO getNoticeListDetail(@RequestParam String noticeIdx) {
        DataVO dvo = new DataVO();
        try {
            log.info("noticeIdx : " + noticeIdx);

            List<NoticeVO> resultNoticeDetails = addService.getNoticeDetails(noticeIdx);
            
            log.info("result : " + resultNoticeDetails);

            dvo.setData(resultNoticeDetails);
            dvo.setSuccess(true);
            dvo.setMessage("Notice 조회 성공");
        } catch (Exception e) {
            dvo.setSuccess(false);
            dvo.setMessage("Notice 조회 실패");
            e.printStackTrace();
        }
        return dvo;
    }
    

    @GetMapping("/faq")
    public DataVO getFaqs() {
        DataVO dvo = new DataVO();
        try {
            List<FAQVO> faqList = addService.getFaqs();
            dvo.setData(faqList);
            dvo.setSuccess(true);
            dvo.setMessage("FAQ 조회 성공");
        } catch (Exception e) {
            dvo.setSuccess(false);
            dvo.setMessage("FAQ 조회 실패");
            e.printStackTrace();
        }
        return dvo;
    }

    @GetMapping("/getQnas")
    public DataVO getQnas(@RequestParam("userIdx") String userIdx) {
        DataVO dvo = new DataVO();
        try {
            List<QNAVO> qnaList = addService.getQnas(userIdx);
            dvo.setData(qnaList);
            dvo.setSuccess(true);
            dvo.setMessage("QNA 조회 성공");
        } catch (Exception e) {
            dvo.setSuccess(false);
            dvo.setMessage("QNA 조회 실패");
            e.printStackTrace();
        }
        return dvo;
    }

    @GetMapping("/qna/getQnaDetail")
    public DataVO getQnaDetail(@RequestParam("qnaIdx") String qnaIdx) {
        DataVO dvo = new DataVO();
        try {
            QNAVO qnaDetail = addService.getQnaDetail(qnaIdx);
            if(qnaDetail == null) {
                dvo.setSuccess(false);
                dvo.setMessage("QNA Detail 조회 실패");
                return dvo;
            }
            dvo.setData(qnaDetail);
            dvo.setSuccess(true);
            dvo.setMessage("QNA Detail 조회 성공");
        } catch (Exception e) {
            dvo.setSuccess(false);
            dvo.setMessage("QNA Detail 조회 실패");
            e.printStackTrace();
        }
        return dvo;
    }

    @PostMapping("/writeQna")
    public DataVO writeQna(
        @RequestParam String userIdx,
        @RequestParam String qnaSubject,
        @RequestParam String qnaContent,
        @RequestParam(value = "qnaMultipartFile", required = false) MultipartFile qnaMultipartFile
        ) {
        DataVO dvo = new DataVO();
        try {
            QNAVO qnavo = new QNAVO();
            qnavo.setUserIdx(userIdx);
            qnavo.setQnaSubject(qnaSubject);
            qnavo.setQnaContent(qnaContent);

            // 파일 처리
            if(qnaMultipartFile != null && !qnaMultipartFile.isEmpty()) {
                String filename = UUID.randomUUID().toString() + "_" + qnaMultipartFile.getOriginalFilename();
                
                // 파일 업로드
                String path = "D:\\upload\\qna";
                File upLoadDir = new File(path);

                if (!upLoadDir.exists()) {
                    upLoadDir.mkdirs();
                }
                try {
                    qnaMultipartFile.transferTo(new File(upLoadDir, filename));
                    log.info("파일 업로드 완료: " + filename);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                qnavo.setQnaFile(filename);
            }

            // DB에 QNA 저장
            int result = addService.writeQna(qnavo);
            if(result > 0){
                dvo.setSuccess(true);
                dvo.setMessage("QNA 작성 성공");
            }else{
                dvo.setSuccess(false);
            dvo.setMessage("QNA 작성 실패");
            }


        } catch (Exception e) {
            dvo.setSuccess(false);
            dvo.setMessage("QNA 작성 오류");
            e.printStackTrace();
        }
        return dvo;
    }
    
}
