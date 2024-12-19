package com.ict.finalpj.domain.deal.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.domain.deal.mapper.DealMapper;
import com.ict.finalpj.domain.deal.vo.DealVO;

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
    public int getDealUpdate(DealVO dealVO) {
        return dealMapper.getDealUpdate(dealVO);
    }

    @Override
    public int getDealWrite(DealVO dealVO) {
        int result = dealMapper.getDealWrite(dealVO);
        
        // 파일 정보를 저장하는 로직 추가
        FileVo fileVO = dealVO.getFileVO(); // DealVO에서 FileVo 가져오기
        if (fileVO != null) {
            fileVO.setFileTableIdx(dealVO.getDealIdx());
            dealMapper.insertFile(fileVO);
        }
        
        return result;
    }

    public DataVO saveFile(MultipartFile file, DealVO dealVO) {
        DataVO dataVO = new DataVO();
        try {
            // 파일명 생성
            UUID uuid = UUID.randomUUID();
            String fileName = uuid.toString() + "_" + file.getOriginalFilename();

            // FileVo 설정
            FileVo fileVo = new FileVo();
            fileVo.setFileTableType("2");  // 테이블 타입 설정
            fileVo.setFileTableIdx(dealVO.getDealIdx());  // 테이블 인덱스 설정
            fileVo.setFileName(fileName);
            fileVo.setFileOrder("0");  // 파일 순서 설정 (여기서는 첫 번째 파일로 가정)
            fileVo.setFileActive("1");  // 활성화 상태

            // 파일 업로드 경로
            String path = "D:\\upload\\deal";
            File upLoadDir = new File(path);
            if (!upLoadDir.exists()) {
                upLoadDir.mkdirs();
            }

            // 파일 업로드
            file.transferTo(new File(upLoadDir, fileName));

            // DB에 파일 정보 저장
            dealMapper.insertFile(fileVo);

            dataVO.setSuccess(true);
            dataVO.setMessage("파일 업로드 및 DB 저장 완료");
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("파일 업로드 실패: " + e.getMessage());
        }
        return dataVO;
    }
}
