package com.ict.finalpj.domain.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.domain.admin.mapper.AdminMapper;
import com.ict.finalpj.domain.deal.vo.DealVO;

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

}
