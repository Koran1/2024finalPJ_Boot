package com.ict.finalpj.domain.deal.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.common.vo.ReportVO;
import com.ict.finalpj.common.vo.ViewsVO;
import com.ict.finalpj.domain.deal.service.DealService;
import com.ict.finalpj.domain.deal.vo.DealSatisfactionVO;
import com.ict.finalpj.domain.deal.vo.DealVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/deal")
public class DealController {

    @Autowired
    private DealService dealService;

    // 공통 응답 생성 메서드
    private DataVO createResponse(boolean success, String message, Object data) {
        return DataVO.builder()
            .success(success)
            .message(message)
            .data(data)
            .build();
    }

    // 파일 처리 공통 메서드
    private void handleFileUpload(String dealIdx, MultipartFile[] files) throws Exception {
        if (files == null || files.length == 0) return;

        int maxFiles = 5;
        int storedFiles = 0;
        String path = "D:\\upload\\deal";
        
        for (MultipartFile file : files) {
            if (storedFiles >= maxFiles || file.isEmpty()) continue;

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            
            FileVo fileVo = FileVo.builder()
                .fileTableType("2")
                .fileTableIdx(dealIdx)
                .fileName(fileName)
                .fileOrder(storedFiles)
                .build();

            File upLoadDir = new File(path);
            if (!upLoadDir.exists()) {
                upLoadDir.mkdirs();
            }

            try {
                file.transferTo(new File(upLoadDir, fileName));
                dealService.getIDealFileInsert(fileVo);
                storedFiles++;
                log.info("파일 업로드 완료: {}", fileName);
            } catch (Exception e) {
                log.error("파일 업로드 실패: {}", e.getMessage());
            }
        }
    }
    
