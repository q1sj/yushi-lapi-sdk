package com.xsy.yushi.lapi.handle;

import com.xsy.yushi.lapi.config.YuShiLapiConfig;

/**
 * @author Q1sj
 * @date 2026/4/10 下午2:34
 */
public interface YuShiSDKMessageHandler {
	String supportUri();

	void handle(YuShiLapiConfig config, String message);
}
