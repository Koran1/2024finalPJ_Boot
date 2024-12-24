package com.ict.finalpj.domain.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ict.finalpj.domain.user.vo.SocialVO;
import com.ict.finalpj.domain.user.vo.UserVO;

@Mapper
public interface UserMapper {
    UserVO getUserInfoByIdx(String userIdx);
    
    UserVO getUserInfoById(String userId);
    UserVO getUserInfoByPhone(String userPhone);
    UserVO getUserInfoByNickname(String userNickname);
    UserVO getUserInfoByMail(String userMail);
    UserVO findUserId(UserVO uvo);
    UserVO findUserPw(UserVO uvo);
    int changeUserPw(UserVO uvo);
    int insertUserInfo(UserVO uvo);

    UserVO getUserInfoByKakaoId(String k_userId);
    UserVO getUserInfoByNaverId(String n_userId);

    int insertSocialData(SocialVO sovo);
    SocialVO getSocialData(String socialIdx);

    int updateUserNaverId(UserVO uvo);
    int updateUserKakaoId(UserVO uvo);

    int updateConnRegByIdx(String userIdx);

}
