package com.ict.finalpj.domain.deal.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public DataVO getDealMainList() {
        DataVO dataVO = new DataVO();
        try {
            List<DealVO> list = dealService.getDealMainList();
            dataVO.setSuccess(true);
            dataVO.setMessage("캠핑마켓 메인페이지 조회 완료");
            dataVO.setData(list);
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("캠핑마켓 메인페이지 조회 실패");
        }
        return dataVO;
    }

    @GetMapping("/detail/{dealIdx}")
    public ResponseEntity<Map<String, Object>> getDealDetail(@PathVariable String dealIdx) {
        Map<String, Object> response = new HashMap<>();
        try {
            DealVO deal = dealService.getDealDetail(dealIdx);
            List<FileVo> files = dealService.getPjFileByDealIdx(dealIdx);

            response.put("success", true);
            response.put("deal", deal);
            response.put("files", files);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "상품 정보를 불러오는 중 오류가 발생했습니다.");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/write")
    public DataVO getDealWrite(
            @ModelAttribute("data") DealVO dealVO,
            @RequestParam(value = "file", required = false) MultipartFile[] files) {

        // UUID 생성
        String dealIdx = UUID.randomUUID().toString();
        dealVO.setDealIdx(dealIdx); // DealVO에 dealIdx 설정
        log.info("Generated dealIdx: " + dealIdx); // 로그 추가

        DataVO dataVO = dealService.getDealWrite(dealVO);

        // dealIdx가 서비스에서 설정된 후 로그 추가
        log.info("After service call, dealIdx: " + dealVO.getDealIdx()); // 로그 추가

        // 파일 업로드 및 DB 저장 처리
        if (files != null && files.length > 0) {
            log.info("첨부된 파일 개수: " + files.length);

            int maxFiles = 6; // 최대 파일 개수 설정
            int storedFiles = 0; // 저장된 파일 개수 추적

            for (int i = 0; i < files.length; i++) {
                if (storedFiles >= maxFiles) {
                    break; // 최대 파일 수 도달 시 루프 종료
                }

                MultipartFile file = files[i];
                if (!file.isEmpty()) {
                    FileVo fileVo = new FileVo();

                    // 파일명 생성
                    UUID uuid = UUID.randomUUID();
                    String fileName = uuid.toString() + "_" + file.getOriginalFilename();
                    log.info("생성된 파일명: " + fileName);

                    // FileVo 설정
                    fileVo.setFileTableType("2");
                    fileVo.setFileTableIdx(dealIdx); // dealIdx를 fileTableIdx에 설정
                    fileVo.setFileName(fileName);
                    fileVo.setFileOrder(storedFiles); // int로 설정

                    // 파일 업로드 경로
                    String path = "D:\\upload\\deal";
                    // 업로드할 디렉토리 객체 생성
                    File upLoadDir = new File(path);

                    if (!upLoadDir.exists()) {
                        upLoadDir.mkdirs();
                    }

                    // 파일 업로드
                    try {
                        file.transferTo(new File(upLoadDir, fileName));
                    } catch (Exception e) {
                        log.error("파일 업로드 중 오류 발생: " + e.getMessage());
                        continue; // 다음 파일로 이동
                    }
                    log.info("파일 업로드 완료: " + fileName);

                    // DB에 파일 정보 저장
                    dealService.insertFileInfo(fileVo);
                    log.info("파일 정보 DB 저장 완료 - 순서: " + storedFiles);

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
    public DataVO getDealUpdate(@PathVariable("dealIdx") String dealIdx, @RequestBody DealVO dealVO) {
        DataVO dataVO = new DataVO();
        try {
            dealVO.setDealIdx(dealIdx);
            
            // 파라미터 유효성 검사
            if (dealVO.getDealTitle() == null || dealVO.getDealCategory() == null || dealVO.getDealStatus() == null || dealVO.getDealDescription() == null || 
              dealVO.getDealPrice() == null || dealVO.getDealPackage() == null || dealVO.getDealDirect() == null || dealVO.getDealDirectContent() == null || 
              dealVO.getDealCount() == null) { // 필수 필드가 누락되었는지 확인
                dataVO.setSuccess(false); // 실패 여부 설정
                dataVO.setMessage("필수 입력값이 누락되었습니다."); // 입력값 누락 메시지 설정
                return dataVO; // 데이터 전달 객체 반환
            }

            DataVO result = dealService.getDealUpdate(dealVO, files);

            if (result == null) { // 업데이트 실패 시
                dataVO.setSuccess(false); // 실패 여부 설정
                dataVO.setMessage("캠핑마켓 수정 실패"); // 수정 실패 메시지 설정
                return dataVO; // 데이터 전달 객체 반환
            }
            dataVO.setSuccess(true); // 성공 여부 설정
            dataVO.setMessage("캠핑마켓 수정 성공"); // 수정 성공 메시지 설정

        } catch (NullPointerException | IllegalArgumentException e) { // NullPointerException 또는 IllegalArgumentException 발생 시
            dataVO.setSuccess(false); // 실패 여부 설정
            dataVO.setMessage("캠핑마켓 수정 중 잘못된 입력이 발생했습니다."); // 잘못된 입력 메시지 설정
            log.error("캠핑마켓 수정 오류: ", e); // 오류 로그 기록
        } catch (Exception e) { // 그 외의 예외 발생 시
            dataVO.setSuccess(false); // 실패 여부 설정
            dataVO.setMessage("캠핑마켓 수정 중 예상치 못한 오류가 발생했습니다."); // 예상치 못한 오류 메시지 설정
            log.error("캠핑마켓 수정 오류: ", e); // 오류 로그 기록
        }
        return dataVO;  // 데이터 전달 객체 반환
    }

}