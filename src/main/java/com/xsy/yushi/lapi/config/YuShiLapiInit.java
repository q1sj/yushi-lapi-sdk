package com.xsy.yushi.lapi.config;

import com.xsy.yushi.lapi.YuShiLapiSDK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author Q1sj
 * @date 2026/4/10 下午3:28
 */
@Slf4j
@Component
public class YuShiLapiInit {
	@Autowired
	private YuShiLapiConfigs yuShiLapiConfigs;
	@Autowired
	private YuShiLapiSDK yuShiLapiSDK;

	@PostConstruct
	public void init() {
		List<YuShiLapiConfig> lapi = yuShiLapiConfigs.getLapi();
		if (CollectionUtils.isEmpty(lapi)) {
			return;
		}
		for (YuShiLapiConfig config : lapi) {
			try {
				yuShiLapiSDK.subscription(config);
			} catch (Exception e) {
				log.error("宇视lapi sdk订阅失败 {}", config);
			}
		}
	}
}
