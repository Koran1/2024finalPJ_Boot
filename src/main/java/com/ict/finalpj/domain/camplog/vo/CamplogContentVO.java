package com.ict.finalpj.domain.camplog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CamplogContentVO {
    private String logContentIdx, logIdx, logContent, logContentUpdated, logContentCreated, logContentOrder, logContentIsActive;
}