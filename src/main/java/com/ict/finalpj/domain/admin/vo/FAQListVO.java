package com.ict.finalpj.domain.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FAQListVO {
    private String 
        faqIdx, 
        faqQuestion, 
        faqAnswer, 
        faqReg, 
        faqStatus, 
        keyword, 
        option
        ;
    private int page, size, offset;
}
