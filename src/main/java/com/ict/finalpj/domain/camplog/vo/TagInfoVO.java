package com.ict.finalpj.domain.camplog.vo;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagInfoVO {
    private String logIdx, userIdx, tagId,  fieldIdx, dealIdx, tagX, tagY, tagContent ;
    private List<TagData> tagData;

    @Data
    public static class TagData {
        private String tagId,  fieldIdx, dealIdx, tagX, tagY, tagContent ;
    }
  
}
