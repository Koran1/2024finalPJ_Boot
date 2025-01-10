package com.ict.finalpj.domain.camplog.service;

import java.util.List;
import java.util.Map;

import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.common.vo.ReportVO;
import com.ict.finalpj.domain.camp.vo.CampVO;
import com.ict.finalpj.domain.camplog.vo.CampLogCommentVO;
import com.ict.finalpj.domain.camplog.vo.CampLogContentVO;
import com.ict.finalpj.domain.camplog.vo.CampLogListVO;
import com.ict.finalpj.domain.camplog.vo.CampLogVO;
import com.ict.finalpj.domain.camplog.vo.TagInfoVO;
import com.ict.finalpj.domain.deal.vo.DealVO;
import com.ict.finalpj.domain.user.vo.UserVO;

public interface CampLogService {
    // *로그(후기) 내용
    List<DealVO> getDealListByuserIdx(String userIdx);
    String[] getFileNamesByDealIdxes(List<String> dealIdx);
    String getFileNamesByDealIdx(String  dealIdx);
    List<CampVO> getCampListAll();
    int insertToPjcamplog(CampLogVO cvo);
    int insertToPjlogcontent(CampLogContentVO cvto);
    int insertToPjfile(FileVo fvo);
    int insertToPjtaginfo(TagInfoVO tvo);
    CampLogVO getLogDetailByLogIdx(String logIdx);
    List<CampLogContentVO> getLogContentByLogIdx(String logIdx);
    int isUserRemommend(String logIdx, String userIdx);
    List<FileVo> getLogFileByLogIdx(String logIdx);
    List<TagInfoVO> getLogTagByLogIdx(String logIdx);
    List<DealVO> getDealList();
    UserVO getUserDataByUserIdx(String userIdx);
    int toogleOff(Map<String, String> map);
    int toogleOn(Map<String, String> map);
    int getLogActiveZero(String logIdx);
    int countLogRecommend(String logIdx);
    String getFacltNmByCampIdx(String campIdx);
    int updateToPjcamplog(CampLogVO lvo);
    int deleteLogContentByLogIdx(String logIdx);
    int deleteOldFile(FileVo fvo);
    int deleteTagByLogIdx(String logIdx);
    public int getLogReport(ReportVO logIdx); // 로그 글 신고
    public List<ReportVO> getLogReportCount(String logIdx); // 로그 글 신고 횟수
    int updateLinkeCamp( Map<String, String> map);
    int logUpdateDatetoNow(String logIdx);

    // 캠프로그 리스트 
    Map<String, Object> getCamplogList(CampLogListVO campLogListVO);

    // *댓글*
    public List<CampLogCommentVO> getCommentList(String logIdx);
    public int getCommentWrite(CampLogCommentVO lcvo);
    public int getCommentDelete(String logCommentIdx);
    public int getCommentReport(ReportVO logCommentIdx);
    public List<UserVO> getUserInfoByIdx(List<String> userIdxList);
    public List<ReportVO> getCommentReportCount(List<String> userIdxList);

    // 내가 쓴 댓글
    List<CampLogCommentVO> getMyComments(String userIdx);
}
