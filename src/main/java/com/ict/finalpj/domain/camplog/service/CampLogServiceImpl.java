package com.ict.finalpj.domain.camplog.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalpj.domain.camplog.mapper.CampLogMapper;
import com.ict.finalpj.domain.camplog.vo.CampLogListVO;

@Service
public class CampLogServiceImpl implements CampLogService {
    @Autowired
    private CampLogMapper campLogMapper;

    @Override
    public Map<String, Object> getCamplogList(CampLogListVO campLogListVO) {
        int offset = (campLogListVO.getPage() -1) * campLogListVO.getSize();
        campLogListVO.setOffset(offset);

        List<CampLogListVO> camplogList = campLogMapper.getCamplogList(campLogListVO);
        int totalCount = campLogMapper.getCampLogCount(campLogListVO);

        Map<String, Object> result = new HashMap<>();
        result.put("data", camplogList);
        result.put("totalCount", totalCount);
        result.put("totalPages", (int) Math.ceil((double) totalCount / campLogListVO.getSize()));
        return result;
    }
}
