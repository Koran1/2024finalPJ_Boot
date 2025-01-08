package com.ict.finalpj.domain.weather.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalpj.domain.weather.mapper.WthrMapper;
import com.ict.finalpj.domain.weather.vo.RegionVO;
import com.ict.finalpj.domain.weather.vo.WthrVO;

@Service
public class WthrServiceImpl implements WthrService {
    @Autowired
    private WthrMapper wthrMapper;

    @Override
    public RegionVO getRegInfo(String region) {
        return wthrMapper.getRegInfo(region);
    }

    @Override
    public List<WthrVO> getWthrInfo(String region) {
        return wthrMapper.getWthrInfo(region);
    }

    @Override
    public int insertWthrInfo(WthrVO wthrvo) {
        return wthrMapper.insertWthrInfo(wthrvo);
    }

    @Override
    public int deleteWthrInfo() {
        return wthrMapper.deleteWthrInfo();
    }

    @Override
    public int insertMoonSunInfo(WthrVO wthrvo) {
        return wthrMapper.insertMoonSunInfo(wthrvo);
    }

    @Override
    public int insertLunAgeInfo(WthrVO wthrvo) {
        return wthrMapper.insertLunAgeInfo(wthrvo);
    }

}
