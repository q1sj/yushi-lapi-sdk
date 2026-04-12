package com.xsy.yushi.lapi.config;

import com.xsy.device.config.car.CarDeviceConfig;
import com.xsy.yushi.lapi.enums.ImageDataTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Q1sj
 * @date 2026/4/10 下午2:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class YuShiLapiConfig extends CarDeviceConfig {
	/**
	 * 数据发送目的IP地址类型
	 * 0:IPv4 1:IPv6 2:域名 3:IPv4和IPv6都需要。当前仅支持IPv4
	 */
	public int receiveAlarmDataAddressType = 0;
	/**
	 * 数据发送目的IPv4地址
	 */
	public String receiveAlarmDataAddress;
	/**
	 * 数据发送目的端口，范围为[1, 65535]
	 */
	public int receiveAlarmDataPort = 50235;
	/**
	 * 订阅周期，单位为 s，范围为[30, 3600]
	 */
	public int SubscriptionDuration = 60;
	/**
	 * 订阅对象数量
	 */
	public int SubscriptionObjNum = 1;
	/**
	 * 订阅事件类型
	 */
	public String SubscriptionEventType = "All";
	/**
	 * 订阅图片类型
	 */
	public long SubscriptionPictureType = ImageDataTypeEnum.BASE64_DATA_TYPE.getValue();

	private Integer subscribeId;
	/**
	 * 扩展字段
	 */
	private Map<String, Object> extra = new HashMap<>();

	public String getSubscribeURL() {
		return "http://" + ip + ":" + port + "/LAPI/V1.0/System/Event/Subscription";
	}

	public YuShiLapiConfig() {
		super();
		this.port = 80;
	}
}
