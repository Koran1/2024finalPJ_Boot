package com.ict.finalpj.domain.deal.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.ict.finalpj.domain.deal.service.DealService;
import com.ict.finalpj.domain.deal.vo.DealVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/deal")
public class DealController {

    @Autowired
    private DealService dealService;

    @GetMapping("/dealMain")
    public ResponseEntity<DataVO> getDealMainList() {
        Map<String, Object> resultMap = new HashMap<>();
        DataVO dataVO = new DataVO();
        try {
            List<DealVO> dealList = dealService.getDealMainList(); 
            List<FileVo> file_list = new ArrayList<>();
            
            for(DealVO dealVO : dealList) {
                List<FileVo> fvos = dealService.getPjFileByDealIdx(dealVO.getDealIdx());
                if(fvos != null && !fvos.isEmpty()) {
                    file_list.addAll(fvos);
                }
            }

            resultMap.put("list", dealList);
            resultMap.put("file_list", file_list);
            
            dataVO.setSuccess(true);
            dataVO.setMessage("캠핑마켓 메인페이지 조회 완료");
            dataVO.setData(resultMap);
            
            return ResponseEntity.ok(dataVO);
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("캠핑마켓 메인페이지 조회 실패");
            log.error("캠핑마켓 메인페이지 조회 중 오류 발생", e);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dataVO);
        }
    }

    @GetMapping("/detail/{dealIdx}")
    public ResponseEntity<DataVO> getDealDetail(@PathVariable("dealIdx") String dealIdx) {
        DataVO dataVO = new DataVO();
        
        try {
            DealVO dealVO = dealService.getDealDetail(dealIdx);
            List<FileVo> files = dealService.getPjFileByDealIdx(dealIdx);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("deal", dealVO);
            resultMap.put("files", files);
            
            dataVO.setSuccess(true);
            dataVO.setMessage("상품 상세 정보 조회 완료");
            dataVO.setData(resultMap);
            
            return ResponseEntity.ok(dataVO);
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("상품 정보를 불러오는 중 오류가 발생했습니다.");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dataVO);
        }
    }

    @PostMapping("/write")
    public DataVO getDealWrite(
            @ModelAttribute("data") DealVO dealVO,
            @RequestParam(value = "file", required = false) MultipartFile[] files) {

        // UUID 생성
        String dealIdx = UUID.randomUUID().toString();
        dealVO.setDealIdx(dealIdx); // DealVO에 dealIdx 설정
        log.info("Generated dealIdx: " + dealIdx); // 로그 추가

        // Deal 저장을 서비스 레이어로 위임
        DataVO dataVO = dealService.getDealWrite(dealVO, null); // files 파라미터 제거

        log.info("After service call, dealIdx: " + dealVO.getDealIdx()); // 로그 추가

        // 파일 업로드 및 DB 저장 처리
        if (files != null && files.length > 0) {
            log.info("첨부된 파일 개수: " + files.length);

            int maxFiles = 5; // 최대 파일 개수 설정
            int storedFiles = 0; // 저장된 파일 개수 추적

            for (MultipartFile file : files) {
                if (storedFiles >= maxFiles) {
                    break; // 최대 파일 수 도달 시 루프 종료
                }

                if (!file.isEmpty()) {
                    // 파일명 생성 (UUID는 여기서 한 번만 생성됩니다.)
                    String uuid = UUID.randomUUID().toString();
                    String fileName = uuid + "_" + file.getOriginalFilename();
                    log.info("생성된 파일명: " + fileName);

                    // FileVo 설정
                    FileVo fileVo = FileVo.builder()
                        .fileTableType("2")
                        .fileTableIdx(dealVO.getDealIdx())
                        .fileName(fileName)
                        .fileOrder(storedFiles)
                        .build();

                    // 파일 업로드 경로
                    String path = "D:\\upload\\deal";
                    File upLoadDir = new File(path);

                    if (!upLoadDir.exists()) {
                        upLoadDir.mkdirs();
                    }

                    // 파일 업로드
                    try {
                        file.transferTo(new File(upLoadDir, fileName));
                        log.info("파일 업로드 완료: " + fileName);
                    } catch (Exception e) {
                        log.error("파일 업로드 중 오류 발생: " + e.getMessage());
                        continue; // 다음 파일로 이동
                    }

                    // DB에 파일 정보 저장 (insertFileInfo은 여기서 한 번만 호출됩니다.)
                    dealService.insertFileInfo(fileVo);

                    storedFiles++; // 저장된 파일 개수 증가
                }
            }

            // 만약 업로드된 파일이 최대 개수를 초과한다면 로그 또는 메시지 추가
            if (files.length > maxFiles) {
                log.warn("최대 " + maxFiles + "개의 파일만 업로드됩니다. 초과된 파일은 무시됩니다.");
                // 필요 시 사용자에게 메시지를 반환하도록 구현
            }
        } else {
            log.info("첨부된 파일 없음");
        }

        return dataVO;
    }

    @PutMapping("/update/{dealIdx}")
    public ResponseEntity<Map<String, Object>> getDealUpdate(
        @PathVariable("dealIdx") String dealIdx,
        @ModelAttribute DealVO dealVO,
        @RequestParam(value = "file", required = false) MultipartFile[] files) 
    {
        // dealIdx를 DealVO에 설정
        dealVO.setDealIdx(dealIdx);
        
        try {
            dealVO.setDealIdx(dealIdx);
            
            // 유효성 검사
            if (dealVO.getDealTitle() == null || dealVO.getDealCategory() == null || 
                dealVO.getDealStatus() == null || dealVO.getDealDescription() == null || 
                dealVO.getDealPrice() == null || dealVO.getDealPackage() == null || 
                dealVO.getDealDirect() == null || dealVO.getDealDirectContent() == null || 
                dealVO.getDealCount() == null) 
            {
                response.put("success", false);
                response.put("message", "필수 입력값이 누락되었습니다.");
                return ResponseEntity.badRequest().body(response);
            }

            // 기존 파일 정보 조회
            List<FileVo> existingFiles = dealService.getPjFileByDealIdx(dealIdx);
            Map<Integer, FileVo> existingFileMap = new HashMap<>();
            for (FileVo file : existingFiles) {
                existingFileMap.put(file.getFileOrder(), file);
            }

            // 새로운 파일 처리
            if (files != null && files.length > 0) {
                int maxFiles = 5;
                for (int i = 0; i < files.length && i < maxFiles; i++) {
                    MultipartFile file = files[i];
                    if (!file.isEmpty()) {
                        String uuid = UUID.randomUUID().toString();
                        String fileName = uuid + "_" + file.getOriginalFilename();

                        String path = "D:\\upload\\deal";
                        File upLoadDir = new File(path);
                        if (!upLoadDir.exists()) {
                            upLoadDir.mkdirs();
                        }

                        // 파일 업로드
                        try {
                            file.transferTo(new File(upLoadDir, fileName));
                            log.info("파일 업로드 완료: " + fileName);
                        } catch (Exception e) {
                            log.error("파일 업로드 중 오류 발생: " + e.getMessage());
                            continue; // 다음 파일로 이동
                        }

                        // DB에 파일 정보 저장 (insertFileInfo은 여기서 한 번만 호출됩니다.)
                        dealService.insertFileInfo(fileVo);

                        storedFiles++; // 저장된 파일 개수 증가
                    }
                }

                // 만약 업로드된 파일이 최대 개수를 초과한다면 로그 또는 메시지 추가
                if (files.length > maxFiles) {
                    log.warn("최대 " + maxFiles + "개의 파일만 업로드됩니다. 초과된 파일은 무시됩니다.");
                    // 필요 시 사용자에게 메시지를 반환하도록 구현
                }
            } else {
                log.info("첨부된 파일 없음");
            }

            // 수정이 성공하면 /api/deal/detail/{dealIdx}로 리다이렉트
            URI redirectUri = URI.create("/detail/" + dealIdx);
            return ResponseEntity.status(HttpStatus.FOUND).location(redirectUri).build();

        } catch (NullPointerException | IllegalArgumentException e) { 
            log.error("캠핑마켓 수정 오류: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) { 
            log.error("캠핑마켓 수정 오류: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}