    // 파일 조회 공통 메서드
    private List<FileVo> getFileList(String fileTableIdx) {
        try {
            List<FileVo> fileList = dealService.getDealFileDetail(fileTableIdx);
            return fileList != null ? fileList : new ArrayList<>();
        } catch (Exception e) {
            log.error("파일 조회 중 오류 발생: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    // 메인페이지 조회 API
    @GetMapping("/dealMain")
    public DataVO getDealMainList() {
        try {
            List<FileVo> file_list = new ArrayList<>();
            List<DealVO> list = dealService.getDealMainList();

            List<FileVo> fileList = list.stream()
                .map(deal -> dealService.getDealFileOne(deal.getDealIdx()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            Map<String, Object> resultMap = Map.of(
                "list", list,
                "file_list", fileList
            );

            return createResponse(true, "캠핑마켓 메인페이지 조회 완료", resultMap);
        } catch (Exception e) {
            log.error("메인페이지 조회 실패", e);
            return createResponse(false, "캠핑마켓 메인페이지 조회 실패", null);

        }
    }

    // 상품 상세 조회 API
    @GetMapping("/detail/{dealIdx}")
    public DataVO getDealDetail(@PathVariable("dealIdx") String dealIdx) {

        try {
            
            DealVO dealVO = dealService.getDealDetail(dealIdx);

            List<FileVo> fileList = getFileList(dealIdx);
            int viewCount = dealService.getTotalViewCount(dealIdx);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("deal", dealVO);
            resultMap.put("files", fileList);
            resultMap.put("viewCount", viewCount);

            return createResponse(true, "상품 정보 조회 성공", resultMap);
        } catch (Exception e) {
            log.error("상품 상세 조회 오류", e);
            return createResponse(false, "상품 정보 조회 실패", null);
        }
    }

    // 상품 등록 API
    @PostMapping("/write")
    public DataVO getDealWrite(
            @ModelAttribute("data") DealVO dealVO,
            @RequestParam(value = "file", required = false) MultipartFile[] files) {
        try {
            if (!isValidDealVO(dealVO)) {
                return createResponse(false, "필수 입력값이 누락되었습니다", null);
            }

            // 임시로 하드코딩된 사용자 정보 설정
            String dealIdx = UUID.randomUUID().toString();
            dealVO.setDealIdx(dealIdx);
            dealVO.setDealSellerUserIdx(dealVO.getDealSellerUserIdx());  // 테스트용 임시 userIdx
            dealVO.setDealSellerNick(dealVO.getDealSellerNick());  // 테스트용 임시 닉네임


            int result = dealService.getDealWrite(dealVO);
            if (result > 0) {
                handleFileUpload(dealIdx, files);
                return createResponse(true, "상품등록 완료", dealIdx);
            }
            return createResponse(false, "상품등록 실패", null);
        } catch (Exception e) {
            log.error("상품등록 오류", e);
            return createResponse(false, "상품등록 중 오류가 발생했습니다", null);
        }
    }

    // 상품 수정 API
    @PutMapping("/update/{dealIdx}")
    public DataVO getDealUpdate(
        @PathVariable("dealIdx") String dealIdx,
        @ModelAttribute DealVO dealVO,
        @RequestParam(value = "file", required = false) MultipartFile[] files,
        @RequestParam(value = "fileName", required = false) List<String> fileNames)
    {
        try {
            dealVO.setDealIdx(dealIdx);
            log.info("상품 수정 시작 - dealIdx: {}", dealIdx);
            
            // 삭제할 파일 처리
            if (fileNames != null && !fileNames.isEmpty()) {
                log.info("삭제할 파일 개수: {}", fileNames.size());
                for (String fileName : fileNames) {
                    try {
                        // 실제 파일 삭제
                        String filePath = "D:\\upload\\deal\\" + fileName;
                        File file = new File(filePath);
                        if (file.exists()) {
                            boolean isDeleted = file.delete();
                            if (isDeleted) {
                                log.info("물리적 파일 삭제 성공 - fileName: {}", fileName);
                                dealService.getDealFileNameDelete(dealIdx, fileName);
                                log.info("DB 파일 정보 삭제 완료 - fileName: {}", fileName);
                            } else {
                                log.error("물리적 파일 삭제 실패 - fileName: {}", fileName);
                            }
                        } else {
                            log.warn("삭제할 파일이 존재하지 않음 - fileName: {}", fileName);
                            dealService.getDealFileNameDelete(dealIdx, fileName);
                            log.info("DB 파일 정보만 삭제 완료 - fileName: {}", fileName);
                        }
                    } catch (Exception e) {
                        log.error("파일 삭제 중 오류 발생 - fileName: {}", fileName, e);
                    }
                }
            }

            // 새 파일 업로드
            if (files != null && files.length > 0) {
                log.info("업로드할 새 파일 개수: {}", files.length);
                List<FileVo> existingFiles = dealService.getDealFileDetail(dealIdx);
                int nextOrder = existingFiles != null ? existingFiles.size() : 0;

                for (MultipartFile file : files) {
                    if (file != null && !file.isEmpty()) {
                        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                        log.info("새 파일 업로드 시작 - originalName: {}, newFileName: {}", 
                                file.getOriginalFilename(), fileName);
                        
                        FileVo fileVo = FileVo.builder()
                            .fileTableType("2")
                            .fileTableIdx(dealIdx)
                            .fileName(fileName)
                            .fileOrder(nextOrder++)
                            .build();

                        File uploadDir = new File("D:\\upload\\deal");
                        if (!uploadDir.exists()) {
                            uploadDir.mkdirs();
                        }

                        file.transferTo(new File(uploadDir, fileName));
                        dealService.getIDealFileInsert(fileVo);
                        log.info("새 파일 업로드 완료 - fileName: {}", fileName);
                    }
                }
            }

            // 상품 정보 업데이트
            int result = dealService.getDealUpdate(dealVO);
            log.info("상품 정보 업데이트 결과 - dealIdx: {}, result: {}", dealIdx, result);
            
            return createResponse(result > 0, 
                "상품 수정 " + (result > 0 ? "완료" : "실패"), null);
            
        } catch (IOException e) {
            log.error("파일 처리 오류 - dealIdx: {}", dealIdx, e);
            return createResponse(false, "파일 처리 중 오류가 발생했습니다", null);
        } catch (Exception e) {
            log.error("상품 수정 오류 - dealIdx: {}", dealIdx, e);
            return createResponse(false, "상품 수정 실패", null);
        }
    }

    // 파일 삭제 API
    @DeleteMapping("/update/{dealIdx}/file")
    public DataVO deleteFile(
        @PathVariable("dealIdx") String dealIdx,
        @RequestParam("fileName") String fileName) 
    {
        try {
            // 실제 파일 삭제
            String filePath = "D:\\upload\\deal\\" + fileName;
            File file = new File(filePath);
            if (file.exists()) {
                boolean isDeleted = file.delete();
                if (isDeleted) {
                    log.info("물리적 파일 삭제 성공 - fileName: {}", fileName);
                    dealService.getDealFileNameDelete(dealIdx, fileName);
                    log.info("DB 파일 정보 삭제 완료 - fileName: {}", fileName);
                    return createResponse(true, "파일 삭제 성공", null);
                } else {
                    return createResponse(false, "파일 삭제 실패", null);
                }
            } else {
                // 파일이 없더라도 DB에서는 삭제
                dealService.getDealFileNameDelete(dealIdx, fileName);
                return createResponse(true, "DB 파일 정보 삭제 완료", null);

            }
        } catch (Exception e) {
            log.error("파일 삭제 중 오류 발생 - fileName: {}", fileName, e);
            return createResponse(false, "파일 삭제 중 오류 발생", null);
        }
    }

    // 유효성 검사 메서드
    private boolean isValidDealVO(DealVO dealVO) {
        return dealVO != null 
            && dealVO.getDealTitle() != null && !dealVO.getDealTitle().trim().isEmpty()
            && dealVO.getDealDescription() != null && !dealVO.getDealDescription().trim().isEmpty()
            && dealVO.getDealPrice() != null
            && dealVO.getDealCount() != null;
    }



     // 찜하기 상태 조회 + 조회수 로직 처리 API
    @GetMapping("/like-status")
    public ResponseEntity<DataVO> getLikeStatus(
            @RequestParam("userIdx") String userIdx,
            @RequestParam("dealIdx") String dealIdx) {
        try {
            log.info("좋아요 상태 조회 시작 - userIdx: {}, dealIdx: {}", userIdx, dealIdx);
            
            boolean isLiked = dealService.isLiked(userIdx, dealIdx);
            log.info("좋아요 상태 조회 결과 - isLiked: {}", isLiked);
            
            // 조회수 로직 처리
            ViewsVO viewInfo = dealService.getViewCount(userIdx, dealIdx);
            if (viewInfo == null) {
                // log.info("신규 조회 기록 생성");
                dealService.insertViewCount(userIdx, dealIdx);
            } else {
                // log.info("기존 조회수 업데이트 - 현재 조회수: {}", viewInfo.getViewCount());
                dealService.updateViewCount(userIdx, dealIdx);

            }
            
            String message = isLiked ? "이미 좋아요한 상품입니다." : "아직 좋아요하지 않은 상품입니다.";
            // log.info("좋아요 상태 조회 완료 - message: {}", message);
            
            return ResponseEntity.ok(new DataVO(true, isLiked, null, message, null));
        } catch (Exception e) {
            log.error("좋아요 상태 확인 중 오류 발생", e);
            return ResponseEntity.ok(new DataVO(false, null, null, "오류가 발생했습니다.", null));
        }
    }

    // 좋아요 추가/삭제 API
    @RequestMapping("/like")
    public ResponseEntity<DataVO> toggleLike(
            @RequestParam("userIdx") String userIdx,
            @RequestParam("dealIdx") String dealIdx,
            @RequestParam("isLiked") boolean isLiked) {
        try {
            // log.info("좋아요 토글 시작 - userIdx: {}, dealIdx: {}, 현재상태: {}", 
                    // userIdx, dealIdx, isLiked ? "좋아요" : "좋아요 안함");

            // userIdx 유효성 검사
            if (userIdx == null || userIdx.trim().isEmpty()) {
                return ResponseEntity.ok(new DataVO(false, null, null, "유효하지 않은 사용자입니다.", null));
            }

            int result;
            String message;
            if (isLiked) {
                result = dealService.unlikeDeal(userIdx, dealIdx);
                message = result > 0 ? "좋아요가 취소되었습니다." : "좋아요 취소에 실패했습니다.";
                log.info("좋아요 취소 처리 결과 - result: {}, message: {}", result, message);
            } else {
                result = dealService.likeDeal(userIdx, dealIdx);
                message = result > 0 ? "좋아요가 추가되었습니다." : "좋아요 추가에 실패했습니다.";
                log.info("좋아요 추가 처리 결과 - result: {}, message: {}", result, message);
            }


            return ResponseEntity.ok(new DataVO(
                    result > 0,
                    result,
                    null,
                    message,  // 항상 메시지 포함
                    null
            ));
        } catch (Exception e) {
            String errorMessage = "좋아요 처리 중 오류가 발생했습니다: " + e.getMessage();
            log.error(errorMessage);
            return ResponseEntity.ok(new DataVO(false, null, null, errorMessage, null));
        }
    }


    // 파일 순서 재정렬 API
    @PutMapping("/update/{dealIdx}/reorder")
    public DataVO reorderFiles(@PathVariable("dealIdx") String dealIdx) {
        try {
            log.info("파일 순서 재정렬 시작 - dealIdx: {}", dealIdx);
            
            // 현재 파일 목록 조회
            List<FileVo> files = dealService.getDealFileDetail(dealIdx);
            
            // 파일 순서 재정렬
            for (int i = 0; i < files.size(); i++) {
                FileVo file = files.get(i);
                file.setFileOrder(i);
                dealService.getDealFileOrder(file);
            }
            
            log.info("파일 순서 재정렬 완료 - dealIdx: {}", dealIdx);
            return createResponse(true, "파일 순서 재정렬 완료", null);
            
        } catch (Exception e) {
            log.error("파일 순서 재정렬 중 오류 발생 - dealIdx: {}", dealIdx, e);
            return createResponse(false, "파일 순서 재정렬 실패", null);
        }
    }

    // 좋아요 개수 조회 API 추가
    @GetMapping("/favorite-count/{dealIdx}")
    public ResponseEntity<DataVO> getFavoriteCount(@PathVariable("dealIdx") String dealIdx) {
        try {
            int count = dealService.getFavoriteCount(dealIdx);
            // log.info("좋아요 개수 조회 - dealIdx: {}, count: {}", dealIdx, count);
            return ResponseEntity.ok(new DataVO(true, count, null, "좋아요 개수 조회 성공", null));
        } catch (Exception e) {
            log.error("좋아요 개수 조회 중 오류 발생", e);
            return ResponseEntity.ok(new DataVO(false, 0, null, "좋아요 개수 조회 실패", null));
        }
    }

    // 판매 상태 변경 API
    @PutMapping("/status")
    public DataVO updateDealStatus(
        @RequestParam("dealIdx") String dealIdx,
        @RequestParam("senderIdx") String senderIdx,
        @RequestParam("senderNick") String senderNick
        ) {
        DataVO dvo = new DataVO();
            try {
                log.info("dealIdx : " + dealIdx);
                log.info("senderIdx : " + senderIdx);
                log.info("senderNick : " + senderNick);

                DealVO dealvo = new DealVO();
                dealvo.setDealIdx(dealIdx);
                dealvo.setDealBuyerNick(senderNick);
                dealvo.setDealBuyerUserIdx(senderIdx);

                int result = dealService.getDealStatusUpdate(dealvo);
                if(result > 0){
                    dvo.setSuccess(true);
                    dvo.setMessage("판매 완료 상태 변경");
                }else{
                    dvo.setSuccess(false);
                    dvo.setMessage("판매 완료 상태 변경 실패");

                }



            } catch (Exception e) {
                dvo.setSuccess(false);
                dvo.setMessage("판매 상태 변경 오류");
                e.printStackTrace();
            }
        return dvo;
    }

    // 판매자의 다른 상품 조회
    @GetMapping("/seller-other-deals/{dealIdx}")
    public DataVO getSellerOtherDeals(@PathVariable("dealIdx") String dealIdx) {
        try {
            DealVO currentDeal = dealService.getDealDetail(dealIdx);
            if (currentDeal == null) {
                return createResponse(false, "상품 정보를 찾을 수 없습니다.", null);
            }

            List<DealVO> otherDeals = dealService.getSellerOtherDeals(currentDeal.getDealSellerUserIdx(), dealIdx);
            List<FileVo> fileList = otherDeals.stream()
                .map(deal -> dealService.getDealFileOne(deal.getDealIdx()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            Map<String, Object> resultMap = Map.of(
                "deals", otherDeals,
                "files", fileList
            );

            return createResponse(true, "판매자의 다른 상품 조회 성공", resultMap);
        } catch (Exception e) {
            log.error("판매자의 다른 상품 조회 실패", e);
            return createResponse(false, "판매자의 다른 상품 조회 실패", null);
        }
    }
    
    // 만족도 평가 등록 API
    @PostMapping("/satisfaction")
    public ResponseEntity<DataVO> getDealSatisfactionInsert(@RequestBody DealSatisfactionVO satisfactionVO) {
        try {
            log.info("만족도 평가 등록 시작 - 평가점수: {}, 내용: {}", 
                satisfactionVO.getDealSatisSellerScore(), 
                satisfactionVO.getDealSatisBuyerContent());

            int result = dealService.getDealSatisfactionInsert(satisfactionVO);
            
            if (result > 0) {
                // 만족도 평가 등록 성공 시 판매자 평점 업데이트
                dealService.getDealSatisSellerScoreUpdate(satisfactionVO.getDealSatisSellerUserIdx());
                return ResponseEntity.ok(new DataVO(true, null, null, "만족도 평가가 등록되었습니다.", null));
            } else {
                return ResponseEntity.ok(new DataVO(false, null, null, "만족도 평가 등록에 실패했습니다.", null));
            }
        } catch (Exception e) {
            log.error("만족도 평가 등록 중 오류 발생", e);
            return ResponseEntity.ok(new DataVO(false, null, null, "만족도 평가 등록 중 오류가 발생했습니다.", null));
        }
    }


    // 판매자의 평점 조회
    @GetMapping("/seller-score/{dealSellerUserIdx}")
    @ResponseBody
    public ResponseEntity<DataVO> getDealSatisSellerScore(
        @PathVariable(name = "dealSellerUserIdx") String dealSellerUserIdx
    ) {
        try {
            if (dealService.getUserInfoByIdx(dealSellerUserIdx) == null) {
                return ResponseEntity.ok(new DataVO(true, 5.0, null, "사용자를 찾을 수 없음", null));
            }
            String averageScore = dealService.getDealSatisSellerScore(dealSellerUserIdx);
            
            if (averageScore == null) {
                return ResponseEntity.ok(new DataVO(true, 5.0, null, "평점 없음", null));
            }
            double score = Double.parseDouble(averageScore);
            return ResponseEntity.ok(new DataVO(true, score, null, "판매자 평점 조회 성공", null));
        } catch (Exception e) {
            log.error("평점 조회 실패 - dealSellerUserIdx: {}, 에러: {}", dealSellerUserIdx, e.getMessage());
            return ResponseEntity.ok(new DataVO(true, 5.0, null, "평점 조회 실패", null));
        }
    }

    @PutMapping("/active/{dealIdx}")
    public ResponseEntity<DataVO> getDealActiveUpdate(
        @PathVariable("dealIdx") String dealIdx,
        @RequestParam("dealview") int dealview) {
        try {
            // 상품 상태 업데이트
            int result = dealService.getDealActiveUpdate(dealIdx, dealview);
            
            // dealview가 0이면 신고 상태도 업데이트
            if (dealview == 0) {
                dealService.updateReportStatus(dealIdx);
            }
            
            if (result > 0) {
                return ResponseEntity.ok(new DataVO(true, null, null, "상품 상태가 변경되었습니다.", null));
            } else {
                return ResponseEntity.ok(new DataVO(false, null, null, "상품 상태 변경에 실패했습니다.", null));
            }
        } catch (Exception e) {
            log.error("상품 상태 변경 중 오류 발생", e);
            return ResponseEntity.ok(new DataVO(false, null, null, "상품 상태 변경 중 오류가 발생했습니다.", null));
        }
    }

    // 후기 등록 여부 확인
    @GetMapping("/check-satisfaction")
    public DataVO chkSatisfaction(
        @RequestParam("dealIdx") String dealSatis01
        ) {
        DataVO dvo = new DataVO();
        try {
            boolean result = dealService.chkSatisfaction(dealSatis01);
            dvo.setData(result);
            dvo.setSuccess(true);
            dvo.setMessage("후기 등록 여부");
        } catch (Exception e) {
            dvo.setSuccess(false);
            dvo.setMessage("후기 등록 여부 확인 오류");
            e.printStackTrace();
        }
        return dvo;
    }

    @PostMapping("/report")
    public ResponseEntity<DataVO> getDealReportInsert(@RequestBody ReportVO reportVO) {
        try {
            int result = dealService.getDealReportInsert(reportVO);
            if (result > 0) {
                return ResponseEntity.ok(new DataVO(true, null, null, "신고가 접수되었습니다.", null));
            } else {
                return ResponseEntity.ok(new DataVO(false, null, null, "신고 접수에 실패했습니다.", null));
            }
        } catch (Exception e) {
            log.error("신고 접수 중 오류 발생", e);
            return ResponseEntity.ok(new DataVO(false, null, null, "신고 접수 중 오류가 발생했습니다.", null));
        }
    }
    
}