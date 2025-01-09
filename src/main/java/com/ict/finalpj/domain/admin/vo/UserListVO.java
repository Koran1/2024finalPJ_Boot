package com.ict.finalpj.domain.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListVO {
    private String 
        userIdx, 
        userId, 
        n_userId, 
        k_userId, 
        userPw, 
        userName, 
        userNickname, 
        userMail, 
        userPhone, 
        userAddr, 
        userLevel, 
        userReg, 
        userUpdReg, 
        userConnReg, 
        userReported, 
        dealSatisSellerScore, 
        userEtc01, 
        userEtc02, 
        userEtc03, 
        keyword, 
        option
        ;

    private int page, size, offset;
}
