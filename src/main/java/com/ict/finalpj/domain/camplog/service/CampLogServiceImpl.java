package com.ict.finalpj.domain.camplog.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.common.vo.ReportVO;
import com.ict.finalpj.domain.camp.vo.CampVO;
import com.ict.finalpj.domain.camplog.mapper.CampLogMapper;
import com.ict.finalpj.domain.camplog.vo.CampLogCommentVO;
import com.ict.finalpj.domain.camplog.vo.CampLogContentVO;
import com.ict.finalpj.domain.camplog.vo.CampLogListVO;
import com.ict.finalpj.domain.camplog.vo.CampLogVO;
import com.ict.finalpj.domain.camplog.vo.TagInfoVO;
import com.ict.finalpj.domain.deal.vo.DealVO;
import com.ict.finalpj.domain.user.vo.UserVO;

@Service
public class CampLogServiceImpl implements CampLogService{
    @Autowired
    private CampLogMapper campLogMapper;

    // *로그(후기) 내용*
    @Override
    public List<DealVO> getDealListByuserIdx(String userIdx) {
        return campLogMapper.getDealListByuserIdx(userIdx) ;
    }

    @Override
    public String[] getFileNamesByDealIdxes(List<String> dealIdxes) {
        return campLogMapper.getFileNamesByDealIdxes(dealIdxes);
    }
    @Override
    public String getFileNamesByDealIdx(String dealIdx) {
        return campLogMapper.getFileNamesByDealIdx(dealIdx);
    }
    @Override
    public List<CampVO> getCampListAll() {
        return campLogMapper.getCampListAll();
    }

    @Override
    public int insertToPjcamplog(CampLogVO cvo) {
        return campLogMapper.insertToPjcamplog(cvo);
    }

    @Override
    public int insertToPjlogcontent(CampLogContentVO cvto) {
        return campLogMapper.insertToPjlogcontent(cvto);
    }

    @Override
    public int insertToPjfile(FileVo fvo) {
        return campLogMapper.insertToPjfile(fvo);
    }

    @Override
    public int insertToPjtaginfo(TagInfoVO tvo) {
        return campLogMapper.insertToPjtaginfo(tvo);
    }

    @Override
    public CampLogVO getLogDetailByLogIdx(String logIdx) {
        return campLogMapper.getLogDetailByLogIdx( logIdx);
    }

    @Override
    public List<CampLogContentVO> getLogContentByLogIdx(String logIdx) {
        return campLogMapper.getLogContentByLogIdx(logIdx);
    }

    @Override
    public int isUserRemommend(String logIdx, String userIdx) {
        Map<String, String> map = new HashMap<>();
        map.put("logIdx", logIdx);
        map.put("userIdx", userIdx);
        return campLogMapper.isUserRemommend(map);
    }

    @Override
    public List<FileVo> getLogFileByLogIdx(String logIdx) {
        return campLogMapper.getLogFileByLogIdx(logIdx);
    }

    @Override
    public List<TagInfoVO> getLogTagByLogIdx(String logIdx) {
        return campLogMapper.getLogTagByLogIdx(logIdx);
    }

    @Override
    public List<DealVO> getDealList() {
        return campLogMapper.getDealList();
    }

    @Override
    public UserVO getUserDataByUserIdx(String userIdx) {
        return campLogMapper.getUserDataByUserIdx(userIdx);
    }
    @Override
    public int toogleOff(Map<String, String> map) {
        return campLogMapper.toogleOff(map);
    }

    @Override
    public int toogleOn(Map<String, String> map) {
        return campLogMapper.toogleOn(map);
    }

    @Override
    public int getLogActiveZero(String logIdx) {
        return campLogMapper.getLogActiveZero(logIdx);
    }

    @Override
    public int countLogRecommend(String logIdx) {
        return campLogMapper.countLogRecommend(logIdx);
    }

    @Override
    public String getFacltNmByCampIdx(String campIdx) {
        return campLogMapper.getFacltNmByCampIdx(campIdx);
    }
    @Override
    public int updateToPjcamplog(CampLogVO lvo) {
        return campLogMapper.updateToPjcamplog(lvo);
    }

    @Override
    public int deleteLogContentByLogIdx(String logIdx) {
        return campLogMapper.deleteLogContentByLogIdx(logIdx);
    }

    @Override
    public int deleteOldFile(FileVo fvo) {
        return campLogMapper.deleteOrders(fvo);
    }

    @Override
    public int deleteTagByLogIdx(String logIdx) {
        return campLogMapper.deleteTagByLogIdx(logIdx);
    }

    @Override
    public Map<String, Object> getCamplogList(CampLogListVO campLogListVO) {
        int offset = (campLogListVO.getPage() -1) * campLogListVO.getSize();
        campLogListVO.setOffset(offset);

        List<CampLogListVO> camplogList = campLogMapper.getCamplogList(campLogListVO);
        int totalCount = campLogMapper.getCampLogCount(campLogListVO);

        Map<String, Object> result = new HashMap<>();
        result.put("data", camplogList);
        result.put("totalCount", totalCount);
        result.put("totalPages", (int) Math.ceil((double) totalCount / campLogListVO.getSize()));
        return result;
    }
    // 로그 글 신고
    @Override
    public int getLogReport(ReportVO logIdx){
        return campLogMapper.getLogReport(logIdx);
    }

    // 로그 글 신고 횟수
    @Override
    public List<ReportVO> getLogReportCount(String logIdx) {
        return campLogMapper.getLogReportCount(logIdx);
    }


    // *댓글*
    // 댓글 리스트 가져오기
    @Override
    public List<CampLogCommentVO> getCommentList(String logIdx) {
        return campLogMapper.getCommentList(logIdx);
    }

    // 댓글 작성
    @Override
    public int getCommentWrite(CampLogCommentVO lcvo) {
        return campLogMapper.getCommentWrite(lcvo);
    }

    // 댓글 삭제
    @Override
    public int getCommentDelete(String logCommentIdx) {
        return campLogMapper.getCommentDelete(logCommentIdx);
    }

    // 댓글 신고
    @Override
    public int getCommentReport(ReportVO logCommentIdx) {
        return campLogMapper.getCommentReport(logCommentIdx);
    }

    // 유저 정보 가져오기
    @Override
    public List<UserVO> getUserInfoByIdx(List<String> userIdxList) {
        return campLogMapper.getUserInfoByIdx(userIdxList);
    }

    // 댓글 신고 횟수
    @Override
    public List<ReportVO> getCommentReportCount(List<String> userIdxList) {
        return campLogMapper.getCommentReportCount(userIdxList);
    }

    @Override
    public List<CampLogCommentVO> getMyComments(String userIdx) {
        return campLogMapper.getMyComments(userIdx);
    }
    
}
