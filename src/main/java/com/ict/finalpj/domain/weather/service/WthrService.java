package com.ict.finalpj.domain.weather.service;

import java.util.List;

import com.ict.finalpj.domain.weather.vo.RegionVO;
import com.ict.finalpj.domain.weather.vo.WthrVO;

public interface WthrService {
	public RegionVO getRegInfo(String region);
	public List<WthrVO> getWthrInfo(String region);
	public int insertWthrInfo(WthrVO wthrvo);
	public int deleteWthrInfo();

	public int insertMoonSunInfo(WthrVO wthrvo);
	public int insertLunAgeInfo(WthrVO wthrvo);
}
