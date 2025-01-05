package com.ict.finalpj.domain.camplog.vo;


import com.ict.finalpj.common.vo.FileVO;
import com.ict.finalpj.domain.camp.vo.CampVO;
import com.ict.finalpj.domain.user.vo.UserVO;

import lombok.Data;
@Data
public class WriteDTO {
    private UserVO uvo;
    private CampVO cvo ;
    private CampLogVO lvo;
    private CampLogContentVO lcvo;
    private TagInfoVO tvo;
    private FileVO fvo;
}
