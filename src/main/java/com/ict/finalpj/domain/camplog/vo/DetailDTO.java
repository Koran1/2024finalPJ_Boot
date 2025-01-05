package com.ict.finalpj.domain.camplog.vo;

import java.util.List;

import lombok.Data;

@Data
public class DetailDTO {
    private int order;
    private String  logContent, fileName;
    private List<DetailTagData> tagData;

    @Data
    public static class DetailTagData {
        private String dealIdx, fieldIdx, tagX, tagY, tagContent, tagId;
    }
}
