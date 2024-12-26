package com.ict.finalpj.domain.deal.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @GetMapping("/dealMain")
    public DataVO getDealMainList() {
        DataVO dataVO = new DataVO();
        try {
            List<FileVo> file_list = new ArrayList<>();
            List<DealVO> list = dealService.getDealMainList();

            for(DealVO k : list){
                FileVo fvo =  dealService.getDealFileOne(k.getDealIdx());
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
            e.printStackTrace();
        }
        return dataVO;
    }

    @GetMapping("/detail/{dealIdx}")
    public DataVO getDealDetail(@PathVariable("dealIdx") String dealIdx) {
        DataVO dataVO = new DataVO();
        
        try {
            DealVO dealVO = dealService.getDealDetail(dealIdx);
            List<FileVo> files = dealService.getDealFileDetail(dealIdx);
            
            log.info("DealVO: {}", dealVO);
            log.info("Files found: {}", files);

            if (files == null || files.isEmpty()) {
                log.warn("No files found for dealIdx: {}", dealIdx);
            }

            dataVO.setSuccess(true);
            dataVO.setMessage("상품 정보 조회 성공");

            Map<String, Object> response = new HashMap<>();
            response.put("deal", dealVO);
            response.put("files", files);
            dataVO.setData(response);
            
        } catch (Exception e) {
            log.error("상품 상세 조회 오류:", e);
            dataVO.setSuccess(false);
            dataVO.setMessage("상품 정보 조회 실패");
        }
        return dataVO;
    }

    @PostMapping("/write")
    public DataVO getDealWrite(
            @ModelAttribute("data") DealVO dealVO,
            @RequestParam(value = "file", required = false) MultipartFile[] files) {
        DataVO dataVO = new DataVO();
        try {
            // UUID 생성
            String dealIdx = UUID.randomUUID().toString();
            dealVO.setDealIdx(dealIdx);
            log.info("Generated dealIdx: " + dealIdx);

            // Deal 저장
            int result = dealService.getDealWrite(dealVO);

            if (result > 0) {
                // 파일 업로드 및 DB 저장 처리
                if (files != null && files.length > 0) {
                    log.info("첨부된 파일 개수: " + files.length);
                    int maxFiles = 5;
                    int storedFiles = 0;

                    for (MultipartFile file : files) {
                        if (storedFiles >= maxFiles) break;

                        if (!file.isEmpty()) {
                            String uuid = UUID.randomUUID().toString();
                            String fileName = uuid + "_" + file.getOriginalFilename();
                            log.info("생성된 파일명: " + fileName);

                            FileVo fileVo = FileVo.builder()
                                .fileTableType("2")
                                .fileTableIdx(dealIdx)
                                .fileName(fileName)
                                .fileOrder(storedFiles)
                                .build();

                            String path = "D:\\upload\\deal";
                            File upLoadDir = new File(path);

                            if (!upLoadDir.exists()) {
                                upLoadDir.mkdirs();
                            }

                            try {
                                file.transferTo(new File(upLoadDir, fileName));
                                log.info("파일 업로드 완료: " + fileName);
                                log.info("Inserting file: {}", fileVo);
                                dealService.getIDealFileInsert(fileVo);
                                storedFiles++;
                            } catch (Exception e) {
                                log.error("파일 업로드 중 오류 발생: " + e.getMessage());
                                continue;
                            }
                        }
                    }
                }
                dataVO.setSuccess(true);
                dataVO.setMessage("상품등록 완료");
                dataVO.setData(dealVO.getDealIdx());
            } else {
                dataVO.setSuccess(false);
                dataVO.setMessage("상품등록 실패");
            }
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("상품등록 중 오류가 발생했습니다");
            log.error("상품등록 오류: ", e);
        }
        return dataVO;
    }

    @PutMapping("/update/{dealIdx}")
    public DataVO getDealUpdate(
        @PathVariable("dealIdx") String dealIdx,
        @ModelAttribute DealVO dealVO,
        @RequestParam(value = "file", required = false) MultipartFile[] files,
        @RequestParam(value = "deletedFiles", required = false) String deletedFilesJson) 
    {
        dealVO.setDealIdx(dealIdx);
        DataVO dataVO = new DataVO();

        try {
            // 유효성 검사
            if (dealVO.getDealTitle() == null || dealVO.getDealCategory() == null || 
                dealVO.getDealStatus() == null || dealVO.getDealDescription() == null || 
                dealVO.getDealPrice() == null || dealVO.getDealPackage() == null || 
                dealVO.getDealDirect() == null || dealVO.getDealDirectContent() == null || 
                dealVO.getDealCount() == null) 
            {
                dataVO.setSuccess(false);
                dataVO.setMessage("필수 항목이 누락되었습니다.");
                return dataVO;
            }

            // 삭제할 파일 처리
            if (deletedFilesJson != null && !deletedFilesJson.isEmpty()) {
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
                // 파일 순서 재정렬
                dealService.getDealFileUpdate(dealIdx);
            }

            DataVO result = dealService.getDealUpdate(dealVO, files);
            
            if (result.isSuccess()) {
                dataVO.setSuccess(true);
                dataVO.setMessage("상품 수정 완료");
            } else {
                dataVO.setSuccess(false);
                dataVO.setMessage("상품 수정 실패");
            }
        } catch (Exception e) {
            log.error("캠핑마켓 수정 오류: ", e);
            dataVO.setSuccess(false);
            dataVO.setMessage("상품 수정 오류");
        }
        return dataVO;
    }

}