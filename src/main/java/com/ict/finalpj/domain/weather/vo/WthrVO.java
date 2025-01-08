package com.ict.finalpj.domain.weather.vo;

import lombok.Data;

@Data
public class WthrVO {
    private String wthrDate, region, wthrTMin, wthrTMax, wthrSKY_PTY, wthrPOP,
    wthrSunrise, wthrSunset, wthrMoonrise, wthrMoonset, wthrLunAge, wthrEtc01, wthrEtc02;
}
