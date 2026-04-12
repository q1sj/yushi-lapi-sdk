package com.xsy.yushi.lapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Q1sj
 * @date 2026/4/10 下午3:16
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "device-configs.yushi")
public class YuShiLapiConfigs {
	private List<YuShiLapiConfig> lapi = new CopyOnWriteArrayList<>();
}
