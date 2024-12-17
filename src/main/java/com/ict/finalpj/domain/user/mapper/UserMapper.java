package com.ict.finalpj.domain.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ict.finalpj.domain.user.vo.UserVO;

@Mapper
public interface UserMapper {
  public int getUserJoin(UserVO userVO);
  public UserVO getUserInfo(String name);
  public UserVO findUserByProvider(UserVO userVO);
  public int insertUser(UserVO userVO);
}
