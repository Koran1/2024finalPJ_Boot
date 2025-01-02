package com.ict.finalpj.domain.camplog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampLogCommentVO {
    private String logCommentIdx, logIdx, userIdx, logCommentContent, logCommentRegDate, commentIdx, logCommentIsActive;
}