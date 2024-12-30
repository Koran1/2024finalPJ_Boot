package com.ict.finalpj.domain.mycamp.service;

import java.util.List;

import com.ict.finalpj.domain.camp.vo.CampVO;

public interface MyFavCampService {
    List<CampVO> getFavCampList(String userIdx);
}
