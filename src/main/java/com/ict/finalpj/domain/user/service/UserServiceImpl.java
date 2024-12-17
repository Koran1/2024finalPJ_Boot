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
  public int getUserJoin(UserVO userVO) {
    return userMapper.getUserJoin(userVO);
  }

  @Override
  public UserVO getUserInfo(String name) {
    return userMapper.getUserInfo(name);
  }
}
