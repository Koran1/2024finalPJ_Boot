package com.ict.finalpj.domain.deal.controller; 

import java.io.File;
import java.util.ArrayList;
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
            List<FileVo> file_list = new ArrayList<>();
            List<DealVO> list = dealService.getDealMainList();

            for(DealVO k : list){
                FileVo fvo =  dealService.getFileVO(k.getDealIdx());
                if(fvo == null) continue;
                file_list.add(fvo);
            }  

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("list", list);
            resultMap.put("file_list", file_list);


            dataVO.setSuccess(true);
            dataVO.setMessage("캠핑마켓 메인페이지 조회 완료");
            dataVO.setData(resultMap);
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("캠핑마켓 메인페이지 조회 실패");
        }
        return dataVO;
    }

    @GetMapping("/detail/{dealIdx}")
    public DataVO getDealDetail(@PathVariable("dealIdx") String dealIdx) {
        DataVO dataVO = new DataVO();
        try {
            // 상품 정보 조회
            DealVO deal = dealService.getDealDetail(dealIdx);
            if (deal == null) {
                dataVO.setSuccess(false);
                dataVO.setMessage("상품을 찾을 수 없습니다.");
                return dataVO;
            }

            // 파일 정보 조회
            List<FileVo> files = dealService.getPjFileByDealIdx(dealIdx);
            
            // 응답 데이터 구성
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("deal", deal);
            responseData.put("files", files);

            dataVO.setSuccess(true);
            dataVO.setData(responseData);
            
        } catch (Exception e) {
            log.error("상품 상세 정보 조회 중 오류 발생: ", e);
            dataVO.setSuccess(false);
            dataVO.setMessage("상품 정보를 불러오는 중 오류가 발생했습니다.");
        }
        
        return dataVO;
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
    public DataVO getDealUpdate(
        @PathVariable("dealIdx") String dealIdx,
        @ModelAttribute DealVO dealVO,
        @RequestParam(value = "file", required = false) MultipartFile[] files) {
        
        DataVO dataVO = new DataVO();
        try {
            dealVO.setDealIdx(dealIdx);
            
            // 필수 필드 유효성 검사
            if (dealVO.getDealTitle() == null || dealVO.getDealCategory() == null || 
                dealVO.getDealStatus() == null || dealVO.getDealDescription() == null || 
                dealVO.getDealPrice() == null || dealVO.getDealPackage() == null || 
                dealVO.getDealDirect() == null || dealVO.getDealCount() == null) {
                dataVO.setSuccess(false);
                dataVO.setMessage("필수 입력값이 누락되었습니다.");
                return dataVO;
            }

            // 직거래 가능일 경우 직거래 내용 필수 체크
            if ("직거래 가능".equals(dealVO.getDealDirect()) && 
                (dealVO.getDealDirectContent() == null || dealVO.getDealDirectContent().trim().isEmpty())) {
                dataVO.setSuccess(false);
                dataVO.setMessage("직거래 가능 지역을 입력해주세요.");
                return dataVO;
            }

            // 파일 처리
            if (files != null && files.length > 0) {
                String path = "D:\\upload\\deal";
                File uploadDir = new File(path);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                        FileVo fileVo = new FileVo();
                        fileVo.setFileTableType("2");
                        fileVo.setFileTableIdx(dealIdx);
                        fileVo.setFileName(fileName);
                        
                        file.transferTo(new File(uploadDir, fileName));
                        dealService.insertFileInfo(fileVo);
                    }
                }
            }

            // 상품 정보 업데이트
            DataVO result = dealService.getDealUpdate(dealVO, files);
            if (result == null) {
                dataVO.setSuccess(false);
                dataVO.setMessage("상품 수정에 실패했습니다.");
                return dataVO;
            }

            dataVO.setSuccess(true);
            dataVO.setMessage("상품이 성공적으로 수정되었습니다.");
            dataVO.setData(dealVO);

        } catch (Exception e) {
            log.error("상품 수정 중 오류 발생: ", e);
            dataVO.setSuccess(false);
            dataVO.setMessage("상품 수정 중 오류가 발생했습니다.");
        }
        
        return dataVO;
    }

}