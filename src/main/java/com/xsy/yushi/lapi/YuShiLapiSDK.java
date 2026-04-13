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
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Q1sj
 * @date 2026/4/10 下午2:19
 */
@Slf4j
@Component
public class YuShiLapiSDK {

	private static final Map<Integer, ListenThread> LISTEN_THREAD_MAP = new ConcurrentHashMap<>();
	private static final Map<String, SuscribeThread> SUSCRIBE_THREAD_MAP = new ConcurrentHashMap<>();
	private final List<YuShiSDKMessageHandler> handlers;
	private final YuShiLapiConfigs configs;

	public YuShiLapiSDK(List<YuShiSDKMessageHandler> handlers, YuShiLapiConfigs configs) {
		this.handlers = Objects.requireNonNull(handlers);
		this.configs = Objects.requireNonNull(configs);
	}

	public boolean subscription(YuShiLapiConfig config) {
		AtomicBoolean isSubscription = new AtomicBoolean(false);
		SuscribeThread suscribeThread = SUSCRIBE_THREAD_MAP.compute(config.getSubscribeURL(), (k, v) -> {
			if (v != null && !v.isStop() && v.getSubscribeId() > 0) {
				log.info("订阅已存在 {}", k);
				isSubscription.set(true);
				return v;
			}
			SuscribeThread thread = new SuscribeThread(config);
			if (thread.subscribe() == -1) {
				log.error("config:{}订阅失败", config);
				isSubscription.set(false);
			} else {
				isSubscription.set(true);
				thread.start();
			}
			return thread;
		});
		if (!isSubscription.get()) {
			return false;
		}
		SUSCRIBE_THREAD_MAP.put(config.getSubscribeURL(), suscribeThread);
		LISTEN_THREAD_MAP.computeIfAbsent(config.getReceiveAlarmDataPort(), port -> {
			ListenThread thread = new ListenThread(port, handlers, configs);
			thread.start();
			return thread;
		});
		if (!configs.getLapi().contains(config)) {
			configs.getLapi().add(config);
		}
		return true;
	}

	public void stopSubscription(YuShiLapiConfig config) {
		SuscribeThread suscribeThread = SUSCRIBE_THREAD_MAP.remove(config.getSubscribeURL());
		if (suscribeThread != null) {
			suscribeThread.stopRefreshAndCancelSubscription();
			suscribeThread.interrupt();
		}
		configs.getLapi().remove(config);
	}
}
