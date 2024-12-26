package com.ict.finalpj.domain.deal.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.common.vo.FileVo;
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
            log.info("Files found for dealIdx {}: {}", fileTableIdx, fileList);
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
        @RequestParam(value = "deletedFiles", required = false) String deletedFilesJson) 
    {
        try {
            dealVO.setDealIdx(dealIdx);
            
            if (!isValidDealVO(dealVO)) {
                return createResponse(false, "필수 항목이 누락되었습니다.", null);
            }

            handleDeletedFiles(dealIdx, deletedFilesJson);
            int result = dealService.getDealUpdate(dealVO);
            
            return createResponse(
                result > 0,
                result > 0 ? "상품 수정 완료" : "상품 수정 실패",
                null
            );
        } catch (Exception e) {
            log.error("캠핑마켓 수정 오류", e);
            return createResponse(false, "상품 수정 오류", null);
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

    // 삭제된 파일 처리 메서드
    private void handleDeletedFiles(String dealIdx, String deletedFilesJson) throws Exception {
        if (deletedFilesJson == null || deletedFilesJson.isEmpty()) return;

        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> deletedFiles = mapper.readValue(deletedFilesJson, 
            new TypeReference<List<Map<String, Object>>>() {});

        for (Map<String, Object> fileInfo : deletedFiles) {
            FileVo fileVo = FileVo.builder()
                .fileTableType("2")
                .fileTableIdx(dealIdx)
                .fileName((String) fileInfo.get("fileName"))
                .fileOrder(((Number) fileInfo.get("fileOrder")).intValue())
                .build();
            dealService.getDealFileDelete(fileVo);
        }
        
        dealService.getDealFileUpdate(FileVo.builder()
            .fileTableIdx(dealIdx)
            .build());
    }
}