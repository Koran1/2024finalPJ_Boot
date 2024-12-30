package com.ict.finalpj.domain.camplog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampLogContentVO {
    private String logContentIdx, logIdx, logContent, logContentUpdated, logContentCreated, logContentOrder, logContentIsActive;
}