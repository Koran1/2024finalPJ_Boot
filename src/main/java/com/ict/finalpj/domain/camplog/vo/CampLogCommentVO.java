package com.ict.finalpj.domain.camplog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampLogCommentVO {
    private String logCommentIdx, logIdx, userIdx, logCommentContent, logCommentRegDate, commentIdx, logCommentIsActive, logCommentIsDelete;
    private String logTitle;
    
    // 어드민 페이지에서 사용할 페이징, 검색용
    private String keyword, sortOption;
    private int page, size, offset;
}