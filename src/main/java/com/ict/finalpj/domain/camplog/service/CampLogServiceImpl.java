package com.ict.finalpj.domain.camplog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalpj.common.vo.ReportVO;
import com.ict.finalpj.domain.camplog.mapper.CampLogMapper;
import com.ict.finalpj.domain.camplog.vo.CampLogCommentVO;
import com.ict.finalpj.domain.user.vo.UserVO;

@Service
public class CampLogServiceImpl implements CampLogService{
    @Autowired
    private CampLogMapper campLogMapper;

    @Override
    public List<CampLogCommentVO> getCommentList(String logIdx) {
        return campLogMapper.getCommentList(logIdx);
    }

    @Override
    public int getCommentWrite(CampLogCommentVO lcvo) {
        return campLogMapper.getCommentWrite(lcvo);
    }

    @Override
    public int getCommentDelete(String logCommentIdx) {
        return campLogMapper.getCommentDelete(logCommentIdx);
    }

    @Override
    public int getCommentReport(ReportVO logCommentIdx) {
        return campLogMapper.getCommentReport(logCommentIdx);
    }

    @Override
    public List<UserVO> getUserInfoByIdx(List<String> userIdxList) {
        return campLogMapper.getUserInfoByIdx(userIdxList);
    }

    @Override
    public List<ReportVO> getCommentReportCount(List<String> userIdxList) {
        return campLogMapper.getCommentReportCount(userIdxList);
    }
}
