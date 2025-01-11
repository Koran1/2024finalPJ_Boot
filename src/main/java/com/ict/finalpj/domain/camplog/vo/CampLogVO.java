package com.ict.finalpj.domain.camplog.vo;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampLogVO {
    private String logIdx, userIdx, campIdx, logTitle, logView, logRecommend, logIsProduct, logIsActive,
            logUpdateDate, logRegDate, userNickname, logThumbnail;
    private MultipartFile mpFile;

    // 어드민 페이지에서 사용할 페이징, 검색용
    private String keyword, sortOption;
    private int page, size, offset;
}
