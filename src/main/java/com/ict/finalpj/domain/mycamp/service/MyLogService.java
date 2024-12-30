package com.ict.finalpj.domain.mycamp.service;

import java.util.Map;

import com.ict.finalpj.domain.mycamp.vo.MyLogVO;

public interface MyLogService {
    // mylog 리스트
    Map<String, Object> getMyLogList(MyLogVO myLogVO);
}
