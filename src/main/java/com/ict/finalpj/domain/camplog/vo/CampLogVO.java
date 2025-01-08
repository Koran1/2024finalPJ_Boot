package com.ict.finalpj.domain.camplog.vo;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CampLogVO {
    private String logIdx, userIdx, campIdx, logTitle, logThumbnail, logView, logRecommend, logIsProduct, logIsActive,
            logUpdateDate, logRegDate;
    private MultipartFile mpFile;
}
