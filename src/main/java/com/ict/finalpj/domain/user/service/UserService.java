package com.ict.finalpj.domain.user.service;

import com.ict.finalpj.domain.user.vo.UserVO;

public interface UserService {
    public UserVO getUserInfoById(String userId);
    public UserVO getUserInfoByPhone(String userPhone);
    public UserVO getUserInfoByNickname(String userNickname);
    public UserVO findUserId(UserVO uvo);
    public UserVO findUserPw(UserVO uvo);
    public int changeUserPw(UserVO uvo);
    int insertUserInfo(UserVO uvo);

}
