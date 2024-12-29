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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.common.vo.ViewsVO;
import com.ict.finalpj.domain.deal.service.DealService;
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

    @GetMapping("/dealMain")
    public DataVO getDealMainList() {
        try {
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

    @GetMapping("/detail/{dealIdx}")
    public DataVO getDealDetail(@PathVariable("dealIdx") String dealIdx) {
        try {
            DealVO dealVO = dealService.getDealDetail(dealIdx);
            List<FileVo> fileList = getFileList(dealIdx);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("deal", dealVO);
            resultMap.put("files", fileList);

            return createResponse(true, "상품 정보 조회 성공", resultMap);
        } catch (Exception e) {
            log.error("상품 상세 조회 오류", e);
            return createResponse(false, "상품 정보 조회 실패", null);
        }
    }

    @PostMapping("/write")
    public DataVO getDealWrite(
            @ModelAttribute("data") DealVO dealVO,
            @RequestParam(value = "file", required = false) MultipartFile[] files) {
        try {
            if (!isValidDealVO(dealVO)) {
                return createResponse(false, "필수 입력값이 누락되었습니다", null);
            }

            String dealIdx = UUID.randomUUID().toString();
            dealVO.setDealIdx(dealIdx);

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
        return dealVO.getDealTitle() != null && 
                dealVO.getDealCategory() != null && 
                dealVO.getDealStatus() != null && 
                dealVO.getDealDescription() != null && 
                dealVO.getDealPrice() != null && 
                dealVO.getDealPackage() != null && 
                dealVO.getDealDirect() != null && 
                dealVO.getDealDirectContent() != null && 
                dealVO.getDealCount() != null;
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
                log.info("신규 조회 기록 생성");
                dealService.insertViewCount(userIdx, dealIdx);
            } else {
                log.info("기존 조회수 업데이트 - 현재 조회수: {}", viewInfo.getViewCount());
                dealService.updateViewCount(userIdx, dealIdx);
            }
            
            String message = isLiked ? "이미 좋아요한 상품입니다." : "아직 좋아요하지 않은 상품입니다.";
            log.info("좋아요 상태 조회 완료 - message: {}", message);
            
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
            log.info("좋아요 토글 시작 - userIdx: {}, dealIdx: {}, 현재상태: {}", 
                    userIdx, dealIdx, isLiked ? "좋아요" : "좋아요 안함");

            int result;
            if (isLiked) {
                // 이미 좋아요 상태라면 좋아요 취소
                result = dealService.unlikeDeal(userIdx, dealIdx);
                log.info("좋아요 취소 처리 결과 - result: {}", result);
            } else {
                // 좋아요 추가
                result = dealService.likeDeal(userIdx, dealIdx);
                log.info("좋아요 추가 처리 결과 - result: {}", result);
            }

            String message = isLiked ? "좋아요가 취소되었습니다." : "좋아요가 추가되었습니다.";
            log.info("좋아요 토글 완료 - message: {}", message);

            return ResponseEntity.ok(new DataVO(
                    result > 0, // 성공 여부
                    null,      // data
                    null,      // JWT 토큰
                    message,   // 메시지
                    null       // UserDetails
            ));
        } catch (Exception e) {
            log.error("좋아요 처리 중 오류 발생 - userIdx: {}, dealIdx: {}", userIdx, dealIdx, e);
            return ResponseEntity.ok(new DataVO(false, null, null, "좋아요 처리 중 오류가 발생했습니다.", null));
        }
    }

}