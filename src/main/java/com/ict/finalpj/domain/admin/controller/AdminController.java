package com.ict.finalpj.domain.admin.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.domain.admin.service.AdminService;
import com.ict.finalpj.domain.deal.vo.DealVO;
import lombok.extern.slf4j.Slf4j;
import com.ict.finalpj.domain.add.vo.QNAVO;
import com.ict.finalpj.domain.camp.vo.CampSearchVO;

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

    // 건우
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

    // QnA 관리
    @GetMapping("/qnaList")
    public DataVO getMethodName() {
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

}
