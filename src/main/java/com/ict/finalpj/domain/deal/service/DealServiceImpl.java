package com.ict.finalpj.domain.deal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public int getDealUpdate(DealVO dealVO) {
        return dealMapper.getDealUpdate(dealVO);
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
}