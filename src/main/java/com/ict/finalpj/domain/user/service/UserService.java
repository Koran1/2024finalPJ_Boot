package com.ict.finalpj.domain.user.service;

import com.ict.finalpj.domain.user.vo.UserVO;

public interface UserService {
  public int getUserJoin(UserVO userVO);
  public UserVO getUserInfo(String name);

}
