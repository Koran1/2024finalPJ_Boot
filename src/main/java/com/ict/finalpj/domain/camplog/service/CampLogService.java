package com.ict.finalpj.domain.camplog.service;

import java.util.List;

import com.ict.finalpj.common.vo.ReportVO;
import com.ict.finalpj.domain.camplog.vo.CampLogCommentVO;
import com.ict.finalpj.domain.user.vo.UserVO;

public interface CampLogService {
    public List<CampLogCommentVO> getCommentList(String logIdx);
    public int getCommentWrite(CampLogCommentVO lcvo);
    public int getCommentDelete(String logCommentIdx);
    public int getCommentReport(ReportVO logCommentIdx);
    public List<UserVO> getUserInfoByIdx(List<String> userIdxList);
    public List<ReportVO> getCommentReportCount(List<String> userIdxList);
}
