package com.ict.finalpj.domain.camp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalpj.domain.camp.mapper.CampMapper;
import com.ict.finalpj.domain.camp.vo.CampVO;

@Service
public class CampServiceImpl implements CampService {
    @Autowired
    private CampMapper campMapper;

    @Override
    public List<CampVO> getCampingList() {
        return campMapper.getCampingList();
    }

    @Override
    public List<String> getDoNmList() {
        List<String> doNmLists = campMapper.getDoNmList();

                return doNmLists.stream()
                .map(doNmList -> {
                    if (doNmList.contains("서울")) return "서울";
                    if (doNmList.contains("부산")) return "부산";
                    if (doNmList.contains("대구")) return "대구";
                    if (doNmList.contains("인천")) return "인천";
                    if (doNmList.contains("광주")) return "광주";
                    if (doNmList.contains("대전")) return "대전";
                    if (doNmList.contains("울산")) return "울산";
                    if (doNmList.contains("세종")) return "세종";
                    if (doNmList.contains("경기")) return "경기";
                    if (doNmList.contains("강원")) return "강원";
                    if (doNmList.contains("충청북도")) return "충북";
                    if (doNmList.contains("충청남도")) return "충남";
                    if (doNmList.contains("전라북도")) return "전북";
                    if (doNmList.contains("전라남도")) return "전남";
                    if (doNmList.contains("경상북도")) return "경북";
                    if (doNmList.contains("경상남도")) return "경남";
                    if (doNmList.contains("제주")) return "제주";
                    return doNmList;
                })
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getSigunguList(String doNm2) {
        String originalDoNm = switch (doNm2) {
            case "서울" -> "서울특별시";
            case "부산" -> "부산광역시";
            case "대구" -> "대구광역시";
            case "인천" -> "인천광역시";
            case "광주" -> "광주광역시";
            case "대전" -> "대전광역시";
            case "울산" -> "울산광역시";
            case "경기" -> "경기도";
            case "강원" -> "강원도";
            case "충북" -> "충청북도";
            case "충남" -> "충청남도";
            case "전북" -> "전라북도";
            case "전남" -> "전라남도";
            case "경북" -> "경상북도";
            case "경남" -> "경상남도";
            case "제주" -> "제주도";
            case "세종" -> "세종시";
            default -> doNm2;
        };
    
        return campMapper.getSigunguList(originalDoNm);
    }
}
