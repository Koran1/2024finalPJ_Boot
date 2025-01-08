package com.ict.finalpj.domain.user.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ict.finalpj.common.util.JwtUtil;
import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.domain.user.service.EmailService;
import com.ict.finalpj.domain.user.service.UserService;
import com.ict.finalpj.domain.user.vo.SocialVO;
import com.ict.finalpj.domain.user.vo.UserVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;

    public UserController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    // 로그인
    @PostMapping("/login")
    public DataVO userLogin(@RequestBody UserVO uvo) {
        DataVO dvo = new DataVO();
        try {
            UserVO uvo_true = userService.getUserInfoById(uvo.getUserId());
            // 아이디 검증
            if(uvo_true == null){
                dvo.setSuccess(false);
                dvo.setMessage("존재하지 않는 로그인 정보입니다");
                return dvo;
            }

            // PW 검증
            if(!passwordEncoder.matches(uvo.getUserPw(), uvo_true.getUserPw())){
                dvo.setSuccess(false);
                dvo.setMessage("존재하지 않는 로그인 정보입니다");
                return dvo;
            }

            // 로그인 성공 시 JWT 생성 및 authStore 저장
            String userIdx = uvo_true.getUserIdx();
            String jwtToken = jwtUtil.generateToken(userIdx);

            Map<String, String> map = new HashMap<>();
            map.put("userIdx", userIdx);
            map.put("userNickname", uvo_true.getUserNickname());
            map.put("userEtc01", uvo_true.getUserEtc01());
            dvo.setData(map);

            dvo.setSuccess(true);
            dvo.setMessage("로그인 성공!");
            dvo.setJwtToken(jwtToken);

            // 로그인 기록 
            userService.updateConnRegByIdx(userIdx);

        } catch (Exception e) {
            dvo.setSuccess(false);
            dvo.setMessage("Login Failed");
            e.printStackTrace();
        }
        return dvo;
    }

    // 아이디 찾기
    @PostMapping("/login/findId")
    public DataVO getUserId(@RequestBody UserVO uvo) {
        DataVO dvo = new DataVO();
        try {  
            UserVO uvo_true = userService.findUserId(uvo);
            if(uvo_true == null){
                dvo.setSuccess(false);
                dvo.setMessage("일치하는 정보가 존재하지 않습니다");
            } else{
                String userId = uvo_true.getUserId();
                
                dvo.setSuccess(true);
                dvo.setMessage("귀하의 Id는 " + userId+"입니다");
            }
            
        } catch (Exception e) {
            dvo.setSuccess(false);
            dvo.setMessage("Failed to find Id");
            e.printStackTrace();
        }
        return dvo;
    }

    // 비밀번호 찾기
    @PostMapping("/login/findPw")
    public DataVO getUserPw(@RequestBody UserVO uvo) {
        DataVO dvo = new DataVO();
        try {  
            UserVO uvo_true = userService.findUserPw(uvo);
            log.info("uvo " + uvo);
            log.info("uvo_pw " + uvo_true);
            if(uvo_true == null){
                dvo.setSuccess(false);
                dvo.setMessage("일치하는 정보가 존재하지 않습니다");
            }else{
                dvo.setData(uvo_true.getUserIdx());
                dvo.setSuccess(true);
                dvo.setMessage("정보 조회 성공");
            }
            
        } catch (Exception e) {
            dvo.setSuccess(false);
            dvo.setMessage("Failed to find Pw");
            e.printStackTrace();
        }
        return dvo;
    }

    // 비밀번호 재설정
    @PostMapping("/login/changeUserPw")
    public DataVO changeUserPw(@RequestParam String userPw,
    @RequestParam String userIdx) {
        DataVO dvo = new DataVO();
        try {  
            UserVO uvo_new = new UserVO();
            uvo_new.setUserIdx(userIdx);
            uvo_new.setUserPw(passwordEncoder.encode(userPw));
            
            int result = userService.changeUserPw(uvo_new);
            if (result > 0){
                dvo.setSuccess(true);
                dvo.setMessage("비밀번호 변경 성공!");
            }else{
                dvo.setSuccess(false);
                dvo.setMessage("비밀번호 변경 오류");
            }
        
        } catch (Exception e) {
            dvo.setSuccess(false);
            dvo.setMessage("Failed to change Pw");
            e.printStackTrace();
        }
        return dvo;
    }
    

    // 회원가입 - 일반
    @PostMapping("/join")
    public DataVO userJoin(@RequestBody UserVO uvo) {
        DataVO dvo = new DataVO();
        try {
            String encPW = passwordEncoder.encode(uvo.getUserPw());
            uvo.setUserPw(encPW);
            log.info("uvo : " + uvo);
            int result = userService.insertUserInfo(uvo);
            if(result > 0 ){
                dvo.setSuccess(true);
                dvo.setMessage("회원가입 성공");
            }else{
                dvo.setSuccess(false);
                dvo.setMessage("회원가입 실패!");
            }
        } catch (Exception e) {
            dvo.setSuccess(false);
            dvo.setMessage("Join Failed");
            e.printStackTrace();
        }
        return dvo;
    }

    @PostMapping("/join/chkKakao")
    public DataVO checkKakao(@RequestBody UserVO uvo) {
        DataVO dvo = new DataVO();
        try {
            log.info("chkKakao uvo : "+ uvo);

            // 전화번호와 정보 확인 절차
            UserVO uvo_true = userService.getUserInfoByPhone(uvo.getUserPhone());
            String name_true = uvo_true.getUserName();
            String mail_true = uvo_true.getUserMail();
            if(uvo.getUserName().equals(name_true)){
                dvo = sendMail(uvo.getUserMail());
                dvo.setSuccess(true);
                dvo.setMessage("Kakao 로그인에 사용하신 email로 인증 코드를 보냈습니다");
            } else{
                dvo.setSuccess(false);
                dvo.setMessage("일치하는 정보가 없습니다");
            }

        } catch (Exception e) {
            dvo.setSuccess(false);
            dvo.setMessage("Join Failed");
            e.printStackTrace();
        }
        return dvo;
    }

    @PostMapping("/join/updateKakao")
    public DataVO updateKakao(@RequestBody UserVO uvo) {
        DataVO dvo = new DataVO();
        try {
            int result = userService.updateUserKakaoId(uvo);
            
            if(result > 0 ){
                dvo.setSuccess(true);
                dvo.setMessage("정보 연동 성공");
            }else{
                dvo.setSuccess(false);
                dvo.setMessage("정보 연동 실패!");
            }
        } catch (Exception e) {
            dvo.setSuccess(false);
            dvo.setMessage("Join Failed");
            e.printStackTrace();
        }
        return dvo;
    }

    // 이메일 전송 및 확인
    @GetMapping("/join/mailchk/{userMail}")
    public DataVO sendMail(@PathVariable("userMail") String userMail) {
        DataVO dvo = new DataVO();
        try {
            Random rand = new Random();
            String randNum = String.valueOf(rand.nextInt(1000000));
            if(randNum.length() < 6){
                int empty = 6 - randNum.length();
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < empty; i++) {
                    sb.append("0");
                }
                sb.append(randNum);
                randNum = sb.toString();
            }

            emailService.sendMail(randNum, userMail);

            dvo.setData(randNum);
            dvo.setSuccess(true);
            dvo.setMessage("Email 전송 성공");

            
        } catch (Exception e) {
            dvo.setSuccess(false);
            dvo.setMessage("Email 전송 실패");
            e.printStackTrace();
        }
        return dvo;
    }
    
    // 아이디, 닉네임, 전화번호, 이메일 중복검사
    @GetMapping("/join/chkDupl")
    public DataVO chkDuplicates(@RequestParam("field") String field,
    @RequestParam("value") String value) {
        DataVO dvo = new DataVO();
        try {
            UserVO uvo = new UserVO();

            if(field.equals("userId")){
                uvo = userService.getUserInfoById(value);
                if(uvo == null) {
                    dvo.setSuccess(true);
                    dvo.setMessage("사용 가능한 ID 입니다");
                } else{
                    dvo.setSuccess(false);
                    dvo.setMessage("중복된 ID입니다");
                }
                return dvo;
            }
            if(field.equals("userPhone")){
                uvo = userService.getUserInfoByPhone(value);
                log.info("phone : " + uvo);
                if(uvo == null) {
                    dvo.setSuccess(true);
                    dvo.setMessage("사용 가능한 전화번호 입니다");
                } else{
                    dvo.setSuccess(false);
                    dvo.setMessage("중복된 전화번호입니다");
                }
                return dvo;
            }
            if(field.equals("userNickname")){
                uvo = userService.getUserInfoByNickname(value);
                if(uvo == null) {
                    dvo.setSuccess(true);
                    dvo.setMessage("사용 가능한 닉네임 입니다");
                } else{
                    dvo.setSuccess(false);
                    dvo.setMessage("중복된 닉네임입니다");
                }
                return dvo;
            }
            if(field.equals("userMail")){
                uvo = userService.getUserInfoByMail(value);
                if(uvo == null) {
                    dvo.setSuccess(true);
                    dvo.setMessage("사용 가능한 이메일 입니다");
                } else{
                    dvo.setSuccess(false);
                    dvo.setMessage("중복된 이메일입니다");
                }
                return dvo;
            }

        } catch (Exception e) {
            dvo.setSuccess(false);
            dvo.setMessage("중복 검사 Error");
            e.printStackTrace();
        }
        return dvo;
    }
    
    @GetMapping("/getSocials")
    public DataVO getSocialData(@RequestParam("socialIdx") String socialIdx) {
        DataVO dvo = new DataVO();
        try {
            log.info("socialIdx : " + socialIdx);

            SocialVO sovo = userService.getSocialData(socialIdx);
            log.info("sovo : " + sovo);

            if(sovo != null){
                dvo.setData(sovo);
                dvo.setSuccess(true);
                dvo.setMessage("getSocials Success");
            }else{
                dvo.setSuccess(false);
                dvo.setMessage("getSocials Failed");
            }

        } catch (Exception e) {
            dvo.setSuccess(false);
            dvo.setMessage("getSocials Error");
            e.printStackTrace();
        }
        return dvo;
    }
    
    
    
}
