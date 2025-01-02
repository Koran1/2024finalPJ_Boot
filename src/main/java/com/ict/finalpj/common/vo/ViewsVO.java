package com.ict.finalpj.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewsVO {
    private String viewIdx;
    private String viewTableType;
    private String viewTableIdx;
    private String userIdx;
    private Integer viewCount;
    private String viewRegTime;
}