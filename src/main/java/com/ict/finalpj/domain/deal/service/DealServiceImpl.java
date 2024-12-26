package com.ict.finalpj.domain.deal.service;

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
    return dealMapper.getDealWrite(dealVO, files); 
  }

  @Override
  public List<FileVo> listFile(String fileTableIdx) {
    return dealMapper.listFile(fileTableIdx);
  }

  @Override
  public int saveFile(FileVo fileVo) {
    return dealMapper.saveFile(fileVo);
  }

    @Override
    @Transactional
    public DataVO updateDeal(DealVO dealVO, MultipartFile[] files) {
        DataVO dataVO = new DataVO();
        try {
            log.info("Updating deal with dealIdx: {}", dealVO.getDealIdx());
            int result = dealMapper.updateDeal(dealVO);
            if (result > 0) {
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
    public DataVO getDealFileDelete(String fileTableIdx) {
        return dealMapper.getDealFileDelete(fileTableIdx);
    }

  @Override
  public DealVO getDealDetailWithFiles(String dealIdx) {
    return dealMapper.getDealDetailWithFiles(dealIdx);
  } 

    @Override
    public void updateFileInfo(FileVo fileVo) {
        dealMapper.updateFileInfo(fileVo);
    }



    @Override
    public void updateFileInfo(FileVo fileVo) {
        dealMapper.updateFileInfo(fileVo);
    }



@Override
public FileVo getFileVO(String dealIdx) {
    return dealMapper.getFileVO(dealIdx);
}

}
