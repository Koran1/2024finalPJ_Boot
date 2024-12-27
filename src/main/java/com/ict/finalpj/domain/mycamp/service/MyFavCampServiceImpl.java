package com.ict.finalpj.domain.mycamp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalpj.domain.camp.vo.CampVO;
import com.ict.finalpj.domain.mycamp.mapper.MyFavCampMapper;

@Service
public class MyFavCampServiceImpl implements MyFavCampService {
    @Autowired
    private MyFavCampMapper myFavCampMapper;

    @Override
    public List<CampVO> getFavCampList(String userIdx) {
        return myFavCampMapper.getFavCampList(userIdx);
    }

}
