package com.ict.finalpj.domain.camplog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CamplogVO {
    private String logIdx, userIdx, campIdx, logTitle, logThumbnail, logView, logRecommend, logIsProduct, logIsActive, logUpdateDate, logRegDate;
}