package com.ict.finalpj.domain.camp.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalpj.common.vo.ViewsVO;
import com.ict.finalpj.domain.camp.mapper.CampMapper2;
import com.ict.finalpj.domain.camp.vo.CampVO;
import com.ict.finalpj.domain.camplog.vo.CampLogVO;

@Service
public class CampServiceImpl2 implements CampService2 {
    @Autowired
    private CampMapper2 campMapper2;

    @Override
    public CampVO getCampDetail(String campIdx) {
        return campMapper2.getCampDetail(campIdx);
    }

    @Override
    public List<CampLogVO> getCampLog(String campIdx) {
        return campMapper2.getCampLog(campIdx);
    }

    @Override
    public boolean isLiked(String userIdx, String campIdx) {
        // 좋아요 상태를 바로 반환
        return campMapper2.isLiked(userIdx, campIdx);
    }

    @Override
    public void likeCamp(String userIdx, String campIdx) {
        // 좋아요 추가
        campMapper2.addLike(userIdx, campIdx);
    }

    @Override
    public void unlikeCamp(String userIdx, String campIdx) {
        // 좋아요 삭제
        campMapper2.removeLike(userIdx, campIdx);
    }

    @Override
    public void updateViewCount(String userIdx, String campIdx) {
        // 현재 시간
        // String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // 조회수 정보가 이미 존재하는지 확인
        ViewsVO viewTable = campMapper2.getViewInfo(userIdx, campIdx);

        if (viewTable == null) {
            // 조회수 정보가 없으면 새로 추가
            campMapper2.insertViewInfo(userIdx, campIdx);
        } else {
            // viewRegTime을 String에서 Date로 변환
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date viewRegTime = null;
            try {
                viewRegTime = sdf.parse(viewTable.getViewRegTime());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 현재 시간과 viewRegTime을 비교하여 24시간 이상 경과했는지 확인
            if (viewRegTime != null) {
                long diffInMillis = System.currentTimeMillis() - viewRegTime.getTime();
                long diffInHours = diffInMillis / (60 * 60 * 1000); // 밀리초에서 시간 단위로 변환

                if (diffInHours >= 24) {
                    // 24시간 이상 경과하면 조회수 증가 및 시간 업데이트
                    campMapper2.updateViewInfo(userIdx, campIdx);
                }
            }
        }
    }
}
