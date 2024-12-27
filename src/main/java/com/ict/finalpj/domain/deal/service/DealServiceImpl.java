package com.ict.finalpj.domain.deal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.common.vo.ViewsVO;
import com.ict.finalpj.domain.deal.mapper.DealMapper;
import com.ict.finalpj.domain.deal.mapper.FavoriteMapper;
import com.ict.finalpj.domain.deal.vo.DealFavoriteVO;
import com.ict.finalpj.domain.deal.vo.DealVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DealServiceImpl implements DealService {

    @Autowired
    private DealMapper dealMapper;

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Override
    public List<DealVO> getDealMainList() {
        return dealMapper.getDealMainList();
    }
    
    @Override
    public FileVo getDealFileOne(String fileTableIdx) {
        return dealMapper.getDealFileOne(fileTableIdx);
    }

    @Override
    public DealVO getDealDetail(String dealIdx) {
        return dealMapper.getDealDetail(dealIdx);
    }
    
    @Override
    public List<FileVo> getDealFileDetail(String fileTableIdx) {
        return dealMapper.getDealFileDetail(fileTableIdx);
    }

    @Override
    public int getDealWrite(DealVO dealVO) {
        return dealMapper.getDealWrite(dealVO);
    }

    @Override
    public int getIDealFileInsert(FileVo fileVo) {
        return dealMapper.getIDealFileInsert(fileVo);
    }

    @Override
    public int getDealUpdate(DealVO dealVO) {
        return dealMapper.getDealUpdate(dealVO);
    }

    @Override
    public int getDealFileUpdate(FileVo fileVo) {
        return dealMapper.getDealFileUpdate(fileVo);
    }

    @Override
    public int getDealFileDelete(FileVo fileVo) {
        return dealMapper.getDealFileDelete(fileVo);
    }
    
    @Override
    public List<DealVO> getDealManagement(String userIdx) {
        return dealMapper.getDealManagement(userIdx);
    }

    @Override
    public List<DealFavoriteVO> getDealinterest(String userIdx) {
        return dealMapper.getDealinterest(userIdx);
    } 

    @Override
    public int getDealFileNameDelete(String fileTableIdx, String fileName) {
        return dealMapper.getDealFileNameDelete(fileTableIdx, fileName);
    }

    @Override
    public int getDealFileOrder(FileVo fileVo) {
        return dealMapper.getDealFileOrder(fileVo);
    }

    // 좋아요 상태 확인
    @Override
    public boolean isLiked(String userIdx, String dealIdx) {
        return favoriteMapper.isLiked(userIdx, dealIdx);
    }

    // 좋아요 추가
    @Override
    public int likeDeal(String userIdx, String dealIdx) {
        return favoriteMapper.likeDeal(userIdx, dealIdx);
    }

    // 좋아요 삭제
    @Override
    public int unlikeDeal(String userIdx, String dealIdx) {
        return favoriteMapper.unlikeDeal(userIdx, dealIdx);
    }

    // 조회수 관련 메서드 구현
    @Override
    public ViewsVO getViewCount(String userIdx, String dealIdx) {
        return favoriteMapper.getViewCount(userIdx, dealIdx);
    }

    @Override
    public int insertViewCount(String userIdx, String dealIdx) {
        return favoriteMapper.insertViewCount(userIdx, dealIdx);
    }

    @Override
    public int updateViewCount(String userIdx, String dealIdx) {
        return favoriteMapper.updateViewCount(userIdx, dealIdx);
    }
}