package com.ict.finalpj.domain.camplog.vo;

import java.util.List;

import lombok.Data;

@Data
public class CampLogContentVO {
    private String logContentIdx, logIdx, logContentIsActive, logContent, logContentOrder;
    private List<ContentData> contentData;
    

    @Data
    public static class ContentData {
        private String logContent, logContentOrder;
    }

}