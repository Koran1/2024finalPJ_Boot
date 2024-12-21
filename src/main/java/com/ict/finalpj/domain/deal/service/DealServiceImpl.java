package com.ict.finalpj.domain.deal.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

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
        return dealMapper.getDealDetail(dealIdx);
    }

    @Override
    @Transactional
    public DataVO getDealWrite(DealVO dealVO) {
        DataVO dataVO = new DataVO();
        
        // Deal 저장
        int result = dealMapper.getDealWrite(dealVO);
        
        // dealIdx가 설정된 후에 로그 출력
        log.info("dealIdx: " + dealVO.getDealIdx()); // 로그 추가

        if (result > 0) {
            dataVO.setSuccess(true);
            dataVO.setMessage("상품등록 완료");
            dataVO.setData(dealVO.getDealIdx()); // dealIdx를 응답 데이터에 설정
        } else {
            dataVO.setSuccess(false);
            dataVO.setMessage("상품등록 실패");
        }

        return dataVO;
    }

    @Override
    public void insertFileInfo(FileVo fileVo) {
        log.info("Inserting file info: " + fileVo.toString()); // 로그 추가
        dealMapper.insertFileInfo(fileVo);
    }

    @Override
    public List<FileVo> getPjFileByDealIdx(String dealIdx) {
        return dealMapper.getPjFileByDealIdx(dealIdx);
    }

    @Override
    @Transactional
    public DataVO getDealUpdate(DealVO dealVO, MultipartFile[] files) {
        DataVO dataVO = new DataVO();
        try {
            // Deal 정보 업데이트
            int result = dealMapper.getDealUpdate(dealVO);
            if (result > 0) {
                // 기존 파일 삭제 또는 업데이트 로직 추가 가능
                // 새로운 파일 업로드 로직
                if (files != null && files.length > 0) {
                    // 파일 처리 로직 (예: 기존 파일 삭제 및 새 파일 업로드)
                    List<FileVo> existingFiles = dealMapper.getPjFileByDealIdx(dealVO.getDealIdx());
                    for (FileVo file : existingFiles) {
                        // 파일 삭제 로직 (파일 시스템에서 삭제)
                        File deleteFile = new File("D:\\upload\\deal", file.getFileName());
                        if (deleteFile.exists()) {
                            deleteFile.delete();
                        }
                        // DB에서 파일 정보 삭제
                        // dealMapper.deleteFileInfo(file.getFileIdx()); // deleteFileInfo 메서드 필요 시 추가
                    }

                    // 새로운 파일 업로드
                    int maxFiles = 6;
                    int storedFiles = 0;
                    for (MultipartFile file : files) {
                        if (storedFiles >= maxFiles) {
                            break;
                        }
                        if (!file.isEmpty()) {
                            FileVo fileVo = new FileVo();
                            UUID uuid = UUID.randomUUID();
                            String fileName = uuid.toString() + "_" + file.getOriginalFilename();
                            fileVo.setFileTableType("2");
                            fileVo.setFileTableIdx(dealVO.getDealIdx());
                            fileVo.setFileName(fileName);
                            fileVo.setFileOrder(storedFiles);

                            String path = "D:\\upload\\deal";
                            File upLoadDir = new File(path);
                            if (!upLoadDir.exists()) {
                                upLoadDir.mkdirs();
                            }

                            try {
                                file.transferTo(new File(upLoadDir, fileName));
                            } catch (Exception e) {
                                log.error("파일 업로드 중 오류 발생: " + e.getMessage());
                                continue;
                            }

                            dealMapper.insertFileInfo(fileVo);
                            storedFiles++;
                        }
                    }

                    if (files.length > maxFiles) {
                        log.warn("최대 " + maxFiles + "개의 파일만 업로드됩니다. 초과된 파일은 무시됩니다.");
                    }
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
}