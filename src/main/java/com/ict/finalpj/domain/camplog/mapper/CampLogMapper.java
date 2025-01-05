package com.ict.finalpj.domain.camplog.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ict.finalpj.common.vo.FileVO;
import com.ict.finalpj.domain.camp.vo.CampVO;
import com.ict.finalpj.domain.camplog.vo.CampLogContentVO;
import com.ict.finalpj.domain.camplog.vo.CampLogVO;
import com.ict.finalpj.domain.camplog.vo.TagInfoVO;
import com.ict.finalpj.domain.deal.vo.DealVO;
import com.ict.finalpj.domain.user.vo.UserVO;

@Mapper
public interface CampLogMapper {
    List<DealVO> getDealListByuserIdx(String userIdx);
    String[] getFileNamesByDealIdxes(List<String> dealIdx);
    String getFileNamesByDealIdx(String dealIdx);
    List<CampVO> getCampListAll();
    int insertToPjcamplog(CampLogVO cvo);
    int insertToPjlogcontent(CampLogContentVO cvto);
    int insertToPjfile(FileVO fvo);
    int insertToPjtaginfo(TagInfoVO tvo);
    CampLogVO getLogDetailByLogIdx(String logIdx);
    List<CampLogContentVO> getLogContentByLogIdx(String logIdx);
    int isUserRemommend(Map<String, String> map);
    List<FileVO> getLogFileByLogIdx(String logIdx);
    List<TagInfoVO> getLogTagByLogIdx(String logIdx);
    List<DealVO> getDealList();
    UserVO getUserDataByUserIdx(String userIdx);
    String[] getFileNamesBydealIdxes(List<String> dealIdxes);
    int toogleOff(Map<String, String> map );
    int toogleOn(Map<String, String> map );
    int getLogActiveZero(String logIdx);
}
