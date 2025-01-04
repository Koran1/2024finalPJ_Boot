package com.ict.finalpj.domain.camplog.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.common.vo.ReportVO;
import com.ict.finalpj.domain.camplog.service.CampLogService;
import com.ict.finalpj.domain.camplog.vo.CampLogCommentVO;
import com.ict.finalpj.domain.user.service.UserService;
import com.ict.finalpj.domain.user.vo.UserVO;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@RestController
@RequestMapping("/api/camplog")
public class CampLogController {
    @Autowired
    CampLogService campLogService;

    @Autowired
    UserService userService;

    @GetMapping("commentList")
    public DataVO getCommentList(@RequestParam("logIdx") String logIdx) {
        log.info("logIdx : " + logIdx);
        DataVO dataVO = new DataVO();
        try {
            // 댓글 리스트 불러오기
            List<CampLogCommentVO> lcvo = campLogService.getCommentList(logIdx);
            // 댓글 리스트에서 userIdx 만 추출
            List<String> userIdxList = lcvo.stream()
                                 .map(CampLogCommentVO::getUserIdx)  // CampLogCommentVO에서 userIdx만 추출
                                 .distinct()                        // 중복 제거
                                 .collect(Collectors.toList());
            // userIdxList를 통해 UserVO 리스트 불러오기
            List<UserVO> uvo = campLogService.getUserInfoByIdx(userIdxList);

            // userIdxList를 통해 
            List<ReportVO> rvo = campLogService.getCommentReportCount(userIdxList);
            log.info("lcvo : " + lcvo);

            // userIdx와 userNickname을 매핑
            Map<String, String> userNicknameMap = uvo.stream()
                .collect(Collectors.toMap(UserVO::getUserIdx, UserVO::getUserNickname));

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("lcvo", lcvo);
            resultMap.put("userNicknameMap", userNicknameMap);
            resultMap.put("rvo", rvo);
            dataVO.setSuccess(true);
            dataVO.setMessage("댓글 리스트를 불러옵니다.");
            dataVO.setData(resultMap);
            return dataVO;
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("댓글 리스트 불러오기 오류 발생");
            e.printStackTrace();
        }
        return dataVO;
    }

    @PostMapping("commentWrite")
    public DataVO getCommentWrite(@ModelAttribute CampLogCommentVO lcvo) {
        DataVO dataVO = new DataVO();
        try {
            int result = campLogService.getCommentWrite(lcvo);
            if(result > 0){
                dataVO.setSuccess(true);

                if(lcvo.getCommentIdx() != null){
                    dataVO.setMessage("댓글 저장 완료");
                }else{
                    dataVO.setMessage("답글 저장 완료");
                }
            }
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("댓/답글 저장 오류");
            e.printStackTrace();
        }
        return dataVO;   
    }

    @PostMapping("commentDelete")
    public DataVO getCommentDelete(@ModelAttribute CampLogCommentVO lcvo) {
        DataVO dataVO = new DataVO();
        try {
            String logCommentIdx = lcvo.getLogCommentIdx();
            int result = campLogService.getCommentDelete(logCommentIdx);

            if(result > 0){
                dataVO.setSuccess(true);

                if(lcvo.getCommentIdx() != null){
                    dataVO.setMessage("댓글 삭제 완료");
                }else{
                    dataVO.setMessage("답글 삭제 완료");
                }
            }
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("댓/답글 삭제 오류");
            e.printStackTrace();
        }
        return dataVO;
    }

    @PostMapping("commentReport")
    public DataVO getCommentReport(@ModelAttribute ReportVO rvo) {
        DataVO dataVO = new DataVO();
        try {
            log.info("rvo : " + rvo);
            int result = campLogService.getCommentReport(rvo);

            if(result > 0){
                dataVO.setSuccess(true);
                dataVO.setMessage("댓글 신고 완료");
            }
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("댓/답글 신고 오류");
            e.printStackTrace();
        }
        return dataVO;
    }
}
