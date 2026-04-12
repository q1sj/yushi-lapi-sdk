package com.xsy.yushi.lapi.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ComponentScan("com.xsy.yushi.lapi")
public class YuShiLapiAutoConfig {
}
