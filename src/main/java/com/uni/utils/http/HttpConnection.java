package com.uni.utils.http;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpConnection {
	HttpURLConnection connection;

	// private Map<String, String> responseHeadersMap;
	private Map<String, String> requestHeadersMap;

	private Integer statusCode;
	// private String statusDescrib;

	public HttpConnection() {
		requestHeadersMap = new HashMap<>();
		this.statusCode = -1;

		// addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)
		// AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36
		// Edge/18.17763");
		addRequestHeader("Connection", "Close");
		// addRequestHeader("Accept", "text/html, image/gif, image/jpeg, *; q=.2, */*;
		// q=.2");
		// addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	}

	public Integer getStatusCode() {
		return this.statusCode;
	}

	public String getResponseHeader(String name) {
		return connection.getHeaderField(name);
	}

	private HttpURLConnection openConnection(String urlStr) throws UnknownHostException, IOException {
		URL url;
		try {

			url = new URI(urlStr).toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			return connection;
		} catch (MalformedURLException e) {
			log.error(e.getMessage(), e);
		} catch (URISyntaxException e) {
			log.error("URL路径错误 {}", e.getMessage(), e);
		} catch (IOException e) {
			log.error("连接错误 {}", e.getMessage(), e);
		}
		return null;
	}

	public void addRequestHeader(String key, String value) {
		requestHeadersMap.put(key, value);
	}

	public void sendRequest(String method, String url, String requestBody)
			throws UnknownHostException, IOException, URISyntaxException {

		// 创建连接
		this.connection = openConnection(url);

		addRequestHeader("Host", this.connection.getURL().toURI().getHost());

		if (connection == null) {
			return;
		}
		// 设置请求方法
		connection.setRequestMethod(method);

		for (String key : requestHeadersMap.keySet()) {
			connection.setRequestProperty(key, requestHeadersMap.get(key));
		}

		// 设置请求体
		if (requestBody != null && !requestBody.isEmpty()) {
			connection.setDoOutput(true);
			OutputStream os = connection.getOutputStream();
			byte[] input = requestBody.getBytes("utf-8");
			os.write(input, 0, input.length);
		}

		this.statusCode = connection.getResponseCode();
	}

	public byte[] readResponse() throws IOException {
		String contentLengthStr = getResponseHeader("Content-Length");
		int contentLength = 8192;
		if (contentLengthStr != null) {
			contentLength = Integer.parseInt(contentLengthStr.replaceAll(" ", ""));
		}
		byte[] data = new byte[contentLength];

		InputStream input = null;
		if (this.statusCode == HttpURLConnection.HTTP_OK) {
			input = this.connection.getInputStream();
		} else {
			input = this.connection.getErrorStream();
		}

		int len = -1;
		int allDataLength = 0;
		if (contentLengthStr != null) {
			while ((len = input.read(data, allDataLength, contentLength - allDataLength)) != -1) {
				allDataLength += len;
				if (allDataLength == contentLength) {
					return data;
				}
			}
		} else {
			byte[] allData = new byte[contentLength];

			try {
				while ((len = input.read(data, 0, contentLength)) != -1) {
					if (allDataLength + len >= allData.length) {
						byte[] newData = new byte[allData.length * 2];
						System.arraycopy(allData, 0, newData, 0, allDataLength);
						allData = newData;
					}
					System.arraycopy(data, 0, allData, allDataLength, len);
					allDataLength += len;
				}
			} catch (SocketTimeoutException e) {

			}
			if (allDataLength > 0) {
				byte[] tempChars = new byte[allDataLength];
				System.arraycopy(allData, 0, tempChars, 0, allDataLength);
				return tempChars;
			}
		}
		input.close();
		return null;
	}
}
