package com.github.tanyueran.component;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IdComp {

    // 终端id
    @Value("${self.serviceId}")
    private long serviceId;

    // 数据中心id
    @Value("${self.dataId}")
    private long dataId;

    @Bean
    public Snowflake snowflake() {
        return IdUtil.getSnowflake(serviceId, dataId);
    }
}
