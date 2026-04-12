package com.xsy.yushi.lapi;

import com.uni.threads.ListenThread;
import com.uni.threads.SuscribeThread;
import com.xsy.yushi.lapi.config.YuShiLapiConfig;
import com.xsy.yushi.lapi.config.YuShiLapiConfigs;
import com.xsy.yushi.lapi.handle.YuShiSDKMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Q1sj
 * @date 2026/4/10 下午2:19
 */
@Slf4j
@Component
public class YuShiLapiSDK {

	private static final Map<Integer, ListenThread> LISTEN_THREAD_MAP = new ConcurrentHashMap<>();
	private final List<YuShiSDKMessageHandler> handlers;
	private final YuShiLapiConfigs configs;

	public YuShiLapiSDK(List<YuShiSDKMessageHandler> handlers, YuShiLapiConfigs configs) {
		this.handlers = Objects.requireNonNull(handlers);
		this.configs = Objects.requireNonNull(configs);
	}

	public void subscription(YuShiLapiConfig config) {
		SuscribeThread suscribeThread = new SuscribeThread(config);
		if (suscribeThread.subscribe() == -1) {
			log.error("config:{}订阅失败", config);
		}
		suscribeThread.start();
		LISTEN_THREAD_MAP.computeIfAbsent(config.getReceiveAlarmDataPort(), port -> {
			ListenThread thread = new ListenThread(port, handlers, configs);
			thread.start();
			return thread;
		});
	}
}
