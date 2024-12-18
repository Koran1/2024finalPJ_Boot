package com.ict.finalpj.domain.user.service;

import com.ict.finalpj.domain.user.vo.SocialVO;
import com.ict.finalpj.domain.user.vo.UserVO;

public interface UserService {
    public UserVO getUserInfoById(String userId);
    public UserVO getUserInfoByPhone(String userPhone);
    public UserVO getUserInfoByNickname(String userNickname);
    public UserVO getUserInfoByMail(String userMail);
    public UserVO findUserId(UserVO uvo);
    public UserVO findUserPw(UserVO uvo);
    public int changeUserPw(UserVO uvo);
    int insertUserInfo(UserVO uvo);

    public UserVO getUserInfoByKakaoId(String k_userId);
    public UserVO getUserInfoByNaverId(String n_userId);

    public int insertSocialData(SocialVO sovo);
    public SocialVO getSocialData(String socialIdx);

    public int updateUserNaverId(UserVO uvo);
    public int updateUserKakaoId(UserVO uvo);

    public int updateConnRegByIdx(String userIdx);
}
