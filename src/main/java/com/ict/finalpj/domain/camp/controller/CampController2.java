package com.ict.finalpj.domain.camp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.domain.camp.service.CampService2;
import com.ict.finalpj.domain.camp.vo.CampFavVO;
import com.ict.finalpj.domain.camp.vo.CampVO;
import com.ict.finalpj.domain.camplog.vo.CampLogVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/camp")
public class CampController2 {
    @Autowired
    private CampService2 campService2;

    // 캠핑장 상세보기 기능
    @GetMapping("/detail/{campIdx}")
    public DataVO getCampDetail(@PathVariable("campIdx") String campIdx) {
        DataVO dataVO = new DataVO();
        try {
            // 캠핑장 상세 정보 조회
            CampVO campDetail = campService2.getCampDetail(campIdx);

            dataVO.setSuccess(true);
            dataVO.setMessage("캠핑장 상세보기 성공");
            dataVO.setData(campDetail);
            log.info("캠핑장 상세보기 성공");
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("캠핑장 상세보기 실패");
            log.info("캠핑장 상세보기 실패");
        }
        return dataVO;
    }

    // 후기 가져오기 기능
    @GetMapping("/detail/log/{campIdx}")
    public DataVO getLogDetail(@PathVariable("campIdx") String campIdx, @RequestParam("logAlign") String logAlign) {
        DataVO dataVO = new DataVO();
        log.info("logAlign: " + logAlign);
        try {
            List<CampLogVO> campLogDetail = campService2.getCampLog(campIdx, logAlign);
            dataVO.setSuccess(true);
            dataVO.setMessage("후기 목록 접근 성공");
            dataVO.setData(campLogDetail);
            log.info("후기 성공");
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("후기 실패");
            log.info("캠핑장 로그 조회 실패", e);
        }
        return dataVO;
    }

    // 찜하기 상태 조회 + 조회수 로직 처리 API
    @GetMapping("/like-status")
    public ResponseEntity<DataVO> getLikeStatus(
            @RequestParam("userIdx") String userIdx,
            @RequestParam("campIdx") String campIdx) {
        boolean isLiked = campService2.isLiked(userIdx, campIdx);
        String message = isLiked ? "The user has already liked this camp." : "The user has not liked this camp yet.";
        log.info(message);
        // 조회수 로직 처리
        campService2.updateViewCount(userIdx, campIdx);
        // DataVO로 응답
        DataVO response = new DataVO(
                true, // 성공 여부
                isLiked, // data 필드에 좋아요 상태 전달
                null, // JWT 토큰 (불필요한 경우 null)
                message, // 메시지
                null // UserDetails (불필요한 경우 null)
        );

        return ResponseEntity.ok(response);
    }

    // 좋아요 추가/삭제 API
    @RequestMapping("/like")
    public ResponseEntity<DataVO> toggleLike(
            @RequestParam("userIdx") String userIdx,
            @RequestParam("campIdx") String campIdx,
            @RequestParam("isLiked") boolean isLiked) {
        log.info("좋아요 추가/삭제 API 호출");

        if (isLiked) {
            // 이미 좋아요 상태라면 좋아요 취소
            campService2.unlikeCamp(userIdx, campIdx);
        } else {
            // 좋아요 추가
            campService2.likeCamp(userIdx, campIdx);
        }

        String message = isLiked ? "Like removed successfully." : "Like added successfully.";
        log.info(message);

        DataVO response = new DataVO(
                true, // 성공 여부
                null, // data는 불필요하므로 null
                null, // JWT 토큰 (불필요한 경우 null)
                message, // 메시지
                null // UserDetails (불필요한 경우 null)
        );

        return ResponseEntity.ok(response);
    }

    // 캠핑장 좋아요 리스트
    @GetMapping("/getLikeList")
    public DataVO getLikeList(@RequestParam("userIdx") String userIdx) {
        DataVO dvo = new DataVO();
        try {
            List<CampFavVO> favList = campService2.getLikeList(userIdx);
            dvo.setData(favList);
            dvo.setSuccess(true);
            dvo.setMessage("캠핑장 좋아요 리스트 조회 성공");

        } catch (Exception e) {
            dvo.setSuccess(false);
            dvo.setMessage("캠핑장 좋아요 리스트 조회 오류");
            e.printStackTrace();
        }
        return dvo;
    }
    

}
