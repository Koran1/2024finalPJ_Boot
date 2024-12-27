package com.ict.finalpj.domain.mycamp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalpj.domain.mycamp.mapper.MyLogMapper;
import com.ict.finalpj.domain.mycamp.vo.MyLogVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MyLogServiceImpl implements MyLogService {
    @Autowired
    private MyLogMapper myLogMapper;

    @Override
    public Map<String, Object> getMyLogList(MyLogVO myLogVO) {
        int offset = (myLogVO.getPage() -1) * myLogVO.getSize();
        myLogVO.setOffset(offset);

        List<MyLogVO> myLogList = myLogMapper.getMyLogList(myLogVO);
        int totalCount = myLogMapper.getMyLogCount(myLogVO);

        Map<String, Object> result = new HashMap<>();
        result.put("data", myLogList);
        result.put("totalCount", totalCount);
        result.put("totalPages", (int) Math.ceil((double) totalCount / myLogVO.getSize()));

        return result;
    }
}
