package com.ict.finalpj.domain.add.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.domain.add.service.AddService;
import com.ict.finalpj.domain.add.vo.NoticeVO;

import lombok.extern.slf4j.Slf4j;

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
    
}
