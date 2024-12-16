package com.ict.finalpj.domain.user.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ict.finalpj.domain.user.mapper.UserMapper;
import com.ict.finalpj.domain.user.vo.UserVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MyUserDetailService implements UserDetailsService{
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserVO uvo = userMapper.getUserInfoById(userId);

        if(uvo == null){
            throw new UnsupportedOperationException("Error : userId is not Found!");
        }
        return new User(uvo.getUserId(), uvo.getUserPw(), new ArrayList<>());
    }

    
    
}
