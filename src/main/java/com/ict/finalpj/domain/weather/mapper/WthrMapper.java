package com.ict.finalpj.domain.weather.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ict.finalpj.domain.weather.vo.RegionVO;
import com.ict.finalpj.domain.weather.vo.WthrVO;

@Mapper
public interface WthrMapper {
    RegionVO getRegInfo(String region);
	List<WthrVO> getWthrInfo(String region);
	int insertWthrInfo(WthrVO wthrvo);
	int deleteWthrInfo();

	int insertMoonSunInfo(WthrVO wthrvo);
	int insertLunAgeInfo(WthrVO wthrvo);
}
