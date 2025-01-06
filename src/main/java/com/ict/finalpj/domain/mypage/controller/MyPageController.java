package com.ict.finalpj.domain.mypage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.domain.user.service.UserService;
import com.ict.finalpj.domain.user.vo.UserVO;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/api/mypage")
public class MyPageController {

    @Autowired
    private UserService userService;

    private final PasswordEncoder passwordEncoder;

    public MyPageController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/checkPw")
    public DataVO checkPw(@RequestBody UserVO uvo) {
        DataVO dvo = new DataVO();
        try {
            log.info("uvo : " + uvo);
            UserVO uvo_true = userService.getUserInfoByIdx(uvo.getUserIdx());
            if(passwordEncoder.matches(uvo.getUserPw(), uvo_true.getUserPw())) {
                dvo.setSuccess(true);
                dvo.setMessage("비밀번호 일치");
            } else {
                dvo.setSuccess(false);
                dvo.setMessage("비밀번호 불일치");
            }
        } catch (Exception e) {
            e.printStackTrace();
            dvo.setSuccess(false);
            dvo.setMessage("비밀번호 조회 오류");
        }
        return dvo;
    }
    
    @PostMapping("/getUserInfo")
        public DataVO getUserInfo(@RequestBody UserVO uvo) {
            DataVO dvo = new DataVO();
            try {
                log.info("유저 정보 가져오기 : " + uvo);
                UserVO uvo_true = userService.getUserInfoByIdx(uvo.getUserIdx());

                uvo_true.setUserPw("");
                if(uvo_true != null) {
                    dvo.setData(uvo_true);
                    dvo.setSuccess(true);
                    dvo.setMessage("유저 정보 조회 성공");
                } else {
                    dvo.setSuccess(false);
                    dvo.setMessage("유저 정보 조회 실패");
                }
            } catch (Exception e) {
                e.printStackTrace();
                dvo.setSuccess(false);
                dvo.setMessage("유저 정보 조회 오류");
            }
            return dvo;
        }

        @PostMapping("/changePw")
        public DataVO changePw(@RequestBody UserVO uvo) {
            DataVO dvo = new DataVO();
            try {
                log.info("유저 정보 가져오기 : " + uvo);
                
                // 비밀번호 암호화
                uvo.setUserPw(passwordEncoder.encode(uvo.getUserPw()));
                int result = userService.changeUserPw(uvo);
                if(result > 0) {
                    dvo.setSuccess(true);
                    dvo.setMessage("비밀번호 변경 성공");
                } else {
                    dvo.setSuccess(false);
                    dvo.setMessage("비밀번호 변경 실패");
                }
            } catch (Exception e) {
                e.printStackTrace();
                dvo.setSuccess(false);
                dvo.setMessage("비밀번호 변경 오류");
            }
            return dvo;
        }

        @PutMapping("/changeUserInfo")
        public DataVO changeUserInfo(@RequestBody UserVO uvo) {
            DataVO dvo = new DataVO();
            try {
                log.info("유저 정보 변경 : "+ uvo);
                int result = userService.changeUserInfo(uvo);
                if(result > 0) {
                    dvo.setSuccess(true);
                    dvo.setMessage("유저 정보 변경 성공");
                } else {
                    dvo.setSuccess(false);
                    dvo.setMessage("유저 정보 변경 실패");
                }
            } catch (Exception e) {
                e.printStackTrace();
                dvo.setSuccess(false);
                dvo.setMessage("유저 정보 변경 오류");
            }
            return dvo;
        }
}
