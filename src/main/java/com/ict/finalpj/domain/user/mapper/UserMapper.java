package com.ict.finalpj.domain.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ict.finalpj.domain.user.vo.UserVO;

@Mapper
public interface UserMapper {
    UserVO getUserInfoById(String userId);
    UserVO getUserInfoByPhone(String userPhone);
    UserVO getUserInfoByNickname(String userNickname);
    UserVO findUserId(UserVO uvo);
    UserVO findUserPw(UserVO uvo);
    int changeUserPw(UserVO uvo);
    int insertUserInfo(UserVO uvo);

}
