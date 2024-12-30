package com.ict.finalpj.domain.camp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalpj.domain.camp.mapper.CampMapper;
import com.ict.finalpj.domain.camp.vo.CampSearchVO;
import com.ict.finalpj.domain.camp.vo.CampVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CampServiceImpl implements CampService {
    @Autowired
    private CampMapper campMapper;

    // @Override
    // public List<CampVO> getCampingList() {
    //     return campMapper.getCampingList();
    // }

    @Override
    public Map<String, Object> getCampingList(CampSearchVO searchVO) {
        int offset = (searchVO.getPage() -1) * searchVO.getSize();
        searchVO.setOffset(offset);

        List<CampVO> campList = campMapper.getCampingList(searchVO);
        int totalCount = campMapper.getCampSearchCount(searchVO);
        
        Map<String, Object> result = new HashMap<>();
        result.put("data", campList);
        result.put("totalCount", totalCount);
        result.put("totalPages", (int) Math.ceil((double) totalCount / searchVO.getSize()));
        
        return result;
    }

    @Override
    public List<String> getDoNmList() {
        return campMapper.getDoNmList();
    }

    @Override
    public List<String> getSigunguList(String doNm2) {
        // String originalDoNm = switch (doNm2) {
        //     case "서울" -> "서울특별시";
        //     case "부산" -> "부산광역시";
        //     case "대구" -> "대구광역시";
        //     case "인천" -> "인천광역시";
        //     case "광주" -> "광주광역시";
        //     case "대전" -> "대전광역시";
        //     case "울산" -> "울산광역시";
        //     case "경기" -> "경기도";
        //     case "강원" -> "강원도";
        //     case "충북" -> "충청북도";
        //     case "충남" -> "충청남도";
        //     case "전북" -> "전라북도";
        //     case "전남" -> "전라남도";
        //     case "경북" -> "경상북도";
        //     case "경남" -> "경상남도";
        //     case "제주" -> "제주도";
        //     case "세종" -> "세종시";
        //     default -> doNm2;
        // };
    
        return campMapper.getSigunguList(doNm2);
    }
}
