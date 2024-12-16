package com.ict.finalpj.domain.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalpj.domain.user.mapper.UserMapper;
import com.ict.finalpj.domain.user.vo.UserVO;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserVO getUserInfoById(String userId) {
        return userMapper.getUserInfoById(userId);
    }

    @Override
    public int insertUserInfo(UserVO uvo) {
        return userMapper.insertUserInfo(uvo);
    }

    @Override
    public UserVO getUserInfoByPhone(String userPhone) {
        return userMapper.getUserInfoByPhone(userPhone);
    }


    @Override
    public UserVO getUserInfoByNickname(String userNickname) {
        return userMapper.getUserInfoByNickname(userNickname);
    }

    @Override
    public UserVO findUserId(UserVO uvo) {
        return userMapper.findUserId(uvo);
    }

    @Override
    public UserVO findUserPw(UserVO uvo) {
        return userMapper.findUserId(uvo);
    }

    @Override
    public int changeUserPw(UserVO uvo) {
        return userMapper.changeUserPw(uvo);
    }

}
