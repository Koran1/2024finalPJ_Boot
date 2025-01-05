package com.ict.finalpj.domain.camplog.vo;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampLogVO {
    private String logIdx, userIdx, campIdx, logTitle, logView, logRecommend, logIsProduct, logIsActive, logUpdateDate, logRegDate;
    private MultipartFile mpFile;
    

}