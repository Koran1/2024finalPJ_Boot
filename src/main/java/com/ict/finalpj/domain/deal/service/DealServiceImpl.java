package com.ict.finalpj.domain.deal.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.domain.deal.mapper.DealMapper;
import com.ict.finalpj.domain.deal.vo.DealVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DealServiceImpl implements DealService {

    @Autowired
    private DealMapper dealMapper;

    @Override
    public List<DealVO> getDealMainList() {
        return dealMapper.getDealMainList();
    }

    @Override
    public DealVO getDealDetail(String dealIdx) {
        log.info("getDealDetail 호출됨: dealIdx={}", dealIdx); // 로그 추가
        DealVO deal = dealMapper.getDealDetail(dealIdx);
        if (deal == null) {
            log.warn("해당 dealIdx에 대한 DealVO를 찾을 수 없습니다: {}", dealIdx);
        } else {
            log.info("DealVO 조회 성공: {}", deal);
        }
        return deal;
    }

    @Override
    @Transactional
    public DataVO getDealWrite(DealVO dealVO, MultipartFile[] files) {
        DataVO dataVO = new DataVO();
        
        // Deal 저장
        int result = dealMapper.getDealWrite(dealVO);
        
        // dealIdx가 설정된 후에 로그 출력
        log.info("dealIdx: " + dealVO.getDealIdx()); // 로그 추가

        if (result > 0) {
            dataVO.setSuccess(true);
            dataVO.setMessage("상품등록 완료");
            dataVO.setData(dealVO.getDealIdx()); // dealIdx를 응답 데이터에 설정

            // 파일 업로드 및 DB 저장 처리는 이제 컨트롤러에서 처리됩니다.
            // ... existing code ...
        } else {
            dataVO.setSuccess(false);
            dataVO.setMessage("상품등록 실패");
        }

        return dataVO;
    }

    @Override
    public void insertFileInfo(FileVo fileVo) {
        log.info("파일 정보 삽입: " + fileVo.toString()); // 로그 추가
        dealMapper.insertFileInfo(fileVo);
    }

    @Override
    public List<FileVo> getPjFileByDealIdx(String dealIdx) {
        return dealMapper.getPjFileByDealIdx(dealIdx);
    }

    @Override
    @Transactional
    public DataVO updateDeal(DealVO dealVO, MultipartFile[] files) {
        DataVO dataVO = new DataVO();
        try {
            log.info("Updating deal with dealIdx: {}", dealVO.getDealIdx());
            int result = dealMapper.updateDeal(dealVO);
            if (result > 0) {
                // 기존 파일 삭제 로직만 유지
                if (files != null && files.length > 0) {
                    // 기존 파일 삭제
                    List<FileVo> existingFiles = dealMapper.getPjFileByDealIdx(dealVO.getDealIdx());
                    for (FileVo file : existingFiles) {
                        // 파일 시스템에서 삭제
                        File deleteFile = new File("D:\\upload\\deal", file.getFileName());
                        if (deleteFile.exists()) {
                            deleteFile.delete();
                        }
                        // DB에서 파일 정보 삭제 (필요 시 추가)
                        // dealMapper.deleteFileInfo(file.getFileIdx());
                    }

                    // 파일 업로드 및 DB 저장은 컨트롤러에서 처리
                }
                dataVO.setSuccess(true);
                dataVO.setMessage("상품 수정 완료");
            } else {
                dataVO.setSuccess(false);
                dataVO.setMessage("상품 수정 실패");
            }
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("상품 수정 중 오류 발생");
            log.error("상품 수정 오류: ", e);
        }
        return dataVO;
    }

    @Override
    public DealVO getDealManagement(String userIdx) {
        return dealMapper.getDealManagement(userIdx);
    }
}