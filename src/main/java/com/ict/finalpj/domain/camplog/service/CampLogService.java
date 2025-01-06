package com.ict.finalpj.domain.camplog.service;

import java.util.Map;

import com.ict.finalpj.domain.camplog.vo.CampLogListVO;

public interface CampLogService {
    // 캠핑로그 list
    Map<String, Object> getCamplogList(CampLogListVO campLogListVO);
}
