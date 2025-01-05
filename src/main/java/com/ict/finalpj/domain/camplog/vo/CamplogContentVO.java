package com.ict.finalpj.domain.camplog.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampLogContentVO {
    private String logContentIdx, logIdx, logContent, logContentUpdated, logContentCreated, logContentOrder, logContentIsActive;
    
    private List<ContentData> contentData;
    
    @Data
    public static class ContentData {
        private String logContent, logContentOrder;
    }
}