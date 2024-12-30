package com.ict.finalpj.domain.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ict.finalpj.common.vo.DataVO;


@RestController
@RequestMapping("/api/user")
public class UserController {
    @GetMapping("/login")
    public DataVO getMethodName(@RequestParam String param) {
        DataVO dvo = new DataVO();

        return dvo;
    }
    
}
