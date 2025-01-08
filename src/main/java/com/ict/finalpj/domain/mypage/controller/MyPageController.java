package com.ict.finalpj.domain.mypage.controller;

import java.io.File;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
            log.info("uvo(비밀번호 확인) : " + uvo);
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
        public DataVO changeUserInfo(
            @ModelAttribute UserVO uvo,
            @RequestParam(value = "userProfileImg", required = false) MultipartFile userProfileImg
        ) {
            DataVO dvo = new DataVO();
            try {
                log.info("유저 정보 변경 : "+ uvo);
                log.info("유저 프사 변경 : "+ userProfileImg);

                // 파일 처리
                // 파일 처리
                if(userProfileImg != null && !userProfileImg.isEmpty()) {
                    String filename = UUID.randomUUID().toString() + "_" + userProfileImg.getOriginalFilename();
                    
                    // 파일 업로드
                    String path = "D:\\upload\\user";
                    File upLoadDir = new File(path);

                    if (!upLoadDir.exists()) {
                        upLoadDir.mkdirs();
                    }
                    try {
                        userProfileImg.transferTo(new File(upLoadDir, filename));
                        log.info("파일 업로드 완료: " + filename);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    uvo.setUserEtc01(filename);
                }

                int result = userService.changeUserInfo(uvo);
                if(result > 0) {
                    dvo.setData(uvo);
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
