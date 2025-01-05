package com.ict.finalpj.domain.camplog.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ict.finalpj.common.vo.FileVO;
import com.ict.finalpj.domain.camp.vo.CampVO;
import com.ict.finalpj.domain.camplog.vo.CampLogContentVO;
import com.ict.finalpj.domain.camplog.vo.CampLogVO;
import com.ict.finalpj.domain.camplog.vo.TagInfoVO;
import com.ict.finalpj.domain.deal.vo.DealVO;
import com.ict.finalpj.domain.user.vo.UserVO;

public interface CampLogService {
    List<DealVO> getDealListByuserIdx(String userIdx);
    String[] getFileNamesByDealIdxes(List<String> dealIdx);
    String getFileNamesByDealIdx(String  dealIdx);
    List<CampVO> getCampListAll();
    int insertToPjcamplog(CampLogVO cvo);
    int insertToPjlogcontent(CampLogContentVO cvto);
    int insertToPjfile(FileVO fvo);
    int insertToPjtaginfo(TagInfoVO tvo);
    CampLogVO getLogDetailByLogIdx(String logIdx);
    List<CampLogContentVO> getLogContentByLogIdx(String logIdx);
    int isUserRemommend(String logIdx, String userIdx);
    List<FileVO> getLogFileByLogIdx(String logIdx);
    List<TagInfoVO> getLogTagByLogIdx(String logIdx);
    List<DealVO> getDealList();
    UserVO getUserDataByUserIdx(String userIdx);
    String[] getFileNamesBydealIdxes(List<String> dealIdxes);
    int toogleOff(Map<String, String> map);
    int toogleOn(Map<String, String> map);
    int getLogActiveZero(String logIdx);
}
