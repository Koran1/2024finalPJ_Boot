package com.ict.finalpj.common.vo;

import org.springframework.security.core.userdetails.UserDetails;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataVO {
    private boolean success;
    private Object data;
    private String jwtToken;
    private String message;
    private UserDetails userDetails;
}