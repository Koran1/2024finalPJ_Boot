package com.ict.finalpj.domain.user.service;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.ict.finalpj.domain.user.mapper.UserMapper;
import com.ict.finalpj.domain.user.vo.SocialVO;
import com.ict.finalpj.domain.user.vo.UserVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MyUserDetailService implements UserDetailsService{
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userIdx) throws UsernameNotFoundException {
        UserVO uvo = userMapper.getUserInfoByIdx(userIdx);

        if(uvo == null){
            throw new UnsupportedOperationException("Error : userIdx is not Found!");
        }
        return new User(uvo.getUserIdx(), uvo.getUserPw(), new ArrayList<>());
    }

    // DB에서 정보 있는지 확인
    public String loadUserByOAuth2User(OAuth2User oAuth2User, String provider){
        @SuppressWarnings("null")
        String id = oAuth2User.getAttribute("id").toString();
        String name = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");
        
        UserVO uvo_true = new UserVO();
        SocialVO sovo = new SocialVO();
        String socialIdx = UUID.randomUUID().toString();

        // kakao 로 로그인 한 적 있는지 확인
        if(provider.equals("kakao")){
            uvo_true = userMapper.getUserInfoByKakaoId(id);
            if(uvo_true == null){
                sovo.setSocialIdx(socialIdx);
                sovo.setSocialId(id);
                sovo.setSocialName(name);
                sovo.setSocialEmail(email);
                userMapper.insertSocialData(sovo);

                return socialIdx;
            }
        }
        
        // naver 로 로그인 한적 있는지 확인
        if(provider.equals("naver")){
            uvo_true = userMapper.getUserInfoByNaverId(id);
            if(uvo_true == null){
                String phone = oAuth2User.getAttribute("phone");
                sovo.setSocialIdx(socialIdx);
                sovo.setSocialId(id);
                sovo.setSocialName(name);
                sovo.setSocialEmail(email);
                sovo.setSocialPhone(phone);
                userMapper.insertSocialData(sovo);
                return socialIdx;
            }
        }

        return null;
    }

    // DB에서 정보 있는지 확인
    public String getUserIdxByPhone(OAuth2User oAuth2User){
        @SuppressWarnings("null")
        String phone = oAuth2User.getAttribute("phone").toString();

        UserVO uvo_true = userMapper.getUserInfoByPhone(phone);
        if(uvo_true == null){
            return null;
        }else{
            UserVO uvo = new UserVO();
            uvo.setN_userId(oAuth2User.getAttribute("id"));
            uvo.setUserPhone(phone);
            userMapper.updateUserNaverId(uvo);

            return uvo_true.getUserIdx();
        }
    }
    
    // DB에서 정보 있는지 확인
    public UserVO getUserInfoByOAuth2User(OAuth2User oAuth2User, String provider){
        @SuppressWarnings("null")
        String id = oAuth2User.getAttribute("id").toString();
        UserVO uvo_true = new UserVO();

        if(provider.equals("kakao")){
            uvo_true = userMapper.getUserInfoByKakaoId(id);
        }
        
        if(provider.equals("naver")){
            uvo_true = userMapper.getUserInfoByNaverId(id);
        }
        return uvo_true;
    }



    
    
}
