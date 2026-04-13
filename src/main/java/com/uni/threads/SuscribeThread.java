package com.uni.threads;

import com.uni.beans.DataInfo;
import com.uni.beans.Response.LAPIResponse;
import com.uni.utils.http.HttpResult;
import com.uni.utils.http.LAPIHttpConnection;
import com.xsy.yushi.lapi.config.YuShiLapiConfig;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SuscribeThread extends Thread {
	private boolean isStop = false; // 设置标志位。当isStop为false时，启动刷新订阅；当isStop为true时，停止刷新订阅

	private YuShiLapiConfig config;
	private LAPIHttpConnection http;
	@Getter
	private int subscribeId = 0;


	public SuscribeThread(YuShiLapiConfig config) {
		super("yushi-lapi-sub-" + config.getIp());
		setDaemon(true);
		this.config = config;
		http = new LAPIHttpConnection(config.getUsername(), config.getPassword());
	}

	@Override
	public void run() {
		if (subscribeId == 0) {
			this.subscribeId = subscribe();
		}

		if (this.subscribeId == -1) {
			log.info("订阅失败");
			return;
		}

		// 保活订阅计时
		int keepaliveTime = 0;
		// 保活成功计数
		int keepaliveCount = 0;

		while (isStop == false) {
			try {
				Thread.sleep(1000);
				if (isStop)
					return;

				// 刷新 保活订阅计时
				keepaliveTime++;

				if (keepaliveTime >= config.SubscriptionDuration - 10) {
					boolean res = refreshSubscription(this.subscribeId, config.SubscriptionDuration);
					keepaliveCount++;
					log.info("刷新保活次数：{}", keepaliveCount);
					if (res) {
						keepaliveTime = 0;
						continue;
					}
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 进行事件订阅
	 */
	public int subscribe() {
		// 填充“事件订阅”的requestBody请求体
		String requestBody = "{" +
				"\"AddressType\": " + config.receiveAlarmDataAddressType + "," +
				"\"IPAddress\": \"" + config.receiveAlarmDataAddress + "\"," +
				"\"Port\": " + config.receiveAlarmDataPort + "," +
				"\"Duration\": " + config.SubscriptionDuration + "," +
				"\"SubscribeEvent\": {" +
				"\"Num\":" + config.SubscriptionObjNum + "," +
				"\"SubScribeObjList\": [{" +
				"\"EventType\": \"" + config.SubscriptionEventType + "\"," +
				"\"PictureType\":" + config.SubscriptionPictureType +
				"}]}}";

		log.info("订阅参数：{}", requestBody);
		// 发送http POST 请求
		HttpResult httpResult = this.http.post(config.getSubscribeURL(), requestBody);
		LAPIResponse lapiResponse = httpResult.readLAPIResponse(DataInfo.class);
		String AlarmReturnData = "Method：POST" + "\n" + "URL：" + config.getSubscribeURL() + "\n" + "返回数据：" + httpResult.readResponseString();
		log.info(AlarmReturnData);
		if (lapiResponse.getResponseCode() != 0) {
			log.info("订阅失败");
			return -1;
		}
		// 提取返回数据Response的Data下的ID值
		DataInfo dataInfo = lapiResponse.getData();
		int subscribeId = dataInfo.getId();
		log.info("订阅成功，订阅id号：{}", subscribeId);
		this.subscribeId = subscribeId;
		config.setSubscribeId(subscribeId);
		return subscribeId;
	}

	/**
	 * 刷新订阅
	 */
	private boolean refreshSubscription(int subscribeId, int duration) {
		log.info("正在刷新保活，订阅ID号：{}", subscribeId);
		// 填充“刷新订阅”的requestBody请求体
		String requestBody = "{\"Duration\": " + duration + "}";
		// 发送http PUT 请求
		HttpResult httpResult = http.put(config.getSubscribeURL() + "/" + subscribeId, requestBody);
		// 提取返回数据Response下的StatusCode状态码
		LAPIResponse lapiResponse = httpResult.readLAPIResponse();
		System.out.println(httpResult.readResponseString()); // 打印返回数据
		String AlarmReturnData = "Method：PUT" + "\n" + "URL：" + config.getSubscribeURL() + "/" + subscribeId + "\n" + "返回数据：" + httpResult.readResponseString();
		log.info(AlarmReturnData);
		int statusCode = lapiResponse.getResponseCode();

		if (statusCode == 0) {
			log.info("刷新保活成功");
			return true;
		} else {
			log.info("刷新保活失败");
			return false;
		}
	}

	/**
	 * 取消订阅
	 */
	private boolean cancelSubscription(int subscribeId) {
		log.info("正在取消订阅，取消订阅ID号：{}", subscribeId);
		// 发送http DELETE 请求
		HttpResult httpResult = http.delete(config.getSubscribeURL() + "/" + subscribeId);
		// 提取返回数据Response下的StatusCode状态码
		LAPIResponse lapiResponse = httpResult.readLAPIResponse();
		System.out.println(httpResult.readResponseString()); // 打印返回数据
		String AlarmReturnData = "Method：DELETE" + "\n" + "URL：" + config.getSubscribeURL() + "/" + subscribeId + "\n" + "返回数据：" + httpResult.readResponseString();
		log.info(AlarmReturnData);
		int statusCode = lapiResponse.getResponseCode();

		if (statusCode == 0) {
			log.info("取消订阅成功");
			return true;
		} else {
			log.info("取消订阅失败");
			return false;
		}
	}

	/**
	 * 停止刷新订阅
	 */
	public void stopRefreshAndCancelSubscription() {
		this.isStop = true;
		// 取消订阅
		cancelSubscription(this.subscribeId);
		// Log.info("停止订阅成功");
	}

	public boolean isStop() {
		return isStop;
	}

}
