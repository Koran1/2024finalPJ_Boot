package com.ict.finalpj.domain.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalpj.domain.user.mapper.UserMapper;

import com.ict.finalpj.domain.user.vo.SocialVO;
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
        return userMapper.findUserPw(uvo);
    }

    @Override
    public int changeUserPw(UserVO uvo) {
        return userMapper.changeUserPw(uvo);
    }

    @Override
    public UserVO getUserInfoByKakaoId(String k_userId) {
        return userMapper.getUserInfoByKakaoId(k_userId);
    }

    @Override
    public UserVO getUserInfoByNaverId(String n_userId) {
        return userMapper.getUserInfoByNaverId(n_userId);
    }

    @Override
    public int insertSocialData(SocialVO sovo) {
        return userMapper.insertSocialData(sovo);
    }

    @Override
    public SocialVO getSocialData(String socialIdx) {
        return userMapper.getSocialData(socialIdx);
    }

    @Override
    public UserVO getUserInfoByMail(String userMail) {
        return userMapper.getUserInfoByMail(userMail);
    }

    @Override
    public int updateUserNaverId(UserVO uvo) {
        return userMapper.updateUserNaverId(uvo);
    }

    @Override
    public int updateUserKakaoId(UserVO uvo) {
        return userMapper.updateUserKakaoId(uvo);
    }

    @Override
    public int updateConnRegByIdx(String userIdx) {
        return userMapper.updateConnRegByIdx(userIdx);
    }

    // 오류나서 잠시 주석
    // @Override
    // public UserVO getUserInfo(String name) {
    // return userMapper.getUserInfo(name);
    // }
}
