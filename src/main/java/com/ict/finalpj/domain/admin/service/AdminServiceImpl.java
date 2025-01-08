package com.ict.finalpj.domain.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.domain.admin.mapper.AdminMapper;
import com.ict.finalpj.domain.deal.vo.DealVO;
import com.ict.finalpj.domain.add.vo.QNAVO;
import com.ict.finalpj.domain.camp.vo.CampSearchVO;
import com.ict.finalpj.domain.camp.vo.CampVO;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public List<DealVO> getDealManagement() {
        return adminMapper.getDealManagement();
    }

    @Override
    public DealVO getDealDetail(String dealIdx) {
        return adminMapper.getDealDetail(dealIdx);
    }

    @Override
    public List<FileVo> getDealFiles(String dealIdx) {
        return adminMapper.getDealFiles(dealIdx);
    }

    @Override
    public int getFavoriteCount(String dealIdx) {
        return adminMapper.getFavoriteCount(dealIdx);
    }

    @Override
    public Map<String, Object> getSellerOtherDeals(String dealIdx) {
        Map<String, Object> result = new HashMap<>();
        result.put("deals", adminMapper.getSellerOtherDeals(dealIdx));
        result.put("files", adminMapper.getSellerOtherFiles(dealIdx));
        return result;
    }

    @Override
    public String getDealSatisSellerScore(String userIdx) {
        return adminMapper.getDealSatisSellerScore(userIdx);
    }

    @Override
    public int getAdminDealActiveUpdate(String dealIdx, int dealview) {
        return adminMapper.getAdminDealActiveUpdate(dealIdx, dealview);
    }

    // 캠핑정보 관리
    @Override
    public Map<String, Object> getCampingList(CampSearchVO campSearchVO) {
        int offset = (campSearchVO.getPage() - 1) * campSearchVO.getSize();
        campSearchVO.setOffset(offset);

        List<CampVO> campList = adminMapper.getSearchList(campSearchVO);
        int totalCount = adminMapper.getSearchCount(campSearchVO);

        Map<String, Object> result = new HashMap<>();
        result.put("data", campList);
        result.put("totalCount", totalCount);
        result.put("totalPages", (int) Math.ceil((double) totalCount / campSearchVO.getSize()));
        return result;
    }

    // QNA 관리
    @Override
    public List<QNAVO> getQnaList() {
        return adminMapper.getQnaList();
    }

}
