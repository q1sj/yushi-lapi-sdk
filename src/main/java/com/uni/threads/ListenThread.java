package com.uni.threads;

import com.xsy.yushi.lapi.handle.YuShiSDKMessageHandler;
import com.xsy.yushi.lapi.config.YuShiLapiConfig;
import com.xsy.yushi.lapi.config.YuShiLapiConfigs;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Objects;

@Slf4j
public class ListenThread extends Thread {
	private boolean isCancle = false;
	private int port;

	private List<YuShiSDKMessageHandler> handlers;
	private YuShiLapiConfigs configs;

	public ListenThread(int port, List<YuShiSDKMessageHandler> handlers, YuShiLapiConfigs configs) {
		super("yushi-lapi-listen-" + port);
		setDaemon(true);
		this.port = port;
		this.handlers = handlers;
		this.configs = configs;
	}

	/**
	 * demo接收告警的方式只是简单开启一个tcp socket接收数据
	 * 实际上设备推送的是HTTP报文，可以考虑开启一个HTTP服务端去接收告警
	 */
	public void run() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			log.info("正在监听端口...{}", port);
			while (!isCancle) {
				try (Socket socket = serverSocket.accept()) {
					// 接受告警数据
					String[] requestInfoArr = processAlarmRequest(socket);
					String url = requestInfoArr[0];
					log.info("从 {} 接收到告警 告警url:{}", socket.getInetAddress(), url);
					String requestJson = requestInfoArr[1];
					for (YuShiSDKMessageHandler handler : handlers) {
						if (!Objects.equals(handler.supportUri(), url)) {
							continue;
						}
						for (YuShiLapiConfig config : configs.getLapi()) {
							if (config.getIp().equals(socket.getInetAddress().toString().replace("/", ""))) {
								handler.handle(config, requestJson);
							}
						}
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	// 停止监听
	public void stopListen() {
		this.isCancle = true;
	}

	// 接收告警返回数据，返回的数组里，下标0是url，下标1是请求体数据
	private String[] processAlarmRequest(Socket socket) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
		String line;
		int contentLength = 0;
		StringBuilder sb = new StringBuilder();

		// 读取
		line = reader.readLine();
		String url = line.split(" ")[1];
		while ((line = reader.readLine()) != null) {
			if (line.trim().isEmpty()) {
				// 空行表示请求头结束
				break;
			}
			if (line.toLowerCase().startsWith("content-length")) {
				contentLength = Integer.parseInt(line.split(":")[1].trim());
			}
		}
		if (contentLength == 0) {
			socket.close();
			return new String[]{url, sb.toString()};
		}
		char[] buffer = new char[contentLength];
		int bytesRead = 0;
		int offSet = 0;
		while (offSet < contentLength) {
			bytesRead = reader.read(buffer, offSet, contentLength - offSet);
			if (bytesRead == -1) {
				break; // 如果 reader.read 返回 -1，表示没有更多数据可读，循环会终止
			}
			offSet += bytesRead;
		}

		sb.append(buffer, 0, offSet);

		socket.close();

		return new String[]{url, sb.toString()};
	}
}