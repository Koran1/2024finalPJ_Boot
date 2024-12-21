package com.ict.finalpj.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewsVO {
    private String viewIdx, viewTableType, viewTableIdx, userIdx, viewCount, viewRegTime;
}
