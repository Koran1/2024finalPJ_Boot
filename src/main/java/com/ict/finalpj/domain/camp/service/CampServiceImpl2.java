package com.ict.finalpj.domain.camp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalpj.domain.camp.mapper.CampMapper2;
import com.ict.finalpj.domain.camp.vo.CampVO;

@Service
public class CampServiceImpl2 implements CampService2 {
    @Autowired
    private CampMapper2 campMapper2;

    @Override
    public CampVO getCampDetail(String campIdx) {
        return campMapper2.getCampDetail(campIdx);
    }

}